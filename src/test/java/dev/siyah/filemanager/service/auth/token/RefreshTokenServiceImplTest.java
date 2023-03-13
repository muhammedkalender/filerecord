package dev.siyah.filemanager.service.auth.token;

import dev.siyah.filemanager.entity.Member;
import dev.siyah.filemanager.entity.RefreshToken;
import dev.siyah.filemanager.properties.JWTProperties;
import dev.siyah.filemanager.repository.MemberRepository;
import dev.siyah.filemanager.repository.RefreshTokenRepository;
import org.mockito.Mock;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class RefreshTokenServiceImplTest {

    private JWTProperties jwtProperties;
    private RefreshTokenServiceImpl refreshTokenService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    private RefreshToken exceptedRefreshToken;

    @BeforeTest
    public void setUp() {
        this.jwtProperties = JWTProperties.builder().refreshExp(1000L).build();
        this.refreshTokenRepository = mock(RefreshTokenRepository.class);
        this.memberRepository = mock(MemberRepository.class);
        this.refreshTokenService = new RefreshTokenServiceImpl(this.jwtProperties, this.refreshTokenRepository, memberRepository);
    }

    @Test
    public void testFindByRefreshToken() {
        RefreshToken refreshToken = RefreshToken.builder()
                .id(UUID.randomUUID())
                .build();

        when(this.refreshTokenRepository.findByRefreshToken("test"))
                .thenReturn(refreshToken);

        RefreshToken actual = this.refreshTokenService.findByRefreshToken("test");

        assertSame(actual, refreshToken);
    }

    @Test
    public void testCreateRefreshToken() {
        Member member = Member.builder()
                .id(UUID.randomUUID())
                .build();

        when(this.memberRepository.findById(member.getId()))
                .thenReturn(Optional.of(member));

        when(this.refreshTokenRepository.save(any())).then((i) -> this.exceptedRefreshToken = (RefreshToken) i.getArguments()[0]);

        RefreshToken actual = this.refreshTokenService.createRefreshToken(member.getId());
        assertSame(actual, this.exceptedRefreshToken);
    }

    @Test
    public void testVerifyExpiration_WhenValid() {
        RefreshToken excepted = RefreshToken.builder()
                .expDate(
                        Instant.now()
                                .plusSeconds(1000)
                )
                .build();

        RefreshToken actual = this.refreshTokenService.verifyExpiration(excepted);
        assertSame(actual, excepted);
    }

    @Test
    public void testVerifyExpiration_WhenExpired() {
        RefreshToken refreshToken = RefreshToken.builder()
                .expDate(
                        Instant.MIN
                )
                .build();

        assertNull(this.refreshTokenService.verifyExpiration(refreshToken));
        verify(refreshTokenRepository, times(1)).delete(refreshToken);
    }

    @Test
    public void testDeleteByMemberId() {
        Member member = Member.builder()
                .id(UUID.randomUUID())
                .build();

        when(this.memberRepository.findById(member.getId()))
                .thenReturn(Optional.of(member));
        when(this.refreshTokenRepository.deleteByMember(member))
                .thenReturn(1);

        assertEquals(this.refreshTokenService.deleteByMemberId(member.getId()), 1);
        verify(this.refreshTokenRepository, times(1)).deleteByMember(member);
    }
}