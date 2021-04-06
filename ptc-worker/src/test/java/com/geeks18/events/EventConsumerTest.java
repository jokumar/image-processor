package com.geeks18.events;

import com.geeks18.dto.FileData;
import com.geeks18.dto.JobMessage;
import com.geeks18.service.PtcFileService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class EventConsumerTest {

    @InjectMocks
    EventConsumer eventConsumer;
    @Mock
    PtcFileService ptcFileService;
    @Mock
    EventStreamService eventStreamService;

    @Test
    public void testConsumer_failed() {

        JobMessage message=getMessage();
        FileData fileData = new FileData();
        fileData.setFileId("1234");
        fileData.setFileSize("254048");
        given(ptcFileService.getFile(message.getPayloadUrl())).willReturn(fileData);
        eventConsumer.consumeEvent(message);

    }


    @Test
    public void testConsumer_running() {

        JobMessage message=getMessage();
        FileData fileData = new FileData();
        fileData.setFileId("1233");
        fileData.setFileSize("254048");
        given(ptcFileService.getFile(message.getPayloadUrl())).willReturn(fileData);
        eventConsumer.consumeEvent(message);

    }

    @Test
    public void testConsumer_success() {

        JobMessage message=getMessage();
        FileData fileData = new FileData();
        fileData.setFileId("5999");
        fileData.setFileSize("254048");
        given(ptcFileService.getFile(message.getPayloadUrl())).willReturn(fileData);
        eventConsumer.consumeEvent(message);

    }


    @Test
    public void testConsumer_clientError() {

        JobMessage message=getMessage();
        FileData fileData = new FileData();
        fileData.setFileId("5999");
        fileData.setFileSize("254048");
        given(ptcFileService.getFile(message.getPayloadUrl())).willThrow(new HttpClientErrorException(HttpStatus.BAD_GATEWAY));
        eventConsumer.consumeEvent(message);

    }


    public JobMessage getMessage(){
        JobMessage message = new JobMessage();
        message.setClientId("1");
        message.setTenantId("1");
        message.setPayloadUrl("htttp://helloworld.com");
        message.setPayloadSize("254048");
        message.setJobId("1");
        return message;
    }
}
