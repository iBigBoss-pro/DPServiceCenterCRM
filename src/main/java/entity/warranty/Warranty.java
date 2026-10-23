package entity.warranty;

import entity.common.BaseEntity;
import entity.order.Order;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "warranties")

public class Warranty extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, insertable = false, updatable = false)
    private Order order;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column
    private Integer months;        // null = гарантия не даётся (например, после воды)

    @Column(name = "valid_until")
    private Instant validUntil;    // null, если months == null

    @Column(length = 1000)
    private String note;           // «Гарантия не распространяется на …»

    @Column(name = "issued_by_user_id", nullable = false)
    private Long issuedByUserId;   // кто установил

    @Column(name = "issued_at", nullable = false, updatable = false)
    private Instant issuedAt;
    @Column(name = "warranty_document_id")
    private Long warrantyDocumentId; // акт гарантии (Document)

    public Warranty(Long orderId, Integer months, String note, Long issuedByUserId) {
        if (orderId == null) throw new IllegalArgumentException("orderId обязателен");
        if (issuedByUserId == null) throw new IllegalArgumentException("Кто устанавливает гарантию — обязателен");
        if (months != null && months < 1) throw new IllegalArgumentException("Срок гарантии должен быть не меньше одного месяца; null означает отсутствие гарантии");
        this.orderId = orderId;
        this.months = months;
        this.note = note;
        this.issuedByUserId = issuedByUserId;
        this.issuedAt = Instant.now();
        if (months != null) {
            this.validUntil = this.issuedAt.atZone(java.time.ZoneOffset.UTC)
                    .plusMonths(months).toInstant();
        }
    }

    public boolean hasWarranty() { return months != null; }

    public Long getOrderId() { return orderId; }
    public Integer getMonths() { return months; }
    public Instant getValidUntil() { return validUntil; }
    public String getNote() { return note; }
    public Long getIssuedByUserId() { return issuedByUserId; }
    public Instant getIssuedAt() { return issuedAt; }
    public Long getWarrantyDocumentId() { return warrantyDocumentId; }

    public void setWarrantyDocumentId(Long id) {
        if (id != null && id <= 0) throw new IllegalArgumentException("Идентификатор гарантийного документа должен быть > 0");
        this.warrantyDocumentId = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Warranty warranty = (Warranty) o;
        return Objects.equals(orderId, warranty.orderId) && Objects.equals(months, warranty.months) && Objects.equals(validUntil, warranty.validUntil) && Objects.equals(note, warranty.note) && Objects.equals(issuedByUserId, warranty.issuedByUserId) && Objects.equals(issuedAt, warranty.issuedAt) && Objects.equals(warrantyDocumentId, warranty.warrantyDocumentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), orderId, months, validUntil, note, issuedByUserId, issuedAt, warrantyDocumentId);
    }
}
