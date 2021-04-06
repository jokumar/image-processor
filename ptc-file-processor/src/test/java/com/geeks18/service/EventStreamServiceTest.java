package com.geeks18.service;

import com.geeks18.dto.JobMessage;
import com.geeks18.events.EventStream;
import com.geeks18.exceptions.MessageCommunicationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class EventStreamServiceTest {

    @InjectMocks
    EventStreamService eventStreamService;
    @Mock
    private EventStream eventStream;
    @Mock
    MessageChannel messageChannel;

    @Before
    public void setup() {

    }

    @Test
    public void testProduceEvent_true() {
        JobMessage msg = new JobMessage();

        given(eventStream.producer()).willReturn(messageChannel);
        given(messageChannel.send(any(Message.class))).willReturn(Boolean.TRUE);

        boolean status = eventStreamService.produceEvent(msg);
        assertThat(status).isTrue();

    }

    @Test(expected = MessageCommunicationException.class)
    public void testProduceEvent_false() {
        JobMessage msg = new JobMessage();

        given(eventStream.producer()).willReturn(messageChannel);
        given(messageChannel.send(any(Message.class))).willReturn(Boolean.FALSE);

        boolean status = eventStreamService.produceEvent(msg);
    }
}
