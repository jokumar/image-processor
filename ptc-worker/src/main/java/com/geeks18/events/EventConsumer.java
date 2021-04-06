package com.geeks18.events;

import com.geeks18.dto.FileData;
import com.geeks18.dto.JobMessage;
import com.geeks18.dto.JobStatus;
import com.geeks18.dto.JobStatusMessage;
import com.geeks18.service.PtcFileService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.client.HttpClientErrorException;


@Configuration
public class EventConsumer {
    org.slf4j.Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    @Autowired
    PtcFileService ptcFileService;

    @Autowired
    EventStreamService eventStreamService;

    @StreamListener(EventStream.INBOUND)
    public void consumeEvent(@Payload JobMessage msg) {
        String jobStatus=JobStatus.SUCCESS.name();
        FileData fileData = new FileData() ;
        try {
            //Retrieve the file from the storage
            fileData = ptcFileService.getFile(msg.getPayloadUrl());

            //Process the file - code for processing can follow here
            int fileId=Integer.valueOf(fileData.getFileId());
            if(fileId%2 ==0){ // this is to simulate the failed behaviour where if the fileid is even number then status is FAILED
                jobStatus=JobStatus.FAILED.name();
                logger.info("File is in FAILED state ");
            }else if(fileId>1000 && fileId <5000){
                logger.info("File is in RUNNING state ");
                jobStatus=JobStatus.RUNNING.name();// this is to simulate the running behaviour
            }


            //Upload the file to the storage and get the new location for the new file .

            logger.info("Inbound message--> id: " + msg.getJobId() + "  Url "
                    + msg.getPayloadUrl() + "||  bytePayload: " + fileData.getFile()+
                    " ||  payloadSize "+fileData.getFileSize() + " TenantId : "+ msg.getTenantId() + " Client Id : "+msg.getClientId());
        }
        catch(HttpClientErrorException e){
            jobStatus=JobStatus.FAILED.name();
            logger.error("HttpClientErrorException in fetching the file from remote storage"+e);
        }
        catch(Exception e){
            jobStatus=JobStatus.FAILED.name();
            logger.error(" Error in fetching the file from remote storage "+e);
        }
        if(msg.getPayloadUrl()==null ){
            jobStatus=JobStatus.FAILED.name();
        }

        JobStatusMessage message =new JobStatusMessage(msg.getJobId(),fileData.getFileId(), jobStatus,fileData.getFileSize());
        eventStreamService.sendResponse(message);


    }

}