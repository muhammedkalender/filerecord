package dev.siyah.filemanager.service;

import dev.siyah.filemanager.entity.FileRecord;
import dev.siyah.filemanager.model.request.file.CreateFileRecordRequest;
import dev.siyah.filemanager.model.request.file.UpdateFileRecordRequest;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

public interface FileRecordService {
    List<FileRecord> list();

    FileRecord create(CreateFileRecordRequest createFileRecordRequest);

    FileRecord update(UUID fileRecordId, UpdateFileRecordRequest updateFileRecordRequest) throws EntityNotFoundException;

    FileRecord getById(UUID fileRecordId) throws EntityNotFoundException;

    void deleteById(UUID fileRecordId);

    byte[] downloadById(UUID fileRecordId) throws EntityNotFoundException;
}
