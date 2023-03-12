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

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/file-record")
@RequiredArgsConstructor
public class FileRecordController {
    private final ModelMapper modelMapper;
    private final FileRecordService fileRecordService;

    @GetMapping
    public List<FileRecordResponse> list() {
        return this.fileRecordService.list().stream().map((record) -> this.modelMapper.map(record, FileRecordResponse.class)).collect(Collectors.toList());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public FileRecordResponse createFileRecord(@Validated CreateFileRecordRequest createFileRecordRequest) throws IOException {
        return this.modelMapper.map(this.fileRecordService.create(createFileRecordRequest), FileRecordResponse.class);
    }

    @PutMapping(path = "{fileRecordId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public FileRecordResponse updateFileRecord(@PathVariable UUID fileRecordId, @Validated UpdateFileRecordRequest updateFileRecordRequest) throws IOException {
        return this.modelMapper.map(this.fileRecordService.update(fileRecordId, updateFileRecordRequest), FileRecordResponse.class);
    }

    @GetMapping("{fileRecordId}")
    public FileRecordResponse getFileRecordById(@PathVariable UUID fileRecordId) {
        return this.modelMapper.map(this.fileRecordService.getById(fileRecordId),
                FileRecordResponse.class);
    }

    @DeleteMapping("{fileRecordId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFileRecordById(@PathVariable UUID fileRecordId) {
        this.fileRecordService.deleteById(fileRecordId);
    }

    @GetMapping("{fileRecordId}/download")
    public byte[] downloadFileRecord(@PathVariable UUID fileRecordId) throws IOException {
        return this.fileRecordService.downloadById(fileRecordId);
    }
}
