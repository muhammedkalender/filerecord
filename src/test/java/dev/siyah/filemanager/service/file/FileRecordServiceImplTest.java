package dev.siyah.filemanager.service.file;

import dev.siyah.filemanager.entity.FileRecord;
import dev.siyah.filemanager.enums.FileExtension;
import dev.siyah.filemanager.model.request.file.CreateFileRecordRequest;
import dev.siyah.filemanager.model.request.file.UpdateFileRecordRequest;
import dev.siyah.filemanager.properties.FileRecordProperties;
import dev.siyah.filemanager.repository.FileRecordRepository;
import dev.siyah.filemanager.utility.FileUtility;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertThrows;
import static org.testng.AssertJUnit.assertEquals;

public class FileRecordServiceImplTest {
    @Mock
    private FileRecordRepository fileRecordRepository;
    private FileRecord actualFileRecord;
    @Mock
    private FileUtility fileUtility;
    private FileRecordProperties fileRecordProperties;
    private FileRecordServiceImpl fileRecordService;
    private MockMultipartFile multipartFile;

    @BeforeMethod
    public void setUp() {
        this.actualFileRecord = null;
        this.fileRecordRepository = mock(FileRecordRepository.class);
        this.fileUtility = mock(FileUtility.class);
        this.fileRecordProperties = FileRecordProperties.builder().path("no-path").build();
        this.fileRecordService = new FileRecordServiceImpl(this.fileRecordProperties, fileRecordRepository, fileUtility);

        this.multipartFile = new MockMultipartFile(
                "file",
                "hello.jpg",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
    }

    @Test
    public void testList_WhenNull() {
        when(fileRecordRepository.findAll())
                .thenReturn(new ArrayList<>());

        assertEquals(this.fileRecordService.list().size(), 0);
    }

    @Test
    public void testList_WhenFilled() {
        FileRecord exampleRecord = FileRecord.builder().id(UUID.randomUUID()).build();

        when(fileRecordRepository.findAll())
                .thenReturn(Collections.singletonList(exampleRecord));

        assertEquals(this.fileRecordService.list().size(), 1);
        assertEquals(this.fileRecordService.list().stream().findFirst().get().getId(), exampleRecord.getId());
    }

    @Test
    public void testCreate() throws IOException {
        CreateFileRecordRequest createFileRecordRequest = new CreateFileRecordRequest();
        createFileRecordRequest.setFile(this.multipartFile);
        createFileRecordRequest.setName("test");
        createFileRecordRequest.setExtension(FileExtension.XLSX);

        FileRecord excepted = new FileRecord();
        excepted.setId(UUID.randomUUID());
        excepted.setName(createFileRecordRequest.getName());
        excepted.setExtension(createFileRecordRequest.getExtension());
        excepted.setSizeInKB(createFileRecordRequest.getFile().getSize());
        excepted.setPath(this.getPathWithoutPrefix(excepted));

        when(this.fileRecordRepository.save(any(FileRecord.class)))
                .thenReturn(excepted);

        FileRecord actual = this.fileRecordService.create(createFileRecordRequest);
        actual.setId(excepted.getId());

        verify(this.fileUtility, times(1)).saveMultipartAsFile(any(), any());
        assertEquals(actual, excepted);
    }

    @Test
    public void testUpdate_WhenWrongId() {
        UUID uuid = UUID.randomUUID();

        when(this.fileRecordRepository.findById(uuid))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                this.fileRecordService.update(uuid, null)
        );
    }

    @Test
    public void testUpdate_WhenNullFile() throws IOException {
        FileRecord fileRecord = FileRecord.builder()
                .id(UUID.randomUUID())
                .name("test")
                .extension(FileExtension.XLSX)
                .build();
        fileRecord.setPath(getPathWithoutPrefix(fileRecord));

        String oldFilePath = fileRecord.getPath();

        when(this.fileUtility.move(anyString(), anyString())).thenReturn(true);

        UpdateFileRecordRequest updateFileRecordRequest = new UpdateFileRecordRequest();
        updateFileRecordRequest.setFile(null);
        updateFileRecordRequest.setName("west");
        updateFileRecordRequest.setExtension(FileExtension.XLSX);

        when(this.fileRecordRepository.findById(fileRecord.getId()))
                .thenReturn(Optional.of(fileRecord));
        when(this.fileRecordRepository.save(any())).then(i -> this.actualFileRecord = (FileRecord) i.getArguments()[0]);

        this.fileRecordService.update(fileRecord.getId(), updateFileRecordRequest);

        verify(this.fileUtility, times(1)).move(this.addPrefixToPath(oldFilePath), this.addPrefixToPath(this.actualFileRecord.getPath()));
        verify(this.fileUtility, times(1)).deleteFileIfExists(any());
        verify(this.fileUtility, never()).saveMultipartAsFile(any(), any());

        assertEquals(this.actualFileRecord.getName(), updateFileRecordRequest.getName());
        assertEquals(this.actualFileRecord.getExtension(), updateFileRecordRequest.getExtension());
        assertEquals(this.actualFileRecord.getPath(), fileRecord.getPath());
        assertNotEquals(this.actualFileRecord.getSizeInKB(), 0);
    }

