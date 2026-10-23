package entity.common;

import java.time.Instant;

/**
 * Базовый класс для всех персистентных сущностей.
 * Поля заполняются на уровне репозитория/БД (в JPA — @MappedSuperclass, @PrePersist, @PreUpdate).
 */
public abstract class BaseEntity {

    protected Long id;
    protected Long serviceCenterId;
    protected Instant createdAt;
    protected Instant updatedAt;
    protected Instant deletedAt;   // null = активна
    protected long entityVersion;        // оптимистичная блокировка

    public Long getId() { return id; }
    public Long getServiceCenterId() { return serviceCenterId; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public Instant getDeletedAt() { return deletedAt; }
    public boolean isDeleted() { return deletedAt != null; }
    public long getEntityVersion() { return entityVersion; }

    protected void setServiceCenterId(Long serviceCenterId) {
        if (serviceCenterId == null || serviceCenterId <= 0) {
            throw new IllegalArgumentException("serviceCenterId обязателен и должен быть > 0");
        }
        this.serviceCenterId = serviceCenterId;
    }
}