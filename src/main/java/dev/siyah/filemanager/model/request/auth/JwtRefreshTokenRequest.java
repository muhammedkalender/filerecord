package dev.siyah.filemanager.model.request.auth;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtRefreshTokenRequest {
    @NotNull
    private String refreshToken;
}
