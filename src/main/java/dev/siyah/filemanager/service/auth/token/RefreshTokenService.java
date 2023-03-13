package dev.siyah.filemanager.service.auth.token;

import dev.siyah.filemanager.entity.RefreshToken;

import java.util.UUID;

public interface RefreshTokenService {
    RefreshToken findByRefreshToken(String token);
    RefreshToken createRefreshToken(UUID memberId);
    RefreshToken verifyExpiration(RefreshToken token);
    int deleteByMemberId(UUID memberId);
}
