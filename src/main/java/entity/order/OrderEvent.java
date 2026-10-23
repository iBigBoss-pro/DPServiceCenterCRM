package entity.order;

import java.time.Instant;

/**
 * Событие смены статуса. Создаётся внутри Order.changeStatus.
 * id появится в JPA (@GeneratedValue); здесь — null до сохранения.
 */
public class OrderEvent {

    private Long id;
    private final Instant timestamp;
    private final OrderStatus oldStatus;
    private final OrderStatus newStatus;
    private final Long userId;     // кто сменил
    private final String statusComment;
    private Long notificationId;   // ссылка на уведомление, если было

    public OrderEvent(OrderStatus oldStatus, OrderStatus newStatus, Long userId, String comment) {
        if (oldStatus == null) throw new IllegalArgumentException("Предыдущий статус обязателен");
        if (newStatus == null) throw new IllegalArgumentException("Новый статус обязателен");
        if (userId == null) throw new IllegalArgumentException("Пользователь обязателен");
        if (oldStatus == newStatus) throw new IllegalArgumentException("Статусы совпадают: " + newStatus);
        this.timestamp = Instant.now();
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.userId = userId;
        this.statusComment = comment;
    }

    public Long getId() { return id; }
    public Instant getTimestamp() { return timestamp; }
    public OrderStatus getOldStatus() { return oldStatus; }
    public OrderStatus getNewStatus() { return newStatus; }
    public Long getUserId() { return userId; }
    public String getStatusComment() { return statusComment; }
    public Long getNotificationId() { return notificationId; }

    public void setNotificationId(Long notificationId) { this.notificationId = notificationId; }
}