package dev.siyah.filemanager.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "T_REFRESH_TOKEN")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {

    @Id
    @Type(type = "pg-uuid")
    private UUID id = UUID.randomUUID();

    @OneToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id", nullable = false)
    private Member member;

    @Column(unique = true, nullable = false)
    private String refreshToken;

    @Column(nullable = false)
    private Instant expDate;
}
