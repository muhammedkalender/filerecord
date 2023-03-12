package dev.siyah.filemanager.controller;

import dev.siyah.filemanager.model.request.file.CreateFileRecordRequest;
import dev.siyah.filemanager.model.request.file.UpdateFileRecordRequest;
import dev.siyah.filemanager.model.response.file.FileRecordResponse;
import dev.siyah.filemanager.service.FileRecordService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/file-record")
@RequiredArgsConstructor
public class FileRecordController {
    private final ModelMapper modelMapper;
    private final FileRecordService fileRecordService;

    @GetMapping
    public List<FileRecordResponse> list() {
        throw new NotImplementedException();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public FileRecordResponse createFileRecord(@Validated CreateFileRecordRequest createFileRecordRequest) {
        throw new NotImplementedException();
    }

    @PutMapping(path = "{fileRecordId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public FileRecordResponse editFileRecord(@PathVariable UUID fileRecordId, @Validated UpdateFileRecordRequest updateFileRecordRequest) {
        throw new NotImplementedException();
    }

    @GetMapping("{fileRecordId}")
    public FileRecordResponse getFileRecordById(@PathVariable UUID fileRecordId) {
        throw new NotImplementedException();
    }

    @DeleteMapping("{fileRecordId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean deleteFileRecordById(@PathVariable UUID fileRecordId) {
        throw new NotImplementedException();
    }

    @GetMapping("{fileRecordId}/download")
    public byte[] downloadFileRecord(@PathVariable UUID fileRecordId) {
        throw new NotImplementedException();
    }
}
