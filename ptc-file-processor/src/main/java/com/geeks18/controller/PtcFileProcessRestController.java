package com.geeks18.controller;

import com.geeks18.data.JobEntity;
import com.geeks18.dto.JobMessage;
import com.geeks18.exceptions.*;
import com.geeks18.repository.JobRepository;
import com.geeks18.service.EventStreamService;
import com.geeks18.service.FileUploaderService;
import com.geeks18.util.ProcessorUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.openapitools.api.JobApi;
import org.openapitools.model.FileRequest;
import org.openapitools.model.FileResponse;
import org.openapitools.model.Job;
import org.openapitools.model.StatusResponse;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;

/**
 * RestController API for processing file
 */
@RestController
public class PtcFileProcessRestController implements JobApi {

    @Autowired
    FileUploaderService fileUploaderService;

    @Value("${remote.storage.url}")
    String blobUrl;
    @Autowired
    JobRepository jobRepository;

    @Autowired
    private EventStreamService eventStreamService;
    org.slf4j.Logger logger = LoggerFactory.getLogger(PtcFileProcessRestController.class);

    /**
     * API to get the Job Details by JobId
     * @param jobId Job Id (required)
     * @return
     */
    @Override
    public ResponseEntity<Job> getJobDetails(String jobId) {
        Job job = new Job();
        JobEntity entity = jobRepository.findById(Long.valueOf(jobId)).orElseThrow(() -> {
            logger.error("Job not found with job Id " + jobId);
            return new ResourceNotFoundException(ErrorConstants.ERR_3_CD, ErrorConstants.ERR_3_DESC + "  " + jobId);
        });
        job.setStatus(entity.getJobStatus());
        job.setClientId(entity.getClientId());
        job.setPayloadLocation(entity.getPayloadLocation());
        job.setPayloadSize(entity.getPayloadSize());
        job.setTenantId(entity.getTenantId());
        job.setId(String.valueOf(entity.getJobId()));

        return ResponseEntity.status(HttpStatus.OK).body(job);
    }

    /**
     * API to get the Job Status by JobId
     * @param jobId Job Id (required)
     * @return
     */
    @Override
    public ResponseEntity<StatusResponse> getJobStatus(String jobId) {
        StatusResponse response = new StatusResponse();

        JobEntity entity = jobRepository.findById(Long.valueOf(jobId)).orElseThrow(() -> {
            logger.error("Job not found with job Id " + jobId);
            return new ResourceNotFoundException(ErrorConstants.ERR_3_CD, ErrorConstants.ERR_3_DESC + "  " + jobId);
        });

        response.setStatus(entity.getJobStatus());


        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * API to submit a Job with the binary data
     * It also validates the Authorization token and the Md5 hash with the binary file
     * @param authorization  (required)
     * @param fileRequest  (required)
     * @return
     */
    @Override
    public ResponseEntity<FileResponse> submitJob(String authorization, @Valid FileRequest fileRequest) {
        String tenantId = "";
        String clientId = "";
        JobEntity entity = new JobEntity();
        try {

            validateMd5CheckSum(fileRequest); // Validates the Md5 checksum with the requested binary

            validateJWTToken(authorization, entity); //Validate JWT token and retrieves the client and tenant id


        } catch (JSONException e) {
            logger.error("Error in parsing json web token", e);
            throw new JWTTokenValidationException(ErrorConstants.ERR_2_CD, ErrorConstants.ERR_2_DESC);
        } catch (NoSuchAlgorithmException e) {
            logger.error("Error in validating the md5 hash", e);
            throw new RequestValidationException(ErrorConstants.ERR_6_CD, ErrorConstants.ERR_6_DESC);
        }

        //Uploads the file to file-storage sevice
        String blobId = fileUploaderService.uploadFile(fileRequest.getEncoding(), fileRequest.getMd5(), fileRequest.getContent(), fileRequest.getFileName());

        if (!StringUtils.hasLength(blobId)) {

            throw new MessageCommunicationException(ErrorConstants.ERR_4_CD, ErrorConstants.ERR_4_DESC);
        }


        entity.setJobStatus("RUNNING");
        //Save the Job in the databse in running status
        jobRepository.save(entity);

        JobMessage message = new JobMessage();
        message.setJobId(String.valueOf(entity.getJobId()));
        message.setPayloadSize(String.valueOf(fileRequest.getContent().length()));
        message.setPayloadUrl(blobUrl + "/" + blobId);

        try {
            //Sends the message to the Message Queue
            eventStreamService.produceEvent(message);
        } catch (Exception e) {
            logger.error("Error in Producing Events");
            entity.setJobStatus("FAILED");
            jobRepository.save(entity);
            throw new MessageCommunicationException(ErrorConstants.ERR_1_CD, ErrorConstants.ERR_1_DESC);
        }

        //Sends back the response
        FileResponse response = new FileResponse();
        response.setPayloadSize(String.valueOf(fileRequest.getContent().length()));
        response.setJobId(String.valueOf(entity.getJobId()));
        response.setPayloadLocation(blobUrl + "/" + blobId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Validate JWT token and retrieves the client and tenant id
     * @param authorization
     * @param entity
     */
    private void validateJWTToken(String authorization, JobEntity entity) {
        JSONObject obj = ProcessorUtil.decodeJWTToken(authorization);
        if (!obj.has("tid") || !obj.has("oid"))
            throw new JWTTokenValidationException(ErrorConstants.ERR_2_CD, ErrorConstants.ERR_2_DESC);

        entity.setTenantId(String.valueOf(obj.get("tid")));
        entity.setClientId(String.valueOf(obj.get("oid")));
    }

    /**
     * Validates the Md5 checksum with the requested binary
     * @param fileRequest
     * @throws NoSuchAlgorithmException
     */
    private void validateMd5CheckSum(FileRequest fileRequest) throws NoSuchAlgorithmException {
        if (!ProcessorUtil.isCheckSumValid(fileRequest.getContent(), fileRequest.getMd5())) {
            logger.error("MD5 hash validation failed");
            throw new RequestValidationException(ErrorConstants.ERR_6_CD, ErrorConstants.ERR_6_DESC);

        }
    }


}
