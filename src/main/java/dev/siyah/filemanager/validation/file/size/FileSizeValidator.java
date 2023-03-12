package dev.siyah.filemanager.validation.file.size;

import dev.siyah.filemanager.validation.ValidationAbstraction;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidatorContext;

public class FileSizeValidator extends ValidationAbstraction<FileSizeValidation, MultipartFile> {
    private int allowedMaxFileSize = 0;

    @Override
    public void initialize(FileSizeValidation constraintAnnotation) {
        this.allowedMaxFileSize = constraintAnnotation.value();

        if (this.allowedMaxFileSize == 0) {
            this.skipValidation = true;
        }
    }

    @Override
    public boolean validate(MultipartFile payload, ConstraintValidatorContext constraintValidatorContext) {
        if (payload == null) {
            return true;
        }

        return payload.getSize() / 1024 >= this.allowedMaxFileSize;
    }
}

