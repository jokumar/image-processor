package com.geeks18.service;

import com.geeks18.dto.FileData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@Service
public class FileUploaderService {
    @Autowired
    RestTemplate restTemplate;

    Logger logger = LoggerFactory.getLogger(FileUploaderService.class);

    @Value("${remote.storage.url}")
    String url;

    public String uploadFile(String encoding, String md5, String content, String fileName) {



        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        Map<String, String> map = new HashMap<>();

        map.put("filename", fileName);
        map.put("encoding", encoding);
        map.put("file", content);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentLength(content.length());

        HttpEntity<String> entity = new HttpEntity(map, headers);

        ResponseEntity<FileData> response = restTemplate.exchange(url, HttpMethod.PUT, entity, FileData.class);

        return response!=null ? ((FileData) response.getBody()).getFileId():null;

    }
}
