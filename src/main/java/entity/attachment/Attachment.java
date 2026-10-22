package main.java.entity.attachment;

import main.java.entity.common.BaseEntity;
import java.time.Instant;

/**
 * Файл (фото, скан, документ). Хранится в файловом хранилище или S3, здесь — метаданные.
 * Полиморфная связь: ownerType + ownerId.
 */
public class Attachment extends BaseEntity {

    private AttachmentOwnerType ownerType;
    private Long ownerId;

    private AttachmentType type;
    private String fileName;       // оригинальное имя
    private String storageKey;     // путь в хранилище / S3 key
    private String mimeType;
    private long sizeBytes;
    private String sha256;         // для проверки целостности

    private Long uploadedByUserId;
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
}