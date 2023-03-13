package dev.siyah.filemanager.utility;

import dev.siyah.filemanager.model.response.common.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ExceptionUtility {
    public static ResponseEntity<?> createExceptionResponse(HttpStatus httpStatus, Object message) {
        return new ResponseEntity<>(
                ExceptionResponse.builder()
                        .message(message)
                        .error(httpStatus)
                        .status(httpStatus.value())
                        .build(),
                httpStatus);
    }
}
