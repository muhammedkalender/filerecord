package dev.siyah.filemanager.entity;

import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "T_MEMBER")
@Data
@Builder
public class Member {
    @Id
    @Type(type = "pg-uuid")
    private UUID id = UUID.randomUUID();

    @Column(nullable = false, length = 32)
    private String fullName;

    @Column(nullable = false, length = 32, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;
}
