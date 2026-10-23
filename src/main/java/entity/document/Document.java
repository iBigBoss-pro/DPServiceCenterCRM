package entity.document;

import entity.common.BaseEntity;
import entity.order.Order;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.Objects;

/**
 * Распечатанный документ. Хранит СНИМОК тела на момент печати (шаблон мог измениться).
 */
@Entity
@Table(name = "documents")

public class Document extends BaseEntity {

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 50, nullable = false)
    private DocumentType type;

    @Column(name = "document_number", length = 50, nullable = false)
    private String documentNumber;     // SC1-2026-000123

    @Column(name = "template_id", nullable = false)
    private Long templateId;

    @Column(name = "template_version", nullable = false)
    private int templateVersion;       // версия шаблона на момент печати

    @Column(name = "body_html", columnDefinition = "TEXT", nullable = false)
    private String bodyHtml;           // снимок HTML (шаблон уже подставлен)

    @Column(name = "pdf_path")
    private String pdfPath;            // путь к сохранённому PDF (или blob)

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50, nullable = false)
    private DocumentStatus status;

    @Column(name = "printed_at", nullable = false)
    private Instant printedAt;

    @Column(name = "printed_by_user_id", nullable = false)
    private Long printedByUserId;

    @Column(name = "signed_at")
    private Instant signedAt;          // когда клиент подписал

    @Column(name = "signed_by_user_id")
    private Long signedByUserId;       // кто из сотрудников принял подпись

    @Column(name = "signed_scan_attachment_id")
    private Long signedScanAttachmentId; // скан подписанного

    @Column(name = "voided_by_user_id")
    private Long voidedByUserId;       // кто аннулировал

    @Column(name = "voided_at")
    private Instant voidedAt;

    @Column(name = "void_reason")
    private String voidReason;

    @Column(name = "copy_number", nullable = false)
    private int copyNumber;            // 1 = оригинал, 2+ = повторная печать

    @Column(name = "next_copy_number")
    private int nextCopyNumber = 2;    // sequence for repeated calls on the same original in this domain instance

    public Document(Long orderId, DocumentType type, String documentNumber,
                    Long templateId, int templateVersion, String bodyHtml,
                    Long printedByUserId) {
        if (orderId == null) throw new IllegalArgumentException("orderId обязателен");
        if (type == null) throw new IllegalArgumentException("Тип документа обязателен");
        if (documentNumber == null || documentNumber.isBlank()) {
            throw new IllegalArgumentException("Номер документа обязателен");
        }
        if (templateId == null) throw new IllegalArgumentException("Шаблон обязателен");
        if (bodyHtml == null || bodyHtml.isBlank()) throw new IllegalArgumentException("Тело документа обязательно");
        if (printedByUserId == null) throw new IllegalArgumentException("Кто печатает — обязателен");

        this.orderId = orderId;
        this.type = type;
        this.documentNumber = documentNumber;
        this.templateId = templateId;
        this.templateVersion = templateVersion;
        this.bodyHtml = bodyHtml;
        this.status = DocumentStatus.PRINTED;
        this.printedAt = Instant.now();
        this.printedByUserId = printedByUserId;
        this.copyNumber = 1;
    }

    public void markSigned(Long signedByUserId, Long scanAttachmentId) {
        if (status != DocumentStatus.PRINTED && status != DocumentStatus.REPRINT) {
            throw new IllegalStateException("Подписать можно только распечатанный документ");
        }
        if (signedByUserId == null) throw new IllegalArgumentException("Сотрудник, принявший подпись, обязателен");
        this.signedAt = Instant.now();
        this.signedByUserId = signedByUserId;
        this.signedScanAttachmentId = scanAttachmentId;
        this.status = DocumentStatus.SIGNED;
    }

    public void voidDocument(Long userId, String reason) {
        if (status == DocumentStatus.VOID) throw new IllegalStateException("Уже аннулирован");
        if (userId == null) throw new IllegalArgumentException("Сотрудник, аннулирующий документ, обязателен");
        if (reason == null || reason.isBlank()) throw new IllegalArgumentException("Причина аннулирования обязательна");
        this.voidedAt = Instant.now();
        this.voidedByUserId = userId;
        this.voidReason = reason;
        this.status = DocumentStatus.VOID;
    }

    public synchronized Document reprint(Long printedByUserId) {
        if (status == DocumentStatus.VOID) throw new IllegalStateException("Нельзя повторно печатать аннулированный документ");
        if (copyNumber != 1) throw new IllegalStateException("Повторную копию нужно создавать из исходного документа");
        if (printedByUserId == null) throw new IllegalArgumentException("Сотрудник, печатающий документ, обязателен");
        Document copy = new Document(orderId, type, documentNumber, templateId,
                templateVersion, bodyHtml, printedByUserId);
        copy.copyNumber = nextCopyNumber++;
        copy.status = DocumentStatus.REPRINT;
        return copy;
    }

    public Long getOrderId() { return orderId; }
    public DocumentType getType() { return type; }
    public String getDocumentNumber() { return documentNumber; }
    public Long getTemplateId() { return templateId; }
    public int getTemplateVersion() { return templateVersion; }
    public String getBodyHtml() { return bodyHtml; }
    public String getPdfPath() { return pdfPath; }
    public DocumentStatus getStatus() { return status; }
    public Instant getPrintedAt() { return printedAt; }
    public Long getPrintedByUserId() { return printedByUserId; }
    public Instant getSignedAt() { return signedAt; }
    public Long getSignedByUserId() { return signedByUserId; }
    public Long getSignedScanAttachmentId() { return signedScanAttachmentId; }
    public Long getVoidedByUserId() { return voidedByUserId; }
    public Instant getVoidedAt() { return voidedAt; }
    public String getVoidReason() { return voidReason; }
    public int getCopyNumber() { return copyNumber; }

    public void setPdfPath(String pdfPath) { this.pdfPath = pdfPath; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Document document = (Document) o;
        return Objects.equals(getId(), document.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}

