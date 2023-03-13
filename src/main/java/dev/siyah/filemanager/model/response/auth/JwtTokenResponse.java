package dev.siyah.filemanager.model.response.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtTokenResponse {
    private String type;
    private String token;
    private String refreshToken;
    private UUID memberId;
    private String fullName;
    private String username;
}
