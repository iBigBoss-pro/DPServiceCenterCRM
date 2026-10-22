package main.java.entity.order;

import main.java.entity.client.Client;
import main.java.entity.common.BaseEntity;
import main.java.entity.common.Money;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

public class Order extends BaseEntity {

    private static final Map<OrderStatus, Set<OrderStatus>> TRANSITIONS = new EnumMap<>(OrderStatus.class);
    static {
        TRANSITIONS.put(OrderStatus.NEW,                Set.of(OrderStatus.ACCEPTED, OrderStatus.CANCELLED));
        TRANSITIONS.put(OrderStatus.ACCEPTED,           Set.of(OrderStatus.DIAGNOSTICS, OrderStatus.CANCELLED));
        TRANSITIONS.put(OrderStatus.DIAGNOSTICS,        Set.of(OrderStatus.IN_REPAIR, OrderStatus.AWAITING_APPROVAL,
                OrderStatus.AWAITING_PARTS, OrderStatus.CANCELLED));
        TRANSITIONS.put(OrderStatus.AWAITING_APPROVAL,  Set.of(OrderStatus.IN_REPAIR, OrderStatus.CANCELLED));
        TRANSITIONS.put(OrderStatus.AWAITING_PARTS,     Set.of(OrderStatus.IN_REPAIR, OrderStatus.CANCELLED));
        TRANSITIONS.put(OrderStatus.IN_REPAIR,          Set.of(OrderStatus.AWAITING_PARTS, OrderStatus.COMPLETED,
                OrderStatus.CANCELLED));
        TRANSITIONS.put(OrderStatus.COMPLETED,          Set.of(OrderStatus.READY_FOR_PICKUP,
                OrderStatus.COMPLETED_AND_PAID, OrderStatus.RETURNED));
        TRANSITIONS.put(OrderStatus.READY_FOR_PICKUP,   Set.of(OrderStatus.COMPLETED_AND_PAID, OrderStatus.RETURNED));
        TRANSITIONS.put(OrderStatus.COMPLETED_AND_PAID, Set.of(OrderStatus.RETURNED));
        TRANSITIONS.put(OrderStatus.CANCELLED,          Set.of());
        TRANSITIONS.put(OrderStatus.RETURNED,           Set.of());
    }

    // ── Идентификация ────────────────────────────────────────────
    private String orderNumber;
    private String publicToken;

    // ── Связи ────────────────────────────────────────────────────
    private Client client;
    private Long masterId;
    private Long receiverId;
    private Long locationId;

    // ── Статус и сроки ───────────────────────────────────────────
    private OrderStatus status;
    private Instant acceptedAt;
    private Instant mustBeFinishedAt;
    private Instant finishedAt;
    private Instant pickUp;

    // ── Устройство ───────────────────────────────────────────────
    private DeviceType deviceType;
    private String manufacturer;
    private String model;
    private String serialNumber;
    private String malfunction;
    private String malfunctionDescription;
    private String accessories;
    private String conditionOnAcceptance;
    private boolean locked;
    private boolean passwordProvided;
    private boolean photosTaken;

    // ── Цены и прибыль ───────────────────────────────────────────
    private BigDecimal priceEstimate;
    private BigDecimal finalPrice;
    private BigDecimal diagnosticsPrice;
    private BigDecimal costPrice;   // себестоимость (запчасти)
    private BigDecimal profit;      // finalPrice - costPrice (снимок на момент завершения)

    // ── Условия приёмки ──────────────────────────────────────────
    private boolean clientAgreedToDiagnostics;
    private boolean clientAgreedToPasswordUse;
    private int storageDeadlineDays;

    // ── Согласованные условия ────────────────────────────────────
    private BigDecimal approvedPrice;
    private Instant approvedDeadline;
    private Instant approvedAt;

    // ── История ──────────────────────────────────────────────────
    private final List<OrderEvent> history = new ArrayList<>();

