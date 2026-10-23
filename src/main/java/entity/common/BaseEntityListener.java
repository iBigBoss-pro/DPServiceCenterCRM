package entity.common;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.Instant;

/**
 * JPA listener для автоматического заполнения createdAt/updatedAt.
 */
public class BaseEntityListener {

    @PrePersist
    public void onCreate(BaseEntity entity) {
        if (entity.getCreatedAt() == null) {
            entity.setCreatedAt(Instant.now());
        }
        entity.setUpdatedAt(Instant.now());
    }

    @PreUpdate
    public void onUpdate(BaseEntity entity) {
        entity.setUpdatedAt(Instant.now());
    }
}
