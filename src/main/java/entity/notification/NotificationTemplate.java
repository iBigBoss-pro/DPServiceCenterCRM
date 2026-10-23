package entity.notification;

import entity.common.BaseEntity;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "notification_templates", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"code", "version"})
})

public class NotificationTemplate extends BaseEntity {

    @Column(name = "code", length = 100, nullable = false)
    private String code;          // ORDER_READY_FOR_PICKUP, APPROVAL_REQUEST...

    @Column(name = "version", nullable = false)
    private int version;

    @Column(name = "language", length = 10, nullable = false)
    private String language;

    @Enumerated(EnumType.STRING)
    @Column(name = "channel", length = 50, nullable = false)
    private NotificationChannel channel;

    @Column(name = "subject", length = 500)
    private String subject;       // для email

    @Column(name = "body", columnDefinition = "TEXT", nullable = false)
    private String body;          // с плейсхолдерами

    @Column(name = "active", nullable = false)
    private boolean active;

    public NotificationTemplate(String code, int version, String language,
                                NotificationChannel channel, String subject, String body) {
        if (code == null || code.isBlank()) throw new IllegalArgumentException("Код обязателен");
        if (version <= 0) throw new IllegalArgumentException("Версия должна быть > 0");
        if (channel == null) throw new IllegalArgumentException("Канал обязателен");
        if (body == null || body.isBlank()) throw new IllegalArgumentException("Тело обязательно");
        this.code = code;
        this.version = version;
        this.language = language;
        this.channel = channel;
        this.subject = subject;
        this.body = body;
        this.active = true;
    }

    public String getCode() { return code; }
    public int getVersion() { return version; }
    public String getLanguage() { return language; }
    public NotificationChannel getChannel() { return channel; }
    public String getSubject() { return subject; }
    public String getBody() { return body; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationTemplate that = (NotificationTemplate) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
