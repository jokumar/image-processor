package com.geeks18.events;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface EventStream {
    String OUTBOUND = "event-producer";
    @Output(OUTBOUND)
    MessageChannel producer();

    String INBOUND = "event-consumer";
    @Input(INBOUND)
    MessageChannel consumer();
}