package com.geeks18.service;

import com.geeks18.dto.FileData;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class PtcFileService {

    public FileData getFile(String fileUri) {


        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity(headers);

        ResponseEntity<FileData> response= restTemplate.exchange(fileUri, HttpMethod.GET, entity, FileData.class);

        return response.getBody();

    }
}
