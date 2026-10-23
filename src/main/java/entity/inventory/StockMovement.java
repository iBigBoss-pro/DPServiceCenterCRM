package entity.inventory;

import entity.common.BaseEntity;
import entity.common.Money;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Движение запчасти по складу.
 * IN     — приход (закупка): purchasePrice = сколько заплатили поставщику, clientPrice = null.
 * OUT    — расход на заказ: purchasePrice = снимок себестоимости, clientPrice = что берём с клиента.
 * ADJUST — инвентаризация (плюс/минус).
 */
public class StockMovement extends BaseEntity {

    private Long partId;
    private Long orderId;
    private MovementType type;
    private int quantity;

    // ── Две цены ─────────────────────────────────────────────────
    private BigDecimal purchasePrice;   // себестоимость единицы
    private BigDecimal clientPrice;     // цена клиенту (только для OUT к заказу)

    private Long userId;
    private Instant movedAt;
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
}