    @Test
    public void testUpdate_WhenFile() throws IOException {
        UUID id = UUID.randomUUID();

        when(this.fileRecordRepository.findById(id)).thenReturn(Optional.of(FileRecord.builder().id(id).build()));
        when(this.fileUtility.move(anyString(), anyString())).thenReturn(true);
        when(this.fileUtility.deleteFileIfExists(anyString())).thenReturn(true);
        when(this.fileRecordRepository.save(any())).then(i -> this.actualFileRecord = (FileRecord) i.getArguments()[0]);

        UpdateFileRecordRequest updateFileRecordRequest = new UpdateFileRecordRequest();
        updateFileRecordRequest.setFile(new MockMultipartFile(
                "file",
                "hello.jpg",
                MediaType.TEXT_PLAIN_VALUE,
                "123456".getBytes()
        ));
        updateFileRecordRequest.setName("test");
        updateFileRecordRequest.setExtension(FileExtension.XLSX);

        this.fileRecordService.update(id, updateFileRecordRequest);

        FileRecord compareObject = FileRecord.builder()
                .id(this.actualFileRecord.getId())
                .name(updateFileRecordRequest.getName())
                .extension(updateFileRecordRequest.getExtension())
                .build();
        compareObject.setPath(this.getPathWithoutPrefix(compareObject));

        verify(this.fileUtility, never()).move(this.addPrefixToPath("null"), this.addPrefixToPath(compareObject.getPath()));
        verify(this.fileUtility, atLeastOnce()).deleteFileIfExists(any());
        verify(this.fileUtility, atLeastOnce()).saveMultipartAsFile(any(), any());

        assertEquals(this.actualFileRecord.getName(), updateFileRecordRequest.getName());
        assertEquals(this.actualFileRecord.getExtension(), updateFileRecordRequest.getExtension());
        assertEquals(this.actualFileRecord.getPath(), compareObject.getPath());
        assertEquals(this.actualFileRecord.getSizeInKB(), 6);
    }

    @Test
    public void testGetById() {
        FileRecord fileRecord = new FileRecord();

        when(this.fileRecordRepository.findById(any())).thenReturn(Optional.of(fileRecord));

        assertEquals(this.fileRecordService.getById(UUID.randomUUID()), fileRecord);
    }

    @Test
    public void testDeleteById() {
        UUID id = UUID.randomUUID();
        FileRecord fileRecord = FileRecord.builder()
                .id(id)
                .build();

        when(this.fileRecordRepository.findById(id)).thenReturn(Optional.of(fileRecord));
        when(this.fileRecordRepository.save(any())).then(i -> actualFileRecord = (FileRecord) Arrays.stream(i.getArguments()).findFirst().get());
        when(this.fileUtility.deleteFileParentIfExists(any())).thenReturn(true);

        this.fileRecordService.deleteById(id);

        verify(this.fileUtility, times(1)).deleteFileParentIfExists(any());

        assertNotEquals(this.actualFileRecord.getDeletedAt(), null);
    }

    @Test
    public void testDownloadById() throws IOException {
        UUID id = UUID.randomUUID();
        FileRecord fileRecord = new FileRecord();
        fileRecord.setPath("test.png");

        byte[] excepted = new byte[]{1, 2, 3, 4, 5, 6};

        when(this.fileRecordRepository.findById(id)).thenReturn(Optional.of(fileRecord));
        when(this.fileUtility.readByPath(this.addPrefixToPath(fileRecord.getPath()))).thenReturn(excepted);

        assertEquals(this.fileRecordService.downloadById(id), excepted);
    }

    @Test
    public void testDownloadById_WhenWrongPath() throws IOException {
        UUID id = UUID.randomUUID();
        FileRecord fileRecord = new FileRecord();
        fileRecord.setPath("test.png");

        when(this.fileRecordRepository.findById(id)).thenReturn(Optional.of(fileRecord));
        when(this.fileUtility.readByPath(this.addPrefixToPath(fileRecord.getPath()))).thenReturn(null);

        Assert.assertNull(this.fileRecordService.downloadById(id));
    }

    @Test
    public void testDownloadById_WhenWrongId() {
        assertThrows(EntityNotFoundException.class, () -> this.fileRecordService.downloadById(UUID.randomUUID()));
    }

    private String addPrefixToPath(String filePath) {
        String slash = this.fileRecordProperties.getPath().endsWith("/") ? "" : "/";

        return this.fileRecordProperties.getPath() + slash + filePath;
    }

    private String getPathWithoutPrefix(FileRecord fileRecord) {
        return fileRecord.getId() + "/" + fileRecord.getName() + "." + fileRecord.getExtension().getExtension();
    }
}