package dev.siyah.filemanager.model.response.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse {
    private int status;
    private HttpStatus error;
    private Object message;
}

