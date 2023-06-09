package dev.siyah.filemanager.repository;

import dev.siyah.filemanager.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {

    Member findByUsernameIgnoreCase(String username);

}
