package dev.siyah.filemanager.service.impl;

import dev.siyah.filemanager.entity.FileRecord;
import dev.siyah.filemanager.model.request.file.CreateFileRecordRequest;
import dev.siyah.filemanager.model.request.file.UpdateFileRecordRequest;
import dev.siyah.filemanager.service.FileRecordService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@Service
public class FileRecordServiceImpl implements FileRecordService {
    @Override
    public List<FileRecord> list() {
        return null;
    }

    @Override
    public FileRecord create(CreateFileRecordRequest createFileRecordRequest) {
        return null;
    }

    @Override
    public FileRecord update(UUID fileRecordId, UpdateFileRecordRequest updateFileRecordRequest) throws EntityNotFoundException {
        return null;
    }

    @Override
    public FileRecord getById(UUID fileRecordId) throws EntityNotFoundException {
        return null;
    }

    @Override
    public void deleteById(UUID fileRecordId) {

    }

    @Override
    public byte[] downloadById(UUID fileRecordId) throws EntityNotFoundException {
        return new byte[0];
    }
}
