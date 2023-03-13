package dev.siyah.filemanager.utility;

import dev.siyah.filemanager.model.response.common.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ExceptionUtilityTest {

    @Test
    public void testCreateExceptionResponse() {
        ResponseEntity<?> actual = ExceptionUtility.createExceptionResponse(HttpStatus.OK, "test");
        ResponseEntity<?> excepted = new ResponseEntity<>(
                ExceptionResponse.builder()
                        .status(200)
                        .error(HttpStatus.OK)
                        .message("test")
                        .build(),
                HttpStatus.OK
        );

        Assert.assertEquals(actual, excepted);
    }
}