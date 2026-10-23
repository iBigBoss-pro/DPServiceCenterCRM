package entity.approval;

import entity.common.BaseEntity;
import entity.common.Money;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * Согласование изменённых условий с клиентом.
 * Создаётся, когда мастер переводит заказ в AWAITING_APPROVAL.
 */
public class Approval extends BaseEntity {

    private Long orderId;
    private BigDecimal proposedPrice;
    private Instant proposedDeadline;
    private String reason;             // почему меняются условия

    private ApprovalStatus status;
    private ApprovalChannel sentChannel;
    private Instant sentAt;
    private Instant respondedAt;
    private ApprovalChannel respondedChannel;
    private String comment;
    private Long createdByUserId;      // мастер, создавший предложение
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
}