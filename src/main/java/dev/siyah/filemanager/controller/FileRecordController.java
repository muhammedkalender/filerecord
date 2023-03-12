package dev.siyah.filemanager.controller;

import dev.siyah.filemanager.service.FileRecordService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/file-record")
@RequiredArgsConstructor
public class FileRecordController {
    private final ModelMapper modelMapper;
    private final FileRecordService fileRecordService;
}
