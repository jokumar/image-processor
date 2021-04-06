package com.geeks18.events;



import com.geeks18.dto.JobStatusMessage;
import com.geeks18.repository.JobRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.Payload;



@Configuration
public class EventConsumer {
    org.slf4j.Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    @Autowired
    JobRepository jobRepository;
    @Value("${remote.storage.url}")
    String url;

    /**
     * Consume events from the Message Queue
     * @param msg
     */
    @StreamListener(EventStream.INBOUND)
    public void consumeEvent(@Payload JobStatusMessage msg) {
        jobRepository.findById(Long.valueOf(msg.getJobId())).ifPresent(entity->{
            entity.setJobStatus(msg.getStatus());
            logger.info("***** File is in "+msg.getStatus()+" state ********");
            if("SUCCESS".equalsIgnoreCase(msg.getStatus())) {
                    //If success set the paload location to download from and the payload size
                entity.setPayloadLocation(url + "/" + msg.getName());
                entity.setPayloadSize(msg.getPayloadSize());
            }
            jobRepository.save(entity);
        });
        ;

        logger.info("Inbound message--> id: " + msg.getJobId() + " name:  "+msg.getName()+"  Job Status: "
                + msg.getStatus() +" payload Size "+ msg.getPayloadSize());




    }
}
