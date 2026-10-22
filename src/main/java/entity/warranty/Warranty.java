package main.java.entity.warranty;

import main.java.entity.common.BaseEntity;
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
        if (months != null && months < 0) throw new IllegalArgumentException("Срок гарантии не может быть < 0");
        this.orderId = orderId;
        this.months = months;
        this.note = note;
        this.issuedByUserId = issuedByUserId;
        this.issuedAt = Instant.now();
        if (months != null) {
            this.validUntil = this.issuedAt.plus(months * 30L, java.time.temporal.ChronoUnit.DAYS);
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

    public void setWarrantyDocumentId(Long id) { this.warrantyDocumentId = id; }
}