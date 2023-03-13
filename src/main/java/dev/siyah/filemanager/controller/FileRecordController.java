package dev.siyah.filemanager.controller;

import dev.siyah.filemanager.model.request.file.CreateFileRecordRequest;
import dev.siyah.filemanager.model.request.file.UpdateFileRecordRequest;
import dev.siyah.filemanager.model.response.common.ExceptionResponse;
import dev.siyah.filemanager.model.response.file.FileRecordResponse;
import dev.siyah.filemanager.service.file.FileRecordService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/file-record")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class FileRecordController {
    private final ModelMapper modelMapper;
    private final FileRecordService fileRecordService;

    @GetMapping
    public List<FileRecordResponse> list() {
        return this.fileRecordService.list().stream().map((record) -> this.modelMapper.map(record, FileRecordResponse.class)).collect(Collectors.toList());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(
            responseCode = "500",
            content = @Content(
                    schema = @Schema(
                            implementation = ExceptionResponse.class
                    ),
                    examples = @ExampleObject(
                            name = "File not saved",
                            value = "{\"status\": 500, \"error\": \"INTERNAL_SERVER_ERROR\", \"message\": \"File not saved\" }"
                    )
            )
    )
    public FileRecordResponse createFileRecord(@Validated CreateFileRecordRequest createFileRecordRequest) throws IOException {
        return this.modelMapper.map(this.fileRecordService.create(createFileRecordRequest), FileRecordResponse.class);
    }

    @PutMapping(path = "{fileRecordId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ApiResponse(
            responseCode = "404",
            content = @Content(
                    schema = @Schema(
                            implementation = ExceptionResponse.class
                    ),
                    examples = {
                            @ExampleObject(
                                    name = "Record not found",
                                    value = "{\"status\": 404, \"error\": \"NOT_FOUND\", \"message\": \"Record not found\" }"
                            )
                    }
            )
    )
    @ApiResponse(
            responseCode = "500",
            content = @Content(
                    schema = @Schema(
                            implementation = ExceptionResponse.class
                    ),
                    examples = {
                            @ExampleObject(
                                    name = "File not deleted",
                                    value = "{\"status\": 500, \"error\": \"INTERNAL_SERVER_ERROR\", \"message\": \"File not deleted\" }"
                            ),
                            @ExampleObject(
                                    name = "File not moved",
                                    value = "{\"status\": 500, \"error\": \"INTERNAL_SERVER_ERROR\", \"message\": \"File not moved\" }"
                            )
                    }
            )
    )
    public FileRecordResponse updateFileRecord(@PathVariable UUID fileRecordId, @Validated UpdateFileRecordRequest updateFileRecordRequest) throws IOException {
        return this.modelMapper.map(this.fileRecordService.update(fileRecordId, updateFileRecordRequest), FileRecordResponse.class);
    }

    @GetMapping("{fileRecordId}")
    @ApiResponse(
            responseCode = "500",
            content = @Content(
                    schema = @Schema(
                            implementation = ExceptionResponse.class
                    ),
                    examples = {
                            @ExampleObject(
                                    name = "Record not found",
                                    value = "{\"status\": 404, \"error\": \"NOT_FOUND\", \"message\": \"Record not found\" }"
                            )
                    }
            )
    )
    public FileRecordResponse getFileRecordById(@PathVariable UUID fileRecordId) {
        return this.modelMapper.map(this.fileRecordService.getById(fileRecordId),
                FileRecordResponse.class);
    }

    @DeleteMapping("{fileRecordId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponse(
            responseCode = "500",
            content = @Content(
                    schema = @Schema(
                            implementation = ExceptionResponse.class
                    ),
                    examples = {
                            @ExampleObject(
                                    name = "File not deleted",
                                    value = "{\"status\": 500, \"error\": \"INTERNAL_SERVER_ERROR\", \"message\": \"File not deleted\" }"
                            )
                    }
            )
    )
    public void deleteFileRecordById(@PathVariable UUID fileRecordId) {
        this.fileRecordService.deleteById(fileRecordId);
    }

    @GetMapping("{fileRecordId}/download")
    @ApiResponse(
            responseCode = "404",
            content = @Content(
                    schema = @Schema(
                            implementation = ExceptionResponse.class
                    ),
                    examples = {
                            @ExampleObject(
                                    name = "Record not found",
                                    value = "{\"status\": 404, \"error\": \"NOT_FOUND\", \"message\": \"Record not found\" }"
                            )
                    }
            )
    )
    public byte[] downloadFileRecord(@PathVariable UUID fileRecordId) throws IOException {
        return this.fileRecordService.downloadById(fileRecordId);
    }
}
