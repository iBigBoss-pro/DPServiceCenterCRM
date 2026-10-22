package main.java.entity.notification;

import main.java.entity.common.BaseEntity;
import java.time.Instant;

public class Notification extends BaseEntity {

    private Long clientId;
    private Long orderId;
    private Long templateId;
    private int templateVersion;

    private NotificationChannel channel;
    private NotificationStatus status;

    private String recipient;        // phone / email / chatId на момент отправки
    private String subject;
    private String body;             // снимок текста

    private Instant sentAt;
    private Instant deliveredAt;
    private Instant readAt;
    private String errorMessage;

    private Long triggeredByUserId; // null = система

    public Notification(Long clientId, Long orderId, Long templateId, int templateVersion,
                        NotificationChannel channel, String recipient,
                        String subject, String body) {
        if (clientId == null) throw new IllegalArgumentException("clientId обязателен");
        if (channel == null) throw new IllegalArgumentException("Канал обязателен");
        if (recipient == null || recipient.isBlank()) throw new IllegalArgumentException("Получатель обязателен");
        if (body == null || body.isBlank()) throw new IllegalArgumentException("Текст уведомления обязателен");
        this.clientId = clientId;
        this.orderId = orderId;
        this.templateId = templateId;
        this.templateVersion = templateVersion;
        this.channel = channel;
        this.recipient = recipient;
        this.subject = subject;
        this.body = body;
        this.status = NotificationStatus.PENDING;
    }

    public void markSent() { this.status = NotificationStatus.SENT; this.sentAt = Instant.now(); }
    public void markDelivered() { this.status = NotificationStatus.DELIVERED; this.deliveredAt = Instant.now(); }
    public void markRead() { this.status = NotificationStatus.READ; this.readAt = Instant.now(); }
    public void markFailed(String error) { this.status = NotificationStatus.FAILED; this.errorMessage = error; }

    public Long getClientId() { return clientId; }
    public Long getOrderId() { return orderId; }
    public Long getTemplateId() { return templateId; }
    public int getTemplateVersion() { return templateVersion; }
    public NotificationChannel getChannel() { return channel; }
    public NotificationStatus getStatus() { return status; }
    public String getRecipient() { return recipient; }
    public String getSubject() { return subject; }
    public String getBody() { return body; }
    public Instant getSentAt() { return sentAt; }
    public Instant getDeliveredAt() { return deliveredAt; }
    public Instant getReadAt() { return readAt; }
    public String getErrorMessage() { return errorMessage; }
    public Long getTriggeredByUserId() { return triggeredByUserId; }

    public void setTriggeredByUserId(Long v) { this.triggeredByUserId = v; }
}