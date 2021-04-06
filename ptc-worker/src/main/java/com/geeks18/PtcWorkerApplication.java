package com.geeks18;

import com.geeks18.events.EventStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

@SpringBootApplication
@EnableBinding(EventStream.class)
public class PtcWorkerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PtcWorkerApplication.class, args);
	}

}
