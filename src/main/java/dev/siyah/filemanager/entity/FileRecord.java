package dev.siyah.filemanager.entity;

import dev.siyah.filemanager.enums.FileExtension;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "T_FILE_RECORDS")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
@Where(clause = "deleted_at IS NULL")
@Builder
public class FileRecord {
    @Id
    @Type(type = "pg-uuid")
    private UUID id = UUID.randomUUID();

    @Column(nullable = false)
    private String name = null;

    @Enumerated(EnumType.STRING)
    private FileExtension extension = null;

    @Column(nullable = false)
    private long sizeInKB = 0L;

    @Column(nullable = false)
    private String path = null;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false, updatable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column
    private LocalDateTime deletedAt = null;
}
