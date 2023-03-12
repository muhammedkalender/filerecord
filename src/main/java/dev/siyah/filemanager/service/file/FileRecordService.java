package dev.siyah.filemanager.service.file;

import dev.siyah.filemanager.entity.FileRecord;
import dev.siyah.filemanager.model.request.file.CreateFileRecordRequest;
import dev.siyah.filemanager.model.request.file.UpdateFileRecordRequest;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface FileRecordService {
    List<FileRecord> list();

    FileRecord create(CreateFileRecordRequest createFileRecordRequest) throws IOException;

    FileRecord update(UUID fileRecordId, UpdateFileRecordRequest updateFileRecordRequest) throws EntityNotFoundException, IOException;

    FileRecord getById(UUID fileRecordId) throws EntityNotFoundException;

    void deleteById(UUID fileRecordId);

    byte[] downloadById(UUID fileRecordId) throws EntityNotFoundException, IOException;
}
