package dev.siyah.filemanager.validation.file.extension;


import dev.siyah.filemanager.enums.FileExtension;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Allowed file extensions (eg: png, jpeg, jpg, docx, pdf, xlsx) <br>
 * Default Value : png, jpeg, jpg, docx, pdf <br>
 *
 * @implNote In this project default value set to {png, jpeg, jpg, docx, pdf, xlsx)}
 * @implNote Used extension instead of mime because file extension may change freely
 * @see <a href="https://www.baeldung.com/spring-mvc-custom-validator">Baeldung - Spring MVC Custom Validation</a>
 */
@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {FileExtensionValidator.class})
public @interface FileExtensionValidation {
    String message() default "The extension of this file is not allowed";

    /**
     * Allowed file extensions (eg: png, jpeg, jpg, docx, pdf, xlsx) <br>
     * Default Value : png, jpeg, jpg, docx, pdf <br>
     */
    FileExtension[] value() default {FileExtension.PNG, FileExtension.JPEG, FileExtension.JPG, FileExtension.DOCX, FileExtension.PDF, FileExtension.XLSX};

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
