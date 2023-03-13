package dev.siyah.filemanager.repository;

import dev.siyah.filemanager.entity.Member;
import dev.siyah.filemanager.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    RefreshToken findByRefreshToken(String token);

    @Modifying
    int deleteByMember(Member member);

}
