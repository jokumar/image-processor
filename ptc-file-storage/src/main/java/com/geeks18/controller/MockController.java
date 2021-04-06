package com.geeks18.controller;



import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ThreadLocalRandom;


@RestController
public class MockController {
org.slf4j.Logger logger = LoggerFactory.getLogger(MockController.class);


    @PutMapping(value = "/api/v1/blob",consumes = "application/json")
    ResponseEntity<?> uploadData(@RequestBody FileData file) {

        logger.info("Begin MockController uploadData " + file.getFileName());
        String fileName=file.getFileName();
        // generate a random number for getting a unique id
        int randomNum = ThreadLocalRandom.current().nextInt(1, 10000 + 1);

        file.setFileId(String.valueOf(randomNum));

        logger.info("Exit  MockController uploadData " + file.getFileId());
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(file);
    }

    @GetMapping(value = "/api/v1/blob/{fileId}",produces = "application/json")
    ResponseEntity<FileData> getData(@PathVariable String fileId) {

        logger.info("Begin MockController getData for " + fileId);
        FileData fileData = new FileData();
        fileData.setEncoding("base64");
        fileData.setFile( "/xAApEAEAAgICAgICAgMBAQEBAAABABEhMUFRYXEQgZGhscEg0eHw8TBA");
        fileData.setFileSize("12345");
        fileData.setFileId(fileId);
        logger.info("Exit MockController getData for " + fileId);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(fileData);
    }




}
