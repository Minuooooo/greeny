package greeny.backend.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditEntity {
    @CreatedDate
    private String createdAt;
    @LastModifiedDate
    private String updatedAt;

    @PrePersist
    public void prePersist() {
        String format = formatDateTime();
        this.createdAt = format;
        this.updatedAt = format;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = formatDateTime();
    }

    private String formatDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}