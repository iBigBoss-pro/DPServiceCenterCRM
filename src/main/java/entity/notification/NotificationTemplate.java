package main.java.entity.notification;

import main.java.entity.common.BaseEntity;

public class NotificationTemplate extends BaseEntity {

    private String code;          // ORDER_READY_FOR_PICKUP, APPROVAL_REQUEST...
    private int version;
    private String language;
    private NotificationChannel channel;
    private String subject;       // для email
    private String body;          // с плейсхолдерами
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
}