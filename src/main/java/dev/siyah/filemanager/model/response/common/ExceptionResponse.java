package dev.siyah.filemanager.model.response.common;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ExceptionResponse {
    private int status;
    private HttpStatus error;
    private Object message;
}

