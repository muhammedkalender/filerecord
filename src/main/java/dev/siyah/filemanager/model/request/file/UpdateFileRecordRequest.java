package dev.siyah.filemanager.model.request.file;

import dev.siyah.filemanager.enums.FileExtension;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateFileRecordRequest {
    @Length(max = 128)
    private String name;

    private FileExtension extension;

    private MultipartFile file;
}