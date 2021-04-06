package com.geeks18.controller;

import com.geeks18.data.JobEntity;
import com.geeks18.dto.JobMessage;
import com.geeks18.exceptions.*;
import com.geeks18.repository.JobRepository;
import com.geeks18.service.EventStreamService;
import com.geeks18.service.FileUploaderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.openapitools.model.FileRequest;
import org.openapitools.model.FileResponse;
import org.openapitools.model.Job;
import org.openapitools.model.StatusResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class PtcFileProcessRestControllerTest {
    @InjectMocks
    PtcFileProcessRestController ptcFileProcessRestController;

    @Mock
    JobRepository jobRepository;
    @Mock
    FileUploaderService fileUploaderService;
    @Mock
    private EventStreamService eventStreamService;


    @Before
    public void setup(){
        ReflectionTestUtils.setField(ptcFileProcessRestController, "blobUrl", "htttp://helloworld.com");

    }

    @Test
    public void getJobDetails_OK(){

        //Given
        String jobId="1";
        JobEntity entity= getJobEntity_Set1();
        given(jobRepository.findById(Long.valueOf(jobId))).willReturn(Optional.of(entity));
        //when
         ResponseEntity<Job>  jobResponse= ptcFileProcessRestController.getJobDetails(jobId);

         //then

        assertThat(jobResponse)
                .isNotNull()
                .extracting(ResponseEntity::getStatusCode)
                .hasSameHashCodeAs(HttpStatus.OK);

        assertThat(jobResponse.getBody())
                .extracting(Job::getClientId,Job::getStatus,Job::getPayloadLocation,Job::getTenantId)
                .contains(entity.getClientId(),entity.getJobStatus(),entity.getPayloadLocation(),entity.getTenantId());

    }
    @Test(expected = ResourceNotFoundException.class)
    public void getJobDetails_ResourceNotFound(){
        //Given
        String jobId="1";

        given(jobRepository.findById(Long.valueOf(jobId))).willThrow(new ResourceNotFoundException(ErrorConstants.ERR_3_CD,ErrorConstants.ERR_3_DESC+ "  "+ jobId));
        //when
        ResponseEntity<Job>  jobResponse= ptcFileProcessRestController.getJobDetails(jobId);
    }


    @Test
    public void getJobStatus_OK(){

        //Given
        String jobId="1";
        JobEntity entity= getJobEntity_Set1();
        given(jobRepository.findById(Long.valueOf(jobId))).willReturn(Optional.of(entity));
        //when
        ResponseEntity<StatusResponse>  jobResponse= ptcFileProcessRestController.getJobStatus(jobId);
        //then

        assertThat(jobResponse)
                .isNotNull()
                .extracting(ResponseEntity::getStatusCode)
                .hasSameHashCodeAs(HttpStatus.OK);
        assertThat(jobResponse.getBody())
                .extracting(StatusResponse::getStatus)
                .hasSameHashCodeAs(entity.getJobStatus());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getJobStatus_ResourceNotFound(){
        //Given
        String jobId="1";

        given(jobRepository.findById(Long.valueOf(jobId))).willThrow(new ResourceNotFoundException(ErrorConstants.ERR_3_CD,ErrorConstants.ERR_3_DESC+ "  "+ jobId));
        //when
        ResponseEntity<StatusResponse>  jobResponse= ptcFileProcessRestController.getJobStatus(jobId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void submitJob_Md5_wrongbase64Validation(){

        //Given
        String authorization="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJ0aWQiOjEsIm9pZCI6MSwiYXVkIjoiY29tLmNvbXBhbnkuam9ic2VydmljZSIsImF6cCI6IjEiLCJlbWFpbCI6ImN1c3RvbWVyQG1haWwuY29tIn0.CcTapGbWX0UEMovUwC8kAcWMUxmbOeO0qhsu-wqHQH0";
        FileRequest fileRequest= new FileRequest();
        fileRequest.setContent("wrong_base64 binary data");
        fileRequest.setEncoding("base64");
        fileRequest.setMd5("091c349785af1c187907d3afb054891a");
        JobEntity entity= getJobEntity_Set1();

        //when
        ResponseEntity<FileResponse>  fileResponse= ptcFileProcessRestController.submitJob(authorization,fileRequest);
        //then


    }



    @Test(expected = RequestValidationException.class)
    public void submitJob_Md5Validation() throws IOException{

        //Given
        String authorization="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJ0aWQiOjEsIm9pZCI6MSwiYXVkIjoiY29tLmNvbXBhbnkuam9ic2VydmljZSIsImF6cCI6IjEiLCJlbWFpbCI6ImN1c3RvbWVyQG1haWwuY29tIn0.CcTapGbWX0UEMovUwC8kAcWMUxmbOeO0qhsu-wqHQH0";
        FileRequest fileRequest= new FileRequest();
        fileRequest.setContent(getBinaryData());
        fileRequest.setEncoding("base64");
        fileRequest.setMd5("091c349785af1c187908d5afb054891a");//Wrong Md5
        JobEntity entity= getJobEntity_Set1();
        //when
        ResponseEntity<FileResponse>  fileResponse= ptcFileProcessRestController.submitJob(authorization,fileRequest);

    }


    @Test(expected = JWTTokenValidationException.class)
    public void submitJob_JWTValidation_withoutTenantId() throws IOException{

        //Given
        //Valid token without tenant id
        String authorization="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJhdWQiOiJjb20uY29tcGFueS5qb2JzZXJ2aWNlIiwiYXpwIjoiMSIsImVtYWlsIjoiY3VzdG9tZXJAbWFpbC5jb20ifQ.yYFs1Unt4CK9yTFO5s86BnQ2qtegipbkXsMpP9KoEew";
        FileRequest fileRequest=  getFileRequest();
        JobEntity entity= getJobEntity_Set1();
        //when
        ResponseEntity<FileResponse>  fileResponse= ptcFileProcessRestController.submitJob(authorization,fileRequest);
        //then

    }

    @Test(expected = JWTTokenValidationException.class)
    public void submitJob_JWTValidation() throws IOException{

        //Given
        //Wrong authorization token
        String authorization="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIswibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJ0aWQiOjEsIm9pZCI6MSwiYXVkIjoiY29tLmNvbXBhbnkuam9ic2VydmljZSIsImF6cCI6IjEiLCJlbWFpbCI6ImN1c3RvbWVyQG1haWwuY29tIn0.CcTapGbWX0UEMovUwC8kAcWMUxmbOeO0qhsu-wqHQH0";// Wrong JWT Token
        FileRequest fileRequest=  getFileRequest();
        JobEntity entity= getJobEntity_Set1();
        //when
        ResponseEntity<FileResponse>  fileResponse= ptcFileProcessRestController.submitJob(authorization,fileRequest);
        //then

    }




    @Test(expected = MessageCommunicationException.class)
    public void submitJob_Error_File_Upload() throws IOException{

        //Given
        String authorization="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJ0aWQiOjEsIm9pZCI6MSwiYXVkIjoiY29tLmNvbXBhbnkuam9ic2VydmljZSIsImF6cCI6IjEiLCJlbWFpbCI6ImN1c3RvbWVyQG1haWwuY29tIn0.CcTapGbWX0UEMovUwC8kAcWMUxmbOeO0qhsu-wqHQH0";
        FileRequest fileRequest=  getFileRequest();
        given(fileUploaderService.uploadFile(fileRequest.getEncoding(),fileRequest.getMd5(),fileRequest.getContent(),fileRequest.getFileName())).willReturn(null);

        //when
        ResponseEntity<FileResponse>  fileResponse= ptcFileProcessRestController.submitJob(authorization,fileRequest);
        //then

    }



    @Test
    public void submitJob_CREATED() throws IOException{

        //Given
        String authorization="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJ0aWQiOjEsIm9pZCI6MSwiYXVkIjoiY29tLmNvbXBhbnkuam9ic2VydmljZSIsImF6cCI6IjEiLCJlbWFpbCI6ImN1c3RvbWVyQG1haWwuY29tIn0.CcTapGbWX0UEMovUwC8kAcWMUxmbOeO0qhsu-wqHQH0";
        FileRequest fileRequest=  getFileRequest();
        String jobId="1";
        String blobId="2020";
        JobEntity entity= getJobEntity_Set1();

        given(fileUploaderService.uploadFile(fileRequest.getEncoding(),fileRequest.getMd5(),fileRequest.getContent(),fileRequest.getFileName())).willReturn(blobId);

        JobMessage message=new JobMessage();


        //when
        ResponseEntity<FileResponse>  fileResponse= ptcFileProcessRestController.submitJob(authorization,fileRequest);
        //then

        assertThat(fileResponse)
                .isNotNull()
                .extracting(ResponseEntity::getStatusCode)
                .hasSameHashCodeAs(HttpStatus.CREATED);


        assertThat(fileResponse.getBody())
                .extracting(FileResponse::getPayloadLocation,FileResponse :: getPayloadSize)
                .contains(entity.getPayloadLocation()+"/"+blobId,entity.getPayloadSize());
    }

    public FileRequest getFileRequest() throws IOException{
        FileRequest fileRequest= new FileRequest();
        fileRequest.setContent(getBinaryData());
        fileRequest.setEncoding("base64");
        fileRequest.setMd5("091c349785af1c187907d3afb054891a");
        return fileRequest;

    }

    public JobEntity getJobEntity_Set1(){
        JobEntity entity=new JobEntity();
        entity.setJobStatus("SUCCESS");
        entity.setClientId("1");
        entity.setTenantId("2");
        entity.setPayloadLocation("htttp://helloworld.com");
        entity.setPayloadSize("254048");
        return entity;
    }
    public String getBinaryData() throws IOException {

        byte[] bytes = Files.readAllBytes(Paths.get("src/test/resources/cbimage.jpg"));
        return Base64.getEncoder().encodeToString(bytes);

    }




}
