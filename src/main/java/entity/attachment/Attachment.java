package entity.attachment;

import entity.common.BaseEntity;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.Objects;

/**
 * Файл (фото, скан, документ). Хранится в файловом хранилище или S3, здесь — метаданные.
 * Полиморфная связь: ownerType + ownerId.
 */
@Entity
@Table(name = "attachments")

public class Attachment extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "owner_type", nullable = false, length = 50)
    private AttachmentOwnerType ownerType;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 50)
    private AttachmentType type;

    @Column(name = "file_name", length = 500, nullable = false)
    private String fileName;       // оригинальное имя

    @Column(name = "storage_key", length = 1000, nullable = false)
    private String storageKey;     // путь в хранилище / S3 key

    @Column(name = "mime_type", length = 200)
    private String mimeType;

    @Column(name = "size_bytes", nullable = false)
    private long sizeBytes;

    @Column(name = "sha256", length = 64)
    private String sha256;         // для проверки целостности

    @Column(name = "uploaded_by_user_id")
    private Long uploadedByUserId;

    @Column(name = "uploaded_at", nullable = false)
    private Instant uploadedAt;

    public Attachment(AttachmentOwnerType ownerType, Long ownerId, AttachmentType type,
                      String fileName, String storageKey, String mimeType,
                      long sizeBytes, Long uploadedByUserId) {
        if (ownerType == null) throw new IllegalArgumentException("ownerType обязателен");
        if (ownerId == null) throw new IllegalArgumentException("ownerId обязателен");
        if (type == null) throw new IllegalArgumentException("Тип вложения обязателен");
        if (fileName == null || fileName.isBlank()) throw new IllegalArgumentException("Имя файла обязательно");
        if (storageKey == null || storageKey.isBlank()) throw new IllegalArgumentException("Ключ хранилища обязателен");
        if (sizeBytes <= 0) throw new IllegalArgumentException("Размер файла должен быть > 0");
        this.ownerType = ownerType;
        this.ownerId = ownerId;
        this.type = type;
        this.fileName = fileName;
        this.storageKey = storageKey;
        this.mimeType = mimeType;
        this.sizeBytes = sizeBytes;
        this.uploadedByUserId = uploadedByUserId;
        this.uploadedAt = Instant.now();
    }

    public AttachmentOwnerType getOwnerType() { return ownerType; }
    public Long getOwnerId() { return ownerId; }
    public AttachmentType getType() { return type; }
    public String getFileName() { return fileName; }
    public String getStorageKey() { return storageKey; }
    public String getMimeType() { return mimeType; }
    public long getSizeBytes() { return sizeBytes; }
    public String getSha256() { return sha256; }
    public Long getUploadedByUserId() { return uploadedByUserId; }
    public Instant getUploadedAt() { return uploadedAt; }

    public void setSha256(String sha256) { this.sha256 = sha256; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attachment that = (Attachment) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
