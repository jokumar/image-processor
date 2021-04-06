package com.geeks18.util;

import com.geeks18.exceptions.JWTTokenValidationException;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class ProcessorUtilTest {
    @InjectMocks
    ProcessorUtil processorUtil;

    @Before
    public void setup() {

    }

    @Test
    public void testDecodeJWTToken() throws JSONException {

        JSONObject jsonObject = processorUtil.decodeJWTToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJ0aWQiOjEsIm9pZCI6MSwiYXVkIjoiY29tLmNvbXBhbnkuam9ic2VydmljZSIsImF6cCI6IjEiLCJlbWFpbCI6ImN1c3RvbWVyQG1haWwuY29tIn0.CcTapGbWX0UEMovUwC8kAcWMUxmbOeO0qhsu-wqHQH0");
        assertThat(jsonObject.has("tid")).isTrue();
    }

    @Test(expected = JWTTokenValidationException.class)
    public void testDecodeJWTToken_no_sig() throws JSONException {

        JSONObject jsonObject = processorUtil.decodeJWTToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJ0aWQiOjEsIm9pZCI6MSwiYXVkIjoiY29tLmNvbXBhbnkuam9ic2VydmljZSIsImF6cCI6IjEiLCJlbWFpbCI6ImN1c3RvbWVyQG1haWwuY29tIn0");
        assertThat(jsonObject.has("tid")).isTrue();
    }

    @Test(expected = JWTTokenValidationException.class)
    public void testDecodeJWTToken_invalid() throws JSONException {
        JSONObject jsonObject = processorUtil.decodeJWTToken("eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJ0aWQiOjEsIm9pZCI6MSwiYXVkIjoiY29tLmNvbXBhbnkuam9ic2VydmljZSIsImF6cCI6IjEiLCJlbWFpbCI6ImN1c3RvbWVyQG1haWwuY29tIn0");
        assertThat(jsonObject.has("tid")).isTrue();
    }

    @Test(expected = JWTTokenValidationException.class)
    public void testDecodeJWTToken_invalid_2() throws JSONException {
        JSONObject jsonObject = processorUtil.decodeJWTToken("InvalidToken");
        assertThat(jsonObject.has("tid")).isTrue();
    }

    @Test
    public void testCheckSumValid() throws IOException, NoSuchAlgorithmException {
        assertThat(processorUtil.isCheckSumValid(getBinaryData(),"091c349785af1c187907d3afb054891a")).isTrue();

    }

    @Test
    public void testCheckSumInValid() throws IOException, NoSuchAlgorithmException {
        assertThat(processorUtil.isCheckSumValid(getBinaryData(),"091c549785af1c187907d3afb054891a")).isFalse();

    }
    public String getBinaryData() throws IOException {

        byte[] bytes = Files.readAllBytes(Paths.get("src/test/resources/cbimage.jpg"));
        return Base64.getEncoder().encodeToString(bytes);

    }

}
