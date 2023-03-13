package dev.siyah.filemanager.validation.file.size;

import dev.siyah.filemanager.validation.file.extension.FileExtensionValidation;
import dev.siyah.filemanager.validation.file.extension.FileExtensionValidator;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.validation.Payload;
import java.lang.annotation.Annotation;

import static org.testng.Assert.*;

@RunWith(SpringRunner.class)
public class FileSizeValidatorTest {
    private FileSizeValidator fileSizeValidator;

    @BeforeMethod
    public void setUp() {
        this.fileSizeValidator = new FileSizeValidator();
    }

    @Test
    public void testValidate_WhenNullFile() {
        this.fileSizeValidator.initialize(createInterfaceObject(10));

        assertTrue(this.fileSizeValidator.validate(null, null));
    }

    @Test
    public void testValidate_WhenUnlimited() {
        this.fileSizeValidator.initialize(createInterfaceObject(0));

        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "hello.jpg",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        assertTrue(this.fileSizeValidator.validate(multipartFile, null));
    }

    @Test
    public void testValidate_WhenUnderLimit() {
        this.fileSizeValidator.initialize(createInterfaceObject(1));

        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "hello.jpg",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        assertTrue(this.fileSizeValidator.validate(multipartFile, null));
    }

    @Test
    public void testValidate_WhenAboveLimit() {
        this.fileSizeValidator.initialize(createInterfaceObject(1));

        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "hello.jpg",
                MediaType.TEXT_PLAIN_VALUE,
                RandomStringUtils.randomGraph(300000).getBytes()
        );

        assertFalse(this.fileSizeValidator.validate(multipartFile, null));
    }

    private FileSizeValidation createInterfaceObject(long allowedMaximumFileSize){
        return new FileSizeValidation(){

            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String message() {
                return null;
            }

            @Override
            public long value() {
                return allowedMaximumFileSize;
            }

            @Override
            public String[] allowedExtensions() {
                return new String[0];
            }

            @Override
            public Class<?>[] groups() {
                return new Class[0];
            }

            @Override
            public Class<? extends Payload>[] payload() {
                return new Class[0];
            }
        };
    }
}