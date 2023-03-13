package dev.siyah.filemanager.service.auth.member;

import dev.siyah.filemanager.entity.Member;
import dev.siyah.filemanager.repository.MemberRepository;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

public class MemberServiceImplTest {
    @Mock
    private MemberRepository memberRepository;

    private MemberServiceImpl memberService;

    @BeforeMethod
    public void setUp() {
        this.memberRepository = mock(MemberRepository.class);
        this.memberService = new MemberServiceImpl(this.memberRepository);
    }

    @Test
    public void testLoadUserByUsername_WhenWrongUsername() {
        assertThrows(UsernameNotFoundException.class, () -> {
            this.memberService.loadUserByUsername("test");
        });
    }

    @Test
    public void testLoadUserByUsername_WhenCorrectUsername() {
        Member member = Member.builder()
                .id(UUID.randomUUID())
                .username("test")
                .fullName("muhammed kalender")
                .password("1234567")
                .build();

        when(this.memberRepository.findByUsernameIgnoreCase(member.getUsername())).thenReturn(member);

        UserDetails userDetails = this.memberService.loadUserByUsername(member.getUsername());

        assertEquals(userDetails.getUsername(), member.getUsername());
        assertEquals(userDetails.getPassword(), member.getPassword());
    }
}