package dev.siyah.filemanager.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "file-record")
@Data
public class FileRecordProperties {
    private String path;
}

