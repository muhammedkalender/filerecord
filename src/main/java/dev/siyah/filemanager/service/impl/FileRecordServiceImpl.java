package dev.siyah.filemanager.service.impl;

import dev.siyah.filemanager.entity.FileRecord;
import dev.siyah.filemanager.exception.FileDeleteException;
import dev.siyah.filemanager.exception.FileMoveException;
import dev.siyah.filemanager.exception.FileSaveException;
import dev.siyah.filemanager.model.request.file.CreateFileRecordRequest;
import dev.siyah.filemanager.model.request.file.UpdateFileRecordRequest;
import dev.siyah.filemanager.properties.FileRecordProperties;
import dev.siyah.filemanager.repository.FileRecordRepository;
import dev.siyah.filemanager.service.FileRecordService;
import dev.siyah.filemanager.utility.FileUtility;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileRecordServiceImpl implements FileRecordService {
    private final FileRecordProperties fileRecordProperties;
    private final FileRecordRepository fileRecordRepository;

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

        this.saveAsFile(fileRecord.getPath(), createFileRecordRequest.getFile());

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

            if (!FileUtility.deleteFileIfExists(this.addPrefixToPath(oldFilePath))) {
                throw new FileDeleteException();
            }

            this.saveAsFile(fileRecord.getPath(), updateFileRecordRequest.getFile());
        } else {
            if (!FileUtility.move(this.addPrefixToPath(oldFilePath), this.addPrefixToPath(fileRecord.getPath()))) {
                throw new FileMoveException();
            }
        }

        fileRecord = fileRecordRepository.save(fileRecord);

        FileUtility.deleteFileIfExists(oldFilePath);

        return fileRecord;
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

    private void saveAsFile(String path, MultipartFile file) throws IOException {
        File saveFile = new File(this.addPrefixToPath(path));

        if (!saveFile.getParentFile().exists()) {
            boolean createFolderResult = saveFile.mkdirs();

            if (!createFolderResult) {
                throw new FileSaveException();
            }
        }

        file.transferTo(saveFile);
    }

    private String addPrefixToPath(String filePath) {
        String slash = this.fileRecordProperties.getPath().endsWith("/") ? "" : "/";

        return this.fileRecordProperties.getPath() + slash + filePath;
    }

    private String getPathWithoutPrefix(FileRecord fileRecord) {
        return fileRecord.getId() + "/" + fileRecord.getName() + "." + fileRecord.getExtension().getExtension();
    }
}
