package dev.siyah.filemanager.service.auth.member;

import dev.siyah.filemanager.entity.Member;
import dev.siyah.filemanager.model.auth.MemberDetail;
import dev.siyah.filemanager.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsernameIgnoreCase(username.toLowerCase());

        if(member == null)
            throw new UsernameNotFoundException("User Not Found");

        return MemberDetail.builder()
                .id(member.getId())
                .fullName(member.getFullName())
                .username(member.getUsername())
                .password(member.getPassword())
                .build();
    }
}
