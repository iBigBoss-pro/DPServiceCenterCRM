package entity.order;

import entity.common.BaseEntity;
import entity.common.Money;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Работа по заказу. Может быть выполнена своим мастером (INTERNAL)
 * или отдана на аутсорс (EXTERNAL).
 *
 * clientPrice — что платит клиент за единицу.
 * costPrice   — сколько это стоило вам (0 для своих; сумма аутсорсеру для EXTERNAL).
 * paidToExecutor — сколько уже выплачено аутсорсеру (для INTERNAL всегда 0).
 *
 * Прибыль по работе = clientPrice * quantity - costPrice.
 */
public class OrderWork extends BaseEntity {

    private Long orderId;
    private String name;
    private BigDecimal clientPrice;   // цена для клиента за единицу
    private int quantity;

    // ── Исполнитель ──────────────────────────────────────────────
    private WorkExecutorType executorType;
    private Long masterId;                 // INTERNAL: id User
    private String externalMasterName;     // EXTERNAL: имя/ник аутсорсера
    private String externalMasterContact;  // телефон/Telegram

    // ── Себестоимость работы и выплаты аутсорсеру ────────────────
    private BigDecimal costPrice;        // 0 для своих; сумма аутсорсеру для EXTERNAL
    private BigDecimal paidToExecutor;   // сколько уже выплачено (только EXTERNAL)

    private String comment;

    // ── Конструктор ──────────────────────────────────────────────
    public OrderWork(Long orderId,
                     String name,
                     BigDecimal clientPrice,
                     int quantity,
                     WorkExecutorType executorType,
                     Long masterId,
                     String externalMasterName,
                     String externalMasterContact,
                     BigDecimal costPrice) {
        if (orderId == null) {
            throw new IllegalArgumentException("orderId обязателен");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Название работы обязательно");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Количество должно быть > 0");
        }
        if (executorType == null) {
            throw new IllegalArgumentException("Тип исполнителя обязателен");
        }

        if (executorType == WorkExecutorType.INTERNAL) {
            if (masterId == null) {
                throw new IllegalArgumentException("Для INTERNAL нужен masterId");
            }
            if (externalMasterName != null || externalMasterContact != null) {
                throw new IllegalArgumentException("Для INTERNAL поля аутсорсера не заполняются");
            }
            // Для своих себестоимость работы — 0 (зарплата учитывается отдельно).
            this.costPrice = Money.zero();
        } else {
            if (masterId != null) {
                throw new IllegalArgumentException("Для EXTERNAL masterId не заполняется");
            }
            if (externalMasterName == null || externalMasterName.isBlank()) {
                throw new IllegalArgumentException("Для EXTERNAL нужно имя аутсорсера");
            }
            if (costPrice == null || costPrice.signum() < 0) {
                throw new IllegalArgumentException("Для EXTERNAL нужна себестоимость (сколько платите аутсорсеру)");
            }
            this.costPrice = Money.of(costPrice, "Себестоимость работы");
        }

        this.orderId = orderId;
        this.name = name;
        this.clientPrice = Money.of(clientPrice, "Цена для клиента");
        this.quantity = quantity;
        this.executorType = executorType;
        this.masterId = masterId;
        this.externalMasterName = externalMasterName;
        this.externalMasterContact = externalMasterContact;
        this.paidToExecutor = Money.zero();
    }

    // ── Геттеры ──────────────────────────────────────────────────
    public Long getOrderId() { return orderId; }
    public String getName() { return name; }
    public BigDecimal getClientPrice() { return clientPrice; }
    public int getQuantity() { return quantity; }
    public WorkExecutorType getExecutorType() { return executorType; }
    public Long getMasterId() { return masterId; }
    public String getExternalMasterName() { return externalMasterName; }
    public String getExternalMasterContact() { return externalMasterContact; }
    public BigDecimal getCostPrice() { return costPrice; }
    public BigDecimal getPaidToExecutor() { return paidToExecutor; }
    public String getComment() { return comment; }

    // ── Расчётные величины ───────────────────────────────────────

    /** Итого клиенту за эту работу. */
    public BigDecimal getTotalClientPrice() {
        return clientPrice.multiply(BigDecimal.valueOf(quantity));
    }

    /** Прибыль по этой работе (до выплаты аутсорсеру — начисление). */
    public BigDecimal getProfit() {
        return getTotalClientPrice().subtract(costPrice);
    }

    /** Долг перед аутсорсером по этой работе. Для INTERNAL — 0. */
    public BigDecimal getDebtToExecutor() {
        if (executorType != WorkExecutorType.EXTERNAL) {
            return Money.zero();
        }
        return costPrice.subtract(paidToExecutor);
    }

    /** Полностью ли рассчитались с аутсорсером. */
    public boolean isFullyPaidToExecutor() {
        return executorType == WorkExecutorType.EXTERNAL
                && getDebtToExecutor().signum() == 0;
    }

    // ── Сеттеры / бизнес-методы ──────────────────────────────────
    public void setClientPrice(BigDecimal v) {
        this.clientPrice = Money.of(v, "Цена для клиента");
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Вызывается сервисом при создании Payment.outsource(...).
     * Увеличивает сумму выплаченного аутсорсеру по этой работе.
     * Не позволяет выплатить больше, чем costPrice.
     */
    public void registerPayout(BigDecimal amount) {
        if (executorType != WorkExecutorType.EXTERNAL) {
            throw new IllegalStateException("Выплаты — только для EXTERNAL работ");
        }
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Сумма выплаты должна быть > 0");
        }
        BigDecimal validatedAmount = Money.of(amount, "Сумма выплаты");
        if (validatedAmount.signum() <= 0) throw new IllegalArgumentException("Сумма выплаты должна быть > 0");
        BigDecimal newPaid = paidToExecutor.add(validatedAmount);
        if (newPaid.compareTo(costPrice) > 0) {
            throw new IllegalStateException(
                    "Выплата превышает себестоимость работы: cost=" + costPrice
                            + ", уже выплачено=" + paidToExecutor
                            + ", пытаетесь выплатить=" + amount);
        }
        this.paidToExecutor = newPaid;
    }

    /**
     * Откат выплаты (например, ошибочно проведённый Payment).
     */
    public void unregisterPayout(BigDecimal amount) {
        if (executorType != WorkExecutorType.EXTERNAL) {
            throw new IllegalStateException("Откат выплат — только для EXTERNAL работ");
        }
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Сумма отката должна быть > 0");
        }
        BigDecimal validatedAmount = Money.of(amount, "Сумма отката");
        if (validatedAmount.signum() <= 0) throw new IllegalArgumentException("Сумма отката должна быть > 0");
        if (paidToExecutor.compareTo(validatedAmount) < 0) {
            throw new IllegalStateException("Нельзя откатить больше, чем выплачено");
        }
        this.paidToExecutor = paidToExecutor.subtract(validatedAmount);
    }
}