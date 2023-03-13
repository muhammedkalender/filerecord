package dev.siyah.filemanager.service.file;

import dev.siyah.filemanager.entity.FileRecord;
import dev.siyah.filemanager.exception.FileDeleteException;
import dev.siyah.filemanager.exception.FileMoveException;
import dev.siyah.filemanager.model.request.file.CreateFileRecordRequest;
import dev.siyah.filemanager.model.request.file.UpdateFileRecordRequest;
import dev.siyah.filemanager.properties.FileRecordProperties;
import dev.siyah.filemanager.repository.FileRecordRepository;
import dev.siyah.filemanager.utility.FileUtility;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileRecordServiceImpl implements FileRecordService {
    private final FileRecordProperties fileRecordProperties;
    private final FileRecordRepository fileRecordRepository;
    private final FileUtility fileUtility;

    @Override
    public List<FileRecord> list() {
        return fileRecordRepository.findAll();
    }

    @Override
    public FileRecord create(CreateFileRecordRequest createFileRecordRequest) throws IOException {
        FileRecord fileRecord = new FileRecord();
        fileRecord.setId(UUID.randomUUID());
        fileRecord.setName(createFileRecordRequest.getName());
        fileRecord.setExtension(createFileRecordRequest.getExtension());
        fileRecord.setSizeInKB(createFileRecordRequest.getFile().getSize());
        fileRecord.setPath(this.getPathWithoutPrefix(fileRecord));

        this.fileUtility.saveMultipartAsFile(this.addPrefixToPath(fileRecord.getPath()), createFileRecordRequest.getFile());

        return fileRecordRepository.save(fileRecord);
    }

    @Override
    public FileRecord update(UUID fileRecordId, UpdateFileRecordRequest updateFileRecordRequest) throws EntityNotFoundException, IOException {
        FileRecord fileRecord = this.getById(fileRecordId);

        String oldFilePath = fileRecord.getPath();

        if (StringUtils.isNotEmpty(updateFileRecordRequest.getName())) {
            fileRecord.setName(updateFileRecordRequest.getName());
        }

        if (updateFileRecordRequest.getExtension() != null) {
            fileRecord.setExtension(updateFileRecordRequest.getExtension());
        }

        fileRecord.setPath(this.getPathWithoutPrefix(fileRecord));

        if (updateFileRecordRequest.getFile() != null) {
            fileRecord.setSizeInKB(updateFileRecordRequest.getFile().getSize());

            if (!this.fileUtility.deleteFileIfExists(this.addPrefixToPath(oldFilePath))) {
                throw new FileDeleteException();
            }

            this.fileUtility.saveMultipartAsFile(this.addPrefixToPath(fileRecord.getPath()), updateFileRecordRequest.getFile());
        } else {
            if (!this.fileUtility.move(this.addPrefixToPath(oldFilePath), this.addPrefixToPath(fileRecord.getPath()))) {
                throw new FileMoveException();
            }
        }

        fileRecord = fileRecordRepository.save(fileRecord);

        this.fileUtility.deleteFileIfExists(oldFilePath);

        return fileRecord;
    }

    @Override
    public FileRecord getById(UUID fileRecordId) throws EntityNotFoundException {
        return fileRecordRepository.findById(fileRecordId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public void deleteById(UUID fileRecordId) {
        FileRecord fileRecord = this.getById(fileRecordId);

        if (fileRecord != null) {
            if (!this.fileUtility.deleteFileParentIfExists(this.addPrefixToPath(fileRecord.getPath()))) {
                throw new FileDeleteException();
            }

            fileRecord.setDeletedAt(LocalDateTime.now());
            this.fileRecordRepository.save(fileRecord);
        }
    }

    @Override
    public byte[] downloadById(UUID fileRecordId) throws EntityNotFoundException, IOException {
        FileRecord fileRecord = this.getById(fileRecordId);

        return this.fileUtility.readByPath(this.addPrefixToPath(fileRecord.getPath()));
    }

    private String addPrefixToPath(String filePath) {
        String slash = this.fileRecordProperties.getPath().endsWith("/") ? "" : "/";

        return this.fileRecordProperties.getPath() + slash + filePath;
    }

    private String getPathWithoutPrefix(FileRecord fileRecord) {
        return fileRecord.getId() + "/" + fileRecord.getName() + "." + fileRecord.getExtension().getExtension();
    }
}
