package entity.order;

import entity.approval.Approval;
import entity.client.Client;
import entity.common.BaseEntity;
import entity.common.Money;
import entity.document.Document;
import entity.notification.Notification;
import entity.payment.Payment;
import entity.warranty.Warranty;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import jakarta.persistence.*;

@Entity
@Table(name = "orders")

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
    @Column(name = "order_number", nullable = false, unique = true, length = 50)
    private String orderNumber;
    @Column(name = "public_token", nullable = false, unique = true, length = 100)
    private String publicToken;

    // ── Связи ────────────────────────────────────────────────────
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
    @Column(name = "master_id")
    private Long masterId;
    @Column(name = "receiver_id")
    private Long receiverId;
    @Column(name = "location_id")
    private Long locationId;

    // ── Статус и сроки ───────────────────────────────────────────
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50, nullable = false)
    private OrderStatus status;
    @Column(name = "accepted_at", nullable = false)
    private Instant acceptedAt;
    @Column(name = "must_be_finished_at", nullable = false)
    private Instant mustBeFinishedAt;
    @Column(name = "finished_at")
    private Instant finishedAt;
    @Column(name = "pick_up")
    private Instant pickUp;

    // ── Устройство ───────────────────────────────────────────────
    @Enumerated(EnumType.STRING)
    @Column(name = "device_type", length = 50, nullable = false)
    private DeviceType deviceType;
    @Column(length = 255)
    private String manufacturer;
    @Column(length = 255)
    private String model;
    @Column(length = 255)
    private String serialNumber;
    @Column(length = 500)
    private String malfunction;
    @Column(name = "malfunction_description", length = 2000)
    private String malfunctionDescription;
    @Column(length = 500)
    private String accessories;
    @Column(name = "condition_on_acceptance", length = 1000)
    private String conditionOnAcceptance;
    @Column
    private boolean locked;
    @Column(name = "password_provided")
    private boolean passwordProvided;
    @Column(name = "photos_taken")
    private boolean photosTaken;

    // ── Цены и прибыль ───────────────────────────────────────────
    @Column(precision = 15, scale = 2)
    private BigDecimal priceEstimate;
    @Column(precision = 15, scale = 2)
    private BigDecimal finalPrice;
    @Column(precision = 15, scale = 2)
    private BigDecimal diagnosticsPrice;
    @Column(precision = 15, scale = 2)
    private BigDecimal costPrice;   // себестоимость (запчасти + аутсорс)
    @Column(precision = 15, scale = 2)
    private BigDecimal profit;      // finalPrice - costPrice (снимок на момент завершения)

    // ── Условия приёмки ──────────────────────────────────────────
    @Column(name = "client_agreed_to_diagnostics")
    private boolean clientAgreedToDiagnostics;
    @Column(name = "client_agreed_to_password_use")
    private boolean clientAgreedToPasswordUse;
    @Column(name = "storage_deadline_days")
    private int storageDeadlineDays;

    // ── Согласованные условия ────────────────────────────────────
    @Column(name = "approved_price", precision = 15, scale = 2)
    private BigDecimal approvedPrice;
    @Column(name = "approved_deadline")
    private Instant approvedDeadline;
    @Column(name = "approved_at")
    private Instant approvedAt;

    // ── История ──────────────────────────────────────────────────
    @Transient
    private final List<OrderEvent> history = new ArrayList<>();

    // ── Коллекции (JPA relationships) ────────────────────────────
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderWork> orderWorks = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Document> documents = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications = new ArrayList<>();

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Warranty warranty;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Approval> approvals = new ArrayList<>();

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
        if (newStatus == OrderStatus.COMPLETED_AND_PAID && finalPrice == null) {
            throw new IllegalStateException("Перед оплатой необходимо финализировать стоимость заказа");
        }
        Set<OrderStatus> allowed = TRANSITIONS.getOrDefault(this.status, Set.of());
        if (!allowed.contains(newStatus)) {
            throw new IllegalStateException("Недопустимый переход: " + this.status + " -> " + newStatus);
        }
        history.add(new OrderEvent(this.status, newStatus, userId, comment));
        this.status = newStatus;
        if (newStatus == OrderStatus.COMPLETED && this.finishedAt == null) {
            this.finishedAt = Instant.now();
        }
    }

    // ── Согласование ─────────────────────────────────────────────
    public void approveConditions(BigDecimal newPrice, Instant newDeadline) {
        if (status != OrderStatus.AWAITING_APPROVAL) {
            throw new IllegalStateException("Согласование только в статусе AWAITING_APPROVAL");
        }
        // Validate every input before changing any field (all-or-nothing update).
        BigDecimal validatedPrice = Money.of(newPrice, "Согласованная цена");
        if (newDeadline != null && newDeadline.isBefore(acceptedAt)) {
            throw new IllegalArgumentException("Новый срок не может быть раньше приёма");
        }
        this.approvedPrice = validatedPrice;
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
     * partsCost      — сумма purchasePrice запчастей, ушедших в заказ (OUT movements).
     * outsourcedCost — сумма costPrice EXTERNAL работ.
     * Итоговая себестоимость = partsCost + outsourcedCost.
     * Прибыль = finalPrice - (partsCost + outsourcedCost).
     */
    public void finalizePricing(BigDecimal finalPrice, BigDecimal partsCost, BigDecimal outsourcedCost) {
        if (partsCost == null) throw new IllegalArgumentException("Себестоимость запчастей обязательна");
        if (outsourcedCost == null) throw new IllegalArgumentException("Стоимость аутсорса обязательна");
        // Validate each component independently; a negative component must not be
        // hidden by a larger positive component in the sum.
        BigDecimal validatedFinalPrice = Money.of(finalPrice, "Финальная цена");
        BigDecimal validatedPartsCost = Money.of(partsCost, "Себестоимость запчастей");
        BigDecimal validatedOutsourcedCost = Money.of(outsourcedCost, "Стоимость аутсорса");
        BigDecimal validatedCost = Money.of(
                validatedPartsCost.add(validatedOutsourcedCost), "Себестоимость");
        this.finalPrice = validatedFinalPrice;
        this.costPrice = validatedCost;
        this.profit = validatedFinalPrice.subtract(validatedCost);
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

    // ── Equals & HashCode ────────────────────────────────────────
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(orderNumber, order.orderNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderNumber);
    }
}