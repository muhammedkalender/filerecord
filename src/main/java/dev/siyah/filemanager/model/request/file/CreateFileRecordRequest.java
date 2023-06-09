package dev.siyah.filemanager.model.request.file;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.siyah.filemanager.enums.FileExtension;
import dev.siyah.filemanager.validation.file.extension.FileExtensionValidation;
import dev.siyah.filemanager.validation.file.size.FileSizeValidation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateFileRecordRequest {
    @NotNull
    @NotEmpty
    @Length(max = 128)
    private String name;

    @NotNull
    private FileExtension extension;

    @NotNull
    @FileSizeValidation
    @FileExtensionValidation
    private MultipartFile file = null;
}
