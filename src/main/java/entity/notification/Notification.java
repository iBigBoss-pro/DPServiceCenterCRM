package entity.notification;

import entity.common.BaseEntity;
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
        if (templateVersion < 0) throw new IllegalArgumentException("Версия шаблона не может быть отрицательной");
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

    public void markSent() {
        requireStatus(NotificationStatus.PENDING, "Отправить можно только ожидающее уведомление");
        this.status = NotificationStatus.SENT;
        this.sentAt = Instant.now();
        this.errorMessage = null;
    }

    public void markDelivered() {
        requireStatus(NotificationStatus.SENT, "Доставить можно только отправленное уведомление");
        this.status = NotificationStatus.DELIVERED;
        this.deliveredAt = Instant.now();
    }

    public void markRead() {
        requireStatus(NotificationStatus.DELIVERED, "Прочитать можно только доставленное уведомление");
        this.status = NotificationStatus.READ;
        this.readAt = Instant.now();
    }

    public void markFailed(String error) {
        if (status != NotificationStatus.PENDING && status != NotificationStatus.SENT) {
            throw new IllegalStateException("Ошибка допустима только для ожидающего или отправленного уведомления");
        }
        if (error == null || error.isBlank()) throw new IllegalArgumentException("Причина ошибки обязательна");
        this.status = NotificationStatus.FAILED;
        this.errorMessage = error;
    }

    private void requireStatus(NotificationStatus expected, String message) {
        if (status != expected) throw new IllegalStateException(message + ". Текущий статус: " + status);
    }

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