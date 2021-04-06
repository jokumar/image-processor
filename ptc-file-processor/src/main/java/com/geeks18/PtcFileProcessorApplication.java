package com.geeks18;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.geeks18.events.EventStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
@EnableBinding(EventStream.class)
public class PtcFileProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(PtcFileProcessorApplication.class, args);
	}

	@Bean
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(mapper);
		return converter;
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}




}