    public Order(String orderNumber, String publicToken, Client client, Long receiverId,
                 Long locationId, Instant mustBeFinishedAt, DeviceType deviceType,
                 String manufacturer, String model, String serialNumber,
                 String malfunction, String malfunctionDescription, String accessories,
                 String conditionOnAcceptance, boolean locked, boolean passwordProvided,
                 BigDecimal priceEstimate, int storageDeadlineDays) {
        if (client == null) throw new IllegalArgumentException("Клиент обязателен");
        if (orderNumber == null || orderNumber.isBlank()) throw new IllegalArgumentException("Номер заказа обязателен");
        if (publicToken == null || publicToken.isBlank()) throw new IllegalArgumentException("publicToken обязателен");
        if (receiverId == null) throw new IllegalArgumentException("Приёмщик обязателен");
        if (locationId == null) throw new IllegalArgumentException("Точка приёма обязательна");
        if (mustBeFinishedAt == null) throw new IllegalStateException("Срок сдачи обязателен");
        if (deviceType == null) throw new IllegalArgumentException("Тип устройства обязателен");
        if (manufacturer == null || manufacturer.isBlank()) throw new IllegalArgumentException("Производитель обязателен");
        if (model == null || model.isBlank()) throw new IllegalArgumentException("Модель обязательна");
        if (serialNumber == null || serialNumber.isBlank()) throw new IllegalArgumentException("Серийный номер обязателен");
        if (malfunction == null || malfunction.isBlank()) throw new IllegalArgumentException("Неисправность обязательна");
        if (storageDeadlineDays <= 0) throw new IllegalArgumentException("Срок хранения должен быть > 0");

        this.orderNumber = orderNumber;
        this.publicToken = publicToken;
        this.client = client;
        this.receiverId = receiverId;
        this.locationId = locationId;
        this.mustBeFinishedAt = mustBeFinishedAt;
        this.deviceType = deviceType;
        this.manufacturer = manufacturer;
        this.model = model;
        this.serialNumber = serialNumber;
        this.malfunction = malfunction;
        this.malfunctionDescription = malfunctionDescription;
        this.accessories = accessories;
        this.conditionOnAcceptance = conditionOnAcceptance;
        this.locked = locked;
        this.passwordProvided = passwordProvided;
        this.priceEstimate = Money.of(priceEstimate, "Примерная цена");
        this.diagnosticsPrice = Money.zero();
        this.costPrice = Money.zero();
        this.profit = Money.zero();
        this.storageDeadlineDays = storageDeadlineDays;
        this.status = OrderStatus.NEW;
        this.acceptedAt = Instant.now();
    }

    // ── Смена статуса ────────────────────────────────────────────
    public void changeStatus(OrderStatus newStatus, Long userId, String comment) {
        if (newStatus == null) throw new IllegalArgumentException("Новый статус обязателен");
        if (userId == null) throw new IllegalArgumentException("Пользователь обязателен");
        Set<OrderStatus> allowed = TRANSITIONS.getOrDefault(this.status, Set.of());
        if (!allowed.contains(newStatus)) {
            throw new IllegalStateException("Недопустимый переход: " + this.status + " -> " + newStatus);
        }
        history.add(new OrderEvent(this.status, newStatus, userId, comment));
        this.status = newStatus;
        if (newStatus == OrderStatus.COMPLETED && this.finishedAt == null) {
            this.finishedAt = Instant.now();
        }
        if (newStatus == OrderStatus.COMPLETED_AND_PAID && this.pickUp == null) {
            this.pickUp = Instant.now();
        }
    }

    // ── Согласование ─────────────────────────────────────────────
    public void approveConditions(BigDecimal newPrice, Instant newDeadline) {
        if (status != OrderStatus.AWAITING_APPROVAL) {
            throw new IllegalStateException("Согласование только в статусе AWAITING_APPROVAL");
        }
        this.approvedPrice = Money.of(newPrice, "Согласованная цена");
        if (newDeadline != null && newDeadline.isBefore(acceptedAt)) {
            throw new IllegalArgumentException("Новый срок не может быть раньше приёма");
        }
        this.approvedDeadline = newDeadline;
        this.approvedAt = Instant.now();
    }

    // ── Выдача ───────────────────────────────────────────────────
    public void markPickedUp(Instant pickUp) {
        if (pickUp == null) throw new IllegalArgumentException("Дата выдачи обязательна");
        if (finishedAt == null) throw new IllegalStateException("Нельзя выдать незавершённый заказ");
        if (status != OrderStatus.READY_FOR_PICKUP && status != OrderStatus.COMPLETED_AND_PAID) {
            throw new IllegalStateException("Выдача только из READY_FOR_PICKUP/COMPLETED_AND_PAID, текущий: " + status);
        }
        if (pickUp.isBefore(finishedAt)) throw new IllegalStateException("Дата выдачи раньше завершения");
        this.pickUp = pickUp;
    }

