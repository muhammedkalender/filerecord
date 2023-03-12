package dev.siyah.filemanager.validation.file.size;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Allowed file extensions (eg: png, jpeg, jpg, docx, pdf, xlsx) <br>
 * Default Value : png, jpeg, jpg, docx, pdf <br>
 *
 * @implNote In this project removed "xlsx" from default value for prevent "Redundant default parameter value assignment"
 * @implNote Used extension instead of mime because file extension may change freely
 * @implNote 1 MB = 1024 KB
 * @see <a href="https://www.baeldung.com/spring-mvc-custom-validator">Baeldung - Spring MVC Custom Validation</a>
 */
@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {FileSizeValidator.class})
public @interface FileSizeValidation {
    String message() default "This file exceeds the allowed file size";

    /**
     * Allowed max file size in MB (eg: 5 for 5MB)
     * Default Value : 4 (4 MB)
     * Use 0 for skip
     *
     * @implNote In this project default value set to 5
     */
    int value() default 5;

    /**
     * Allowed file extensions (eg: png, jpeg, jpg, docx, pdf, xlsx) <br>
     * Default Value : png, jpeg, jpg, docx, pdf <br>
     */
    String[] allowedExtensions() default {"png", "jpeg", "jpg", "docx", "pdf"};

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}