package dev.siyah.filemanager.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JWTProperties {
    private String secret;
    private Long jwtExp;
    private Long refreshExp;
}
