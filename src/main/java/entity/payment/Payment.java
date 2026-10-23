package entity.payment;

import entity.common.BaseEntity;
import entity.common.Money;
import entity.order.Order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

import jakarta.persistence.*;

/**
 * Финансовая транзакция. INCOME — от клиента, EXPENSE — расход (аутсорсеру, поставщику и т.п.).
 * Для EXPENSE с категорией OUTSOURCE — привязка к OrderWork.
 */
@Entity
@Table(name = "payments")
public class Payment extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "direction", length = 50, nullable = false)
    private PaymentDirection direction;

    // ── INCOME ───────────────────────────────────────────────────
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private Order order;

    @Column(name = "order_id")
    private Long orderId;              // по какому заказу пришли деньги

    // ── EXPENSE ──────────────────────────────────────────────────
    @Enumerated(EnumType.STRING)
    @Column(name = "expense_category", length = 50)
    private ExpenseCategory expenseCategory;   // только для EXPENSE
    @Column(name = "order_work_id")
    private Long orderWorkId;                  // для OUTSOURCE — какая работа
    @Column(name = "recipient_name", length = 500)
    private String recipientName;              // «Сергей», «ООО Поставщик»
    @Column(name = "recipient_contact", length = 500)
    private String recipientContact;           // телефон/Telegram
    @Column(name = "supplier_id")
    private Long supplierId;                   // если появится справочник

    // ── Общее ────────────────────────────────────────────────────
    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    @Column(name = "method", length = 50, nullable = false)
    private PaymentMethod method;
    @Column(name = "paid_at", nullable = false)
    private Instant paidAt;
    @Column(name = "accepted_by_user_id", nullable = false)
    private Long acceptedByUserId;    // кто провёл
    @Column(name = "comment", length = 1000)
    private String comment;

    // ── Конструкторы ─────────────────────────────────────────────

    /** Доход от клиента по заказу. */
    public static Payment income(Long orderId, BigDecimal amount, PaymentMethod method,
                                 Long acceptedByUserId, String comment) {
        if (orderId == null) throw new IllegalArgumentException("orderId обязателен");
        requirePositiveAmount(amount);
        requirePaymentMetadata(method, acceptedByUserId);
        Payment p = new Payment();
        p.direction = PaymentDirection.INCOME;
        p.orderId = orderId;
        p.amount = Money.of(amount, "Сумма");
        p.method = method;
        p.acceptedByUserId = acceptedByUserId;
        p.paidAt = Instant.now();
        p.comment = comment;
        return p;
    }

    /** Расход аутсорсеру за конкретную работу. */
    public static Payment outsource(Long orderWorkId, Long orderId, String recipientName,
                                    String recipientContact, BigDecimal amount,
                                    PaymentMethod method, Long acceptedByUserId, String comment) {
        if (orderWorkId == null) throw new IllegalArgumentException("orderWorkId обязателен");
        if (orderId == null) throw new IllegalArgumentException("orderId обязателен для выплаты по заказу");
        requirePositiveAmount(amount);
        requirePaymentMetadata(method, acceptedByUserId);
        if (recipientName == null || recipientName.isBlank()) {
            throw new IllegalArgumentException("Имя получателя обязательно");
        }
        Payment p = new Payment();
        p.direction = PaymentDirection.EXPENSE;
        p.expenseCategory = ExpenseCategory.OUTSOURCE;
        p.orderWorkId = orderWorkId;
        p.orderId = orderId;
        p.recipientName = recipientName;
        p.recipientContact = recipientContact;
        p.amount = Money.of(amount, "Сумма");
        p.method = method;
        p.acceptedByUserId = acceptedByUserId;
        p.paidAt = Instant.now();
        p.comment = comment;
        return p;
    }

    /** Прочий расход (аренда, закупка и т.п.). */
    public static Payment expense(ExpenseCategory category, String recipientName,
                                  BigDecimal amount, PaymentMethod method,
                                  Long acceptedByUserId, String comment) {
        if (category == null) throw new IllegalArgumentException("Категория расхода обязательна");
        requirePositiveAmount(amount);
        requirePaymentMetadata(method, acceptedByUserId);
        if (category == ExpenseCategory.OUTSOURCE) {
            throw new IllegalArgumentException("Для аутсорса используйте outsource(...)");
        }
        Payment p = new Payment();
        p.direction = PaymentDirection.EXPENSE;
        p.expenseCategory = category;
        p.recipientName = recipientName;
        p.amount = Money.of(amount, "Сумма");
        p.method = method;
        p.acceptedByUserId = acceptedByUserId;
        p.paidAt = Instant.now();
        p.comment = comment;
        return p;
    }

    private Payment() {}

    private static void requirePositiveAmount(BigDecimal amount) {
        BigDecimal validated = Money.of(amount, "Сумма");
        if (validated.signum() <= 0) {
            throw new IllegalArgumentException("Сумма платежа должна быть больше нуля");
        }
    }

    private static void requirePaymentMetadata(PaymentMethod method, Long acceptedByUserId) {
        if (method == null) throw new IllegalArgumentException("Способ оплаты обязателен");
        if (acceptedByUserId == null) throw new IllegalArgumentException("Пользователь, проведший платёж, обязателен");
    }

    // ── Геттеры ──────────────────────────────────────────────────
    public PaymentDirection getDirection() { return direction; }
    public Long getOrderId() { return orderId; }
    public ExpenseCategory getExpenseCategory() { return expenseCategory; }
    public Long getOrderWorkId() { return orderWorkId; }
    public String getRecipientName() { return recipientName; }
    public String getRecipientContact() { return recipientContact; }
    public Long getSupplierId() { return supplierId; }
    public BigDecimal getAmount() { return amount; }
    public PaymentMethod getMethod() { return method; }
    public Instant getPaidAt() { return paidAt; }
    public Long getAcceptedByUserId() { return acceptedByUserId; }
    public String getComment() { return comment; }
    public Order getOrder() { return order; }

    public void setComment(String comment) { this.comment = comment; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(id, payment.id) && Objects.equals(serviceCenterId, payment.serviceCenterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serviceCenterId);
    }
}
