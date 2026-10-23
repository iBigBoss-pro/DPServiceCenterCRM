package entity.approval;

import entity.common.BaseEntity;
import entity.common.Money;
import entity.order.Order;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * Согласование изменённых условий с клиентом.
 * Создаётся, когда мастер переводит заказ в AWAITING_APPROVAL.
 */
@Entity
@Table(name = "approvals")

public class Approval extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, insertable = false, updatable = false)
    private Order order;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "proposed_price", precision = 15, scale = 2)
    private BigDecimal proposedPrice;

    @Column(name = "proposed_deadline")
    private Instant proposedDeadline;

    @Column(length = 1000)
    private String reason;             // почему меняются условия

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50, nullable = false)
    private ApprovalStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "sent_channel", length = 50)
    private ApprovalChannel sentChannel;

    @Column(name = "sent_at")
    private Instant sentAt;
    @Column(name = "responded_at")
    private Instant respondedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "responded_channel", length = 50)
    private ApprovalChannel respondedChannel;

    @Column(length = 1000)
    private String comment;

    @Column(name = "created_by_user_id", nullable = false)
    private Long createdByUserId;      // мастер, создавший предложение

    @Column(name = "expires_at")
    private Instant expiresAt;         // когда истекает ожидание

    public Approval(Long orderId, Long createdByUserId,
                    BigDecimal proposedPrice, Instant proposedDeadline,
                    String reason, ApprovalChannel sentChannel, Instant expiresAt) {
        if (orderId == null) throw new IllegalArgumentException("orderId обязателен");
        if (createdByUserId == null) throw new IllegalArgumentException("Автор обязателен");
        if (sentChannel == null) throw new IllegalArgumentException("Канал отправки обязателен");
        if (expiresAt != null && !expiresAt.isAfter(Instant.now())) {
            throw new IllegalArgumentException("Срок согласования должен быть в будущем");
        }
        this.orderId = orderId;
        this.createdByUserId = createdByUserId;
        this.proposedPrice = proposedPrice == null ? null : Money.of(proposedPrice, "Предложенная цена");
        this.proposedDeadline = proposedDeadline;
        this.reason = reason;
        this.sentChannel = sentChannel;
        this.sentAt = Instant.now();
        this.status = ApprovalStatus.PENDING;
        this.expiresAt = expiresAt;
    }

    public void approve(ApprovalChannel channel, String comment) {
        ensureRespondable(channel);
        this.status = ApprovalStatus.APPROVED;
        this.respondedChannel = channel;
        this.respondedAt = Instant.now();
        this.comment = comment;
    }

    public void reject(ApprovalChannel channel, String comment) {
        ensureRespondable(channel);
        this.status = ApprovalStatus.REJECTED;
        this.respondedChannel = channel;
        this.respondedAt = Instant.now();
        this.comment = comment;
    }

    private void ensureRespondable(ApprovalChannel channel) {
        if (status != ApprovalStatus.PENDING) throw new IllegalStateException("Согласование уже закрыто");
        if (channel == null) throw new IllegalArgumentException("Канал ответа обязателен");
        if (expiresAt != null && !Instant.now().isBefore(expiresAt)) {
            this.status = ApprovalStatus.EXPIRED;
            this.respondedAt = Instant.now();
            throw new IllegalStateException("Срок согласования истёк");
        }
    }

    public void expire() {
        if (status == ApprovalStatus.PENDING) {
            this.status = ApprovalStatus.EXPIRED;
            this.respondedAt = Instant.now();
        }
    }

    public Long getOrderId() { return orderId; }
    public BigDecimal getProposedPrice() { return proposedPrice; }
    public Instant getProposedDeadline() { return proposedDeadline; }
    public String getReason() { return reason; }
    public ApprovalStatus getStatus() { return status; }
    public ApprovalChannel getSentChannel() { return sentChannel; }
    public Instant getSentAt() { return sentAt; }
    public Instant getRespondedAt() { return respondedAt; }
    public ApprovalChannel getRespondedChannel() { return respondedChannel; }
    public String getComment() { return comment; }
    public Long getCreatedByUserId() { return createdByUserId; }
    public Instant getExpiresAt() { return expiresAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Approval approval = (Approval) o;
        return Objects.equals(orderId, approval.orderId) && Objects.equals(proposedPrice, approval.proposedPrice) && Objects.equals(proposedDeadline, approval.proposedDeadline) && Objects.equals(reason, approval.reason) && status == approval.status && sentChannel == approval.sentChannel && Objects.equals(sentAt, approval.sentAt) && Objects.equals(respondedAt, approval.respondedAt) && respondedChannel == approval.respondedChannel && Objects.equals(comment, approval.comment) && Objects.equals(createdByUserId, approval.createdByUserId) && Objects.equals(expiresAt, approval.expiresAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), orderId, proposedPrice, proposedDeadline, reason, status, sentChannel, sentAt, respondedAt, respondedChannel, comment, createdByUserId, expiresAt);
    }
}
