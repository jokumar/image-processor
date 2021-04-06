package com.geeks18.events;



import com.geeks18.dto.JobStatusMessage;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

/**
 * EventStream Service to send the event stream to the producer topic
 */
@Service
public class EventStreamService {
    @Autowired
    private EventStream eventStream;
    org.slf4j.Logger logger = LoggerFactory.getLogger(EventStreamService.class);

    public Boolean sendResponse(JobStatusMessage msg) {
        logger.info("Outbound events --> id: "+ msg.getJobId() +" name:  "+ msg.getName() +" Status: "+ msg.getStatus());

        MessageChannel messageChannel = eventStream.producer();
        return messageChannel.send(MessageBuilder.withPayload(msg)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build());

    }

}