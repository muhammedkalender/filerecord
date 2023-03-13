package dev.siyah.filemanager.handler;

import dev.siyah.filemanager.exception.FileDeleteException;
import dev.siyah.filemanager.exception.FileMoveException;
import dev.siyah.filemanager.exception.FileSaveException;
import dev.siyah.filemanager.utility.ExceptionUtility;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class FileRecordExceptionHandler {
    @ExceptionHandler(BindException.class)
    public ResponseEntity<?> handleBindExceptions(BindException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getAllErrors()
                .forEach((error) -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName,
                            errorMessage);
                });

        return ExceptionUtility.createExceptionResponse(HttpStatus.BAD_REQUEST, errors);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentExceptions(IllegalArgumentException ignored) {
        return ExceptionUtility.createExceptionResponse(HttpStatus.BAD_REQUEST, "Some variables not allowed");
    }

    @ExceptionHandler(FileMoveException.class)
    public ResponseEntity<?> handleFileMoveExceptions(FileMoveException ignored) {
        return ExceptionUtility.createExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, "File not moved");
    }

    @ExceptionHandler(FileSaveException.class)
    public ResponseEntity<?> handleFileSaveExceptions(FileSaveException ignored) {
        return ExceptionUtility.createExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, "File not saved");
    }

    @ExceptionHandler(FileDeleteException.class)
    public ResponseEntity<?> handleFileDeleteExceptions(FileDeleteException ignored) {
        return ExceptionUtility.createExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, "File not deleted");
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundExceptions(EntityNotFoundException ignored) {
        return ExceptionUtility.createExceptionResponse(HttpStatus.NOT_FOUND, "Record not found");
    }
}
