package entity.inventory;

import entity.common.BaseEntity;
import entity.common.Money;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

import jakarta.persistence.*;

/**
 * Движение запчасти по складу.
 * IN     — приход (закупка): purchasePrice = сколько заплатили поставщику, clientPrice = null.
 * OUT    — расхо�� на заказ: purchasePrice = снимок себестоимости, clientPrice = что берём с клиента.
 * ADJUST — инвентаризация (плюс/минус).
 */
@Entity
@Table(name = "stock_movements")

public class StockMovement extends BaseEntity {

    @Column(name = "part_id", nullable = false)
    private Long partId;

    @Column(name = "order_id")
    private Long orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 50, nullable = false)
    private MovementType type;

    @Column(name = "quantity")
    private int quantity;

    // ── Две цены ─────────────────────────────────────────────────
    @Column(name = "purchase_price", precision = 15, scale = 2)
    private BigDecimal purchasePrice;   // себестоимость единицы

    @Column(name = "client_price", precision = 15, scale = 2)
    private BigDecimal clientPrice;     // цена клиенту (только для OUT к заказу)

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "moved_at", nullable = false)
    private Instant movedAt;

    @Column(name = "comment", length = 2000)
    private String comment;

    private StockMovement() {}

    /**
     * Приход (закупка): purchasePrice — сколько заплатили. clientPrice = null.
     */
    public static StockMovement incoming(Long partId, int quantity, BigDecimal purchasePrice,
                                         Long userId, String comment) {
        if (partId == null) throw new IllegalArgumentException("partId обязателен");
        if (quantity <= 0) throw new IllegalArgumentException("Количество прихода должно быть > 0");
        if (purchasePrice == null) throw new IllegalArgumentException("Цена закупки обязательна");
        if (userId == null) throw new IllegalArgumentException("Пользователь обязателен");

        StockMovement m = new StockMovement();
        m.partId = partId;
        m.orderId = null;
        m.type = MovementType.IN;
        m.quantity = quantity;
        m.purchasePrice = Money.of(purchasePrice, "Цена закупки");
        m.clientPrice = null;
        m.userId = userId;
        m.movedAt = Instant.now();
        m.comment = comment;
        return m;
    }

    /**
     * Расход на заказ: purchasePrice — снимок себестоимости, clientPrice — что берём с клиента.
     */
    public static StockMovement outgoingToOrder(Long partId, Long orderId, int quantity,
                                                BigDecimal purchasePrice, BigDecimal clientPrice,
                                                Long userId, String comment) {
        if (partId == null) throw new IllegalArgumentException("partId обязателен");
        if (orderId == null) throw new IllegalArgumentException("orderId обязателен");
        if (quantity <= 0) throw new IllegalArgumentException("Количество расхода должно быть > 0");
        if (purchasePrice == null) throw new IllegalArgumentException("Себестоимость обязательна");
        if (userId == null) throw new IllegalArgumentException("Пользователь обязателен");

        StockMovement m = new StockMovement();
        m.partId = partId;
        m.orderId = orderId;
        m.type = MovementType.OUT;
        m.quantity = quantity;
        m.purchasePrice = Money.of(purchasePrice, "Себестоимость");
        m.clientPrice = clientPrice == null ? Money.zero() : Money.of(clientPrice, "Цена клиенту");
        m.userId = userId;
        m.movedAt = Instant.now();
        m.comment = comment;
        return m;
    }

    /**
     * Корректировка (инвентаризация): quantityDelta может быть положительным или отрицательным.
     */
    public static StockMovement adjust(Long partId, int quantityDelta,
                                       BigDecimal purchasePrice, Long userId, String comment) {
        if (partId == null) throw new IllegalArgumentException("partId обязателен");
        if (quantityDelta == 0) throw new IllegalArgumentException("Корректировка не может быть 0");
        if (purchasePrice == null) throw new IllegalArgumentException("Себестоимость обязательна");
        if (userId == null) throw new IllegalArgumentException("Пользователь обязателен");

        StockMovement m = new StockMovement();
        m.partId = partId;
        m.orderId = null;
        m.type = MovementType.ADJUST;
        m.quantity = quantityDelta;
        m.purchasePrice = Money.of(purchasePrice, "Себестоимость");
        m.clientPrice = null;
        m.userId = userId;
        m.movedAt = Instant.now();
        m.comment = comment;
        return m;
    }

    // ── Геттеры ──────────────────────────────────────────────────
    public Long getPartId() { return partId; }
    public Long getOrderId() { return orderId; }
    public MovementType getType() { return type; }
    public int getQuantity() { return quantity; }
    public BigDecimal getPurchasePrice() { return purchasePrice; }
    public BigDecimal getClientPrice() { return clientPrice; }
    public Long getUserId() { return userId; }
    public Instant getMovedAt() { return movedAt; }
    public String getComment() { return comment; }

    // ── Расчётные величины ───────────────────────────────────────

    public BigDecimal getTotalCost() {
        return purchasePrice.multiply(BigDecimal.valueOf(quantity));
    }

    public BigDecimal getTotalClientPrice() {
        if (clientPrice == null) return Money.zero();
        return clientPrice.multiply(BigDecimal.valueOf(quantity));
    }

    /** Прибыль имеет смысл только для расхода запчасти в заказ. */
    public BigDecimal getProfit() {
        if (type != MovementType.OUT || orderId == null) return Money.zero();
        return getTotalClientPrice().subtract(getTotalCost());
    }

    // ── equals & hashCode ────────────────────────────────────────

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        StockMovement that = (StockMovement) o;
        return quantity == that.quantity
                && Objects.equals(partId, that.partId)
                && Objects.equals(orderId, that.orderId)
                && type == that.type
                && Objects.equals(purchasePrice, that.purchasePrice)
                && Objects.equals(clientPrice, that.clientPrice)
                && Objects.equals(userId, that.userId)
                && Objects.equals(movedAt, that.movedAt)
                && Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), partId, orderId, type, quantity,
                purchasePrice, clientPrice, userId, movedAt, comment);
    }
}