    // ── Финализация цен и прибыли ────────────────────────────────
    /**
     * Вызывается сервисом при переводе в COMPLETED_AND_PAID.
     * costPrice — сумма purchasePrice запчастей, ушедших в заказ.
     * Прибыль = finalPrice - costPrice (хранится снимком).
     */
    public void finalizePricing(BigDecimal finalPrice, BigDecimal costPrice) {
        this.finalPrice = Money.of(finalPrice, "Финальная цена");
        this.costPrice = Money.of(costPrice, "Себестоимость");
        this.profit = this.finalPrice.subtract(this.costPrice);
        // Прибыль может быть отрицательной (гарантия, лояльность) — не блокируем.
    }

    // ── Сеттеры цен ──────────────────────────────────────────────
    public void setPriceEstimate(BigDecimal v) { this.priceEstimate = Money.of(v, "Примерная цена"); }
    public void setDiagnosticsPrice(BigDecimal v) { this.diagnosticsPrice = Money.of(v, "Цена диагностики"); }

    // ── Геттеры ──────────────────────────────────────────────────
    public String getOrderNumber() { return orderNumber; }
    public String getPublicToken() { return publicToken; }
    public Client getClient() { return client; }
    public Long getMasterId() { return masterId; }
    public Long getReceiverId() { return receiverId; }
    public Long getLocationId() { return locationId; }
    public OrderStatus getStatus() { return status; }
    public Instant getAcceptedAt() { return acceptedAt; }
    public Instant getMustBeFinishedAt() { return mustBeFinishedAt; }
    public Instant getFinishedAt() { return finishedAt; }
    public Instant getPickUp() { return pickUp; }
    public DeviceType getDeviceType() { return deviceType; }
    public String getManufacturer() { return manufacturer; }
    public String getModel() { return model; }
    public String getSerialNumber() { return serialNumber; }
    public String getMalfunction() { return malfunction; }
    public String getMalfunctionDescription() { return malfunctionDescription; }
    public String getAccessories() { return accessories; }
    public String getConditionOnAcceptance() { return conditionOnAcceptance; }
    public boolean isLocked() { return locked; }
    public boolean isPasswordProvided() { return passwordProvided; }
    public boolean isPhotosTaken() { return photosTaken; }
    public BigDecimal getPriceEstimate() { return priceEstimate; }
    public BigDecimal getFinalPrice() { return finalPrice; }
    public BigDecimal getDiagnosticsPrice() { return diagnosticsPrice; }
    public BigDecimal getCostPrice() { return costPrice; }
    public BigDecimal getProfit() { return profit; }
    public boolean isClientAgreedToDiagnostics() { return clientAgreedToDiagnostics; }
    public boolean isClientAgreedToPasswordUse() { return clientAgreedToPasswordUse; }
    public int getStorageDeadlineDays() { return storageDeadlineDays; }
    public BigDecimal getApprovedPrice() { return approvedPrice; }
    public Instant getApprovedDeadline() { return approvedDeadline; }
    public Instant getApprovedAt() { return approvedAt; }
    public List<OrderEvent> getHistory() { return Collections.unmodifiableList(history); }

    public void setMasterId(Long masterId) { this.masterId = masterId; }
    public void setPhotosTaken(boolean v) { this.photosTaken = v; }
    public void setClientAgreedToDiagnostics(boolean v) { this.clientAgreedToDiagnostics = v; }
    public void setClientAgreedToPasswordUse(boolean v) { this.clientAgreedToPasswordUse = v; }
    public void setMustBeFinishedAt(Instant mustBeFinishedAt) {
        if (mustBeFinishedAt == null) throw new IllegalArgumentException("Срок обязателен");
        if (mustBeFinishedAt.isBefore(acceptedAt)) throw new IllegalArgumentException("Срок раньше приёма");
        if (finishedAt != null && mustBeFinishedAt.isBefore(finishedAt)) {
            throw new IllegalArgumentException("Срок раньше завершения");
        }
        this.mustBeFinishedAt = mustBeFinishedAt;
    }
}