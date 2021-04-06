package com.geeks18.service;

import com.geeks18.dto.JobMessage;
import com.geeks18.events.EventStream;
import com.geeks18.exceptions.ErrorConstants;
import com.geeks18.exceptions.MessageCommunicationException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

@Service
public class EventStreamService {
    @Autowired
    private EventStream eventStream;
    org.slf4j.Logger logger = LoggerFactory.getLogger(EventStreamService.class);
    public Boolean produceEvent(JobMessage msg)  {
        logger.info("Producing events --> id: "+ msg.getJobId() +" name: Actual message: "+ msg.getPayloadUrl());

        MessageChannel messageChannel = eventStream.producer();
        boolean status = messageChannel.send(MessageBuilder.withPayload(msg)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build());
        if(!status){
            throw new MessageCommunicationException(ErrorConstants.ERR_1_CD,ErrorConstants.ERR_1_DESC);
        }

        return status;
    }

}
