package com.geeks18.service;

import com.geeks18.dto.FileData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;

import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;



@RunWith(MockitoJUnitRunner.class)
public class FileUploaderServiceTest {

    @InjectMocks
    FileUploaderService fileUploaderService;
    @Mock
    RestTemplate restTemplate;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(fileUploaderService, "url", "htttp://helloworld.com");

    }

    @Test
    public void testupload() {
        HttpHeaders headers = new HttpHeaders();
        Map<String, String> map = new HashMap<>();
        HttpEntity<String> entity = new HttpEntity(map, headers);
        FileData fileData=new FileData();
        fileData.setFileId("file001");
        ResponseEntity<FileData> response=new ResponseEntity<FileData>(fileData,HttpStatus.ACCEPTED);
        String url ="htttp://helloworld.com";

        assertThat(fileUploaderService.uploadFile("encoding","md5","content","filename")).isNull();

    }
}
