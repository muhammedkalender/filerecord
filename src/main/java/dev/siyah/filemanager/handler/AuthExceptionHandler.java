package dev.siyah.filemanager.handler;

import dev.siyah.filemanager.model.response.common.ExceptionResponse;
import dev.siyah.filemanager.utility.ExceptionUtility;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class AuthExceptionHandler {
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    @ApiResponse(responseCode = "401", content = @Content(
            schema = @Schema(
                    implementation = ExceptionResponse.class
            ),
            examples = @ExampleObject(
                    name = "Invalid Credentials",
                    value = "{\"status\": 401, \"error\": \"UNAUTHORIZED\", \"message\": \"Invalid Credentials\" }"
            )
    ))
    public ResponseEntity<?> handleBadCredentialsExceptions(BadCredentialsException ignored) {
        return ExceptionUtility.createExceptionResponse(HttpStatus.UNAUTHORIZED, "Invalid credentials");
    }

    @ExceptionHandler(JwtException.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    @ApiResponse(
            responseCode = "403",
            content = @Content(
                schema = @Schema(
                    implementation = ExceptionResponse.class
                ),
                examples = @ExampleObject(
                    name = "Expired JWT",
                    value = "{\"status\": 403, \"error\": \"FORBIDDEN\", \"message\": \"Forbidden Access\" }"
                )
            )
    )
    public ResponseEntity<?> handleJwtExceptions(JwtException ignored) {
        return ExceptionUtility.createExceptionResponse(HttpStatus.FORBIDDEN, "Forbidden Access");
    }
}
