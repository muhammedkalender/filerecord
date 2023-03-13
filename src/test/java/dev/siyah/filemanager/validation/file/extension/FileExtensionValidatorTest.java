package dev.siyah.filemanager.validation.file.extension;

import dev.siyah.filemanager.enums.FileExtension;
import dev.siyah.filemanager.utility.FileUtility;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.validation.Payload;

import java.lang.annotation.Annotation;


@RunWith(SpringRunner.class)
@Test
public class FileExtensionValidatorTest {
    private FileExtensionValidator fileExtensionValidator;
    private MockMultipartFile multipartFile;
    private FileUtility fileUtility;

    @BeforeTest
    public void setUp() {
        this.fileUtility = new FileUtility();
        this.fileExtensionValidator = new FileExtensionValidator(this.fileUtility);

        this.multipartFile = new MockMultipartFile(
                "file",
                "hello.jpg",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
    }

    @Test
    public void testValidate_WhenEmptyList() {
        this.fileExtensionValidator.initialize(this.createInterfaceObject(new FileExtension[]{}));


        boolean actual = this.fileExtensionValidator.validate(this.multipartFile, null);
        Assert.assertTrue(actual);
    }

    @Test
    public void testValidate_WhenInvalidExtension() {
        this.fileExtensionValidator.initialize(this.createInterfaceObject(new FileExtension[]{FileExtension.JPEG}));

        boolean actual = this.fileExtensionValidator.validate(this.multipartFile, null);
        Assert.assertFalse(actual);
    }

    @Test
    public void testValidate_WhenValidExtension() {
        this.fileExtensionValidator.initialize(this.createInterfaceObject(new FileExtension[]{FileExtension.JPG, FileExtension.XLSX}));

        boolean actual = this.fileExtensionValidator.validate(this.multipartFile, null);
        Assert.assertTrue(actual);
    }

    @Test
    public void testValidate_WhenNullFile() {
        FileExtensionValidator validator = new FileExtensionValidator(this.fileUtility);
        validator.initialize(this.createInterfaceObject(new FileExtension[]{FileExtension.JPG, FileExtension.XLSX}));

        boolean actual = validator.validate(null, null);
        Assert.assertTrue(actual);
    }

    private FileExtensionValidation createInterfaceObject(FileExtension[] fileExtensions) {
        return new FileExtensionValidation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String message() {
                return null;
            }

            @Override
            public FileExtension[] value() {
                return fileExtensions;
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