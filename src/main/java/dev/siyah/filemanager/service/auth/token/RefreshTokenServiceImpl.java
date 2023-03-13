package dev.siyah.filemanager.service.auth.token;

import dev.siyah.filemanager.entity.RefreshToken;
import dev.siyah.filemanager.properties.JWTProperties;
import dev.siyah.filemanager.repository.MemberRepository;
import dev.siyah.filemanager.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final JWTProperties jwtProperties;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;

    public RefreshToken findByRefreshToken(String token) {
        return refreshTokenRepository.findByRefreshToken(token);
    }

    public RefreshToken createRefreshToken(UUID memberId) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setMember(memberRepository.findById(memberId).get());
        refreshToken.setExpDate(Instant.now().plusMillis(jwtProperties.getRefreshExp()));
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        refreshToken = refreshTokenRepository.save(refreshToken);

        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            return null;
        }

        return token;
    }

    @Transactional
    public int deleteByMemberId(UUID memberId) {
        return refreshTokenRepository.deleteByMember(memberRepository.findById(memberId).get());
    }
}
