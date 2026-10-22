package main.java.entity.document;

import main.java.entity.common.BaseEntity;
import java.time.Instant;

/**
 * Распечатанный документ. Хранит СНИМОК тела на момент печати (шаблон мог измениться).
 */
public class Document extends BaseEntity {

    private Long orderId;
    private DocumentType type;
    private String documentNumber;     // SC1-2026-000123

    private Long templateId;
    private int templateVersion;       // версия шаблона на момент печати
    private String bodyHtml;           // снимок HTML (шаблон уже подставлен)
    private String pdfPath;            // путь к сохранённому PDF (или blob)

    private DocumentStatus status;
    private Instant printedAt;
    private Long printedByUserId;

    private Instant signedAt;          // когда клиент подписал
    private Long signedByUserId;       // кто из сотрудников принял подпись
    private Long signedScanAttachmentId; // скан подписанного

    private Long voidedByUserId;       // кто аннулировал
    private Instant voidedAt;
    private String voidReason;

    private int copyNumber;            // 1 = оригинал, 2+ = повторная печать

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
        if (status != DocumentStatus.PRINTED) throw new IllegalStateException("Подписать можно только распечатанный документ");
        this.signedAt = Instant.now();
        this.signedByUserId = signedByUserId;
        this.signedScanAttachmentId = scanAttachmentId;
        this.status = DocumentStatus.SIGNED;
    }

    public void voidDocument(Long userId, String reason) {
        if (status == DocumentStatus.VOID) throw new IllegalStateException("Уже аннулирован");
        this.voidedAt = Instant.now();
        this.voidedByUserId = userId;
        this.voidReason = reason;
        this.status = DocumentStatus.VOID;
    }

    public Document reprint(Long printedByUserId) {
        Document copy = new Document(orderId, type, documentNumber, templateId,
                templateVersion, bodyHtml, printedByUserId);
        copy.copyNumber = this.copyNumber + 1;
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
}