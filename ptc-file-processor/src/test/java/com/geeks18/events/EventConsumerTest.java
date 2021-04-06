package com.geeks18.events;

import com.geeks18.data.JobEntity;
import com.geeks18.dto.JobStatusMessage;
import com.geeks18.repository.JobRepository;
import com.geeks18.service.FileUploaderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class EventConsumerTest {

    @InjectMocks
    EventConsumer eventConsumer;
    @Mock
    JobRepository jobRepository;

    @Before
    public void setup(){
        ReflectionTestUtils.setField(eventConsumer, "url", "htttp://helloworld.com");

    }

    @Test
    public void testConsumer_true() {
        JobStatusMessage msg=new JobStatusMessage("1","test","SUCCESS","254048");
        String jobId="1";
        JobEntity entity= new JobEntity();
        given(jobRepository.findById(Long.valueOf(jobId))).willReturn(Optional.of(entity));
        eventConsumer.consumeEvent(msg);
        assertThat(entity.getJobStatus()).isNotNull().contains("SUCCESS");
        assertThat(entity.getPayloadSize()).isNotNull().contains(msg.getPayloadSize());



    }



}
