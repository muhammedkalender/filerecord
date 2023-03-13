package dev.siyah.filemanager.model.response.file;

import dev.siyah.filemanager.enums.FileExtension;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class FileRecordResponse {
    private UUID id;
    private String name;
    private FileExtension extension;
    private String path;
    private long fileSizeInKB;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
