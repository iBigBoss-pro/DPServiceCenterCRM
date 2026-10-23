package entity.order;

import entity.common.BaseEntity;
import entity.common.BaseEntityListener;

import java.time.Instant;
import java.util.Objects;
import jakarta.persistence.*;

/**
 * Событие смены статуса. Создаётся внутри Order.changeStatus.
 * id появится в JPA (@GeneratedValue); здесь — null до сохранения.
 */
@Entity
@Table(name = "order_events")
@EntityListeners(BaseEntityListener.class)
public class OrderEvent extends BaseEntity {

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "timestamp", nullable = false, updatable = false)
    private final Instant timestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "old_status", length = 50, nullable = false)
    private final OrderStatus oldStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", length = 50, nullable = false)
    private final OrderStatus newStatus;

    @Column(name = "user_id", nullable = false)
    private final Long userId;     // кто сменил

    @Column(name = "status_comment", length = 1000)
    private final String statusComment;

    @Column(name = "notification_id")
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

    // ── JPA setters ──────────────────────────────────────────────
    public void setId(Long id) { this.id = id; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    // ── Equals & HashCode ────────────────────────────────────────
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderEvent that = (OrderEvent) o;
        return Objects.equals(id, that.id) && Objects.equals(orderId, that.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderId);
    }
}
