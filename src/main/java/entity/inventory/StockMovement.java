package main.java.entity.inventory;

import main.java.entity.common.BaseEntity;
import main.java.entity.common.Money;
import java.math.BigDecimal;
import java.time.Instant;

public class StockMovement extends BaseEntity {

    private Long partId;
    private Long orderId;          // null = не связано с заказом (например, закупка)
    private MovementType type;
    private int quantity;          // положительное для IN, отрицательное для OUT
    private BigDecimal price;      // цена на момент движения
    private Long userId;
    private Instant movedAt;
    private String comment;

    public StockMovement(Long partId, Long orderId, MovementType type,
                         int quantity, BigDecimal price, Long userId, String comment) {
        if (partId == null) throw new IllegalArgumentException("partId обязателен");
        if (type == null) throw new IllegalArgumentException("Тип движения обязателен");
        if (quantity == 0) throw new IllegalArgumentException("Количество не может быть 0");
        if (userId == null) throw new IllegalArgumentException("Пользователь обязателен");
        this.partId = partId;
        this.orderId = orderId;
        this.type = type;
        this.quantity = quantity;
        this.price = price == null ? null : Money.of(price, "Цена");
        this.userId = userId;
        this.movedAt = Instant.now();
        this.comment = comment;
    }

    public Long getPartId() { return partId; }
    public Long getOrderId() { return orderId; }
    public MovementType getType() { return type; }
    public int getQuantity() { return quantity; }
    public BigDecimal getPrice() { return price; }
    public Long getUserId() { return userId; }
    public Instant getMovedAt() { return movedAt; }
    public String getComment() { return comment; }
}