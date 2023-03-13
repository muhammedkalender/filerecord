package dev.siyah.filemanager.validation.file.extension;

import dev.siyah.filemanager.enums.FileExtension;
import dev.siyah.filemanager.utility.FileUtility;
import dev.siyah.filemanager.validation.ValidationAbstraction;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Locale;

@RequiredArgsConstructor
public class FileExtensionValidator extends ValidationAbstraction<FileExtensionValidation, MultipartFile> {
    private final FileUtility fileUtility;
    private FileExtension[] allowedExtensions = new FileExtension[]{};

    @Override
    public void initialize(FileExtensionValidation constraintAnnotation) {
        this.allowedExtensions = constraintAnnotation.value();

        if (constraintAnnotation.value().length == 0) {
            this.skipValidation = true;
        }
    }

    @Override
    public boolean validate(MultipartFile multipartFile, ConstraintValidatorContext context) {
        if (multipartFile == null) {
            return true;
        }

        String extension = this.fileUtility.getExtensionByFileName(multipartFile.getOriginalFilename())
                .toUpperCase(Locale.ROOT);

        FileExtension fileExtension;

        try {
            fileExtension = FileExtension.valueOf(extension);
        } catch (IllegalArgumentException ignored) {
            return false;
        }

        return checkFileExtension(fileExtension);
    }

    private boolean checkFileExtension(FileExtension extension) {
        if(this.allowedExtensions.length == 0){
            return true;
        }

        return Arrays.asList(this.allowedExtensions)
                .contains(extension);
    }
}
