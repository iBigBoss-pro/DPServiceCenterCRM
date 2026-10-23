package entity.common;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * Базовый класс для всех персистентных сущностей.
 * Поля заполняются на уровне репозитория/БД (в JPA — @MappedSuperclass, @PrePersist, @PreUpdate).
 */
@MappedSuperclass
@EntityListeners(BaseEntityListener.class)
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "service_center_id", nullable = false)
    protected Long serviceCenterId;

    @Column(name = "created_at", nullable = false, updatable = false)
    protected Instant createdAt;

    @Column(name = "updated_at")
    protected Instant updatedAt;

    @Column(name = "deleted_at")
    protected Instant deletedAt;

    @Version
    @Column(name = "entity_version")
    protected long entityVersion;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getServiceCenterId() { return serviceCenterId; }
    public void setServiceCenterId(Long serviceCenterId) {
        if (serviceCenterId == null || serviceCenterId <= 0) {
            throw new IllegalArgumentException("serviceCenterId обязателен и должен быть > 0");
        }
        this.serviceCenterId = serviceCenterId;
    }

    public Instant getCreatedAt() { return createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public Instant getDeletedAt() { return deletedAt; }
    public void setDeletedAt(Instant deletedAt) { this.deletedAt = deletedAt; }

    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public boolean isDeleted() { return deletedAt != null; }

    public long getEntityVersion() { return entityVersion; }
    public void setEntityVersion(long entityVersion) { this.entityVersion = entityVersion; }

    public void setServiceCenterIdForTest(Long serviceCenterId) {
        this.serviceCenterId = serviceCenterId;
    }
}
