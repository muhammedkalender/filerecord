package dev.siyah.filemanager.configuration;

import dev.siyah.filemanager.model.response.common.ExceptionResponse;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class OpenAPIConfiguration {
    @Bean
    public OpenAPI springOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info().title("File Record API")
                                .description("Manage files via API")
                                .version("v1.0.0")
                )
                .components(new Components().addSecuritySchemes(
                        "bearer-key",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP)
                                .name("Bearer Authentication")
                                .scheme("bearer")
                                .bearerFormat("JWT"))
                );
    }
}
