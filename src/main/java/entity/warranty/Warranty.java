package entity.warranty;

import entity.common.BaseEntity;
import java.time.Instant;

public class Warranty extends BaseEntity {

    private Long orderId;
    private Integer months;        // null = гарантия не даётся (например, после воды)
    private Instant validUntil;    // null, если months == null
    private String note;           // «Гарантия не распространяется на …»
    private Long issuedByUserId;   // кто установил
    private Instant issuedAt;
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
}