package com.geeks18.service;

import com.geeks18.dto.FileData;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * FileService is to communicate to file-service application to download and upload file
 */
@Service
public class PtcFileService {
    /**
     * Method to retrieve the file from the file-service
     * @param fileUri
     * @return
     */
    public FileData getFile(String fileUri) {


        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity(headers);

        ResponseEntity<FileData> response= restTemplate.exchange(fileUri, HttpMethod.GET, entity, FileData.class);

        return response.getBody();

    }
}
