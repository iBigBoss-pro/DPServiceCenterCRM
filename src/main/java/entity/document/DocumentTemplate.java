package entity.document;

import entity.common.BaseEntity;
import jakarta.persistence.*;

import java.util.Objects;

/**
 * Шаблон документа с ВЕРСИОНИРОВАНИЕМ. При изменении шаблона — новая версия,
 * старые документы продолжают ссылаться на старую версию.
 */
@Entity
@Table(name = "document_templates", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"code", "version"})
})

public class DocumentTemplate extends BaseEntity {

    @Column(name = "code", length = 100, nullable = false)
    private String code;           // ACCEPTANCE_ACT, PAYMENT_RECEIPT...

    @Column(name = "version", nullable = false)
    private int version;           // 1, 2, 3...

    @Column(name = "language", length = 10, nullable = false)
    private String language;       // ru, en

    @Column(name = "name", length = 255, nullable = false)
    private String name;           // «Акт приёма (основной)»

    @Column(name = "body_html", columnDefinition = "TEXT", nullable = false)
    private String bodyHtml;       // с плейсхолдерами {clientName} и т.п.

    @Column(name = "active", nullable = false)
    private boolean active;        // активная версия

    public DocumentTemplate(String code, int version, String language, String name, String bodyHtml) {
        if (code == null || code.isBlank()) throw new IllegalArgumentException("Код шаблона обязателен");
        if (version <= 0) throw new IllegalArgumentException("Версия должна быть > 0");
        if (language == null || language.isBlank()) throw new IllegalArgumentException("Язык обязателен");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Имя шаблона обязательно");
        if (bodyHtml == null || bodyHtml.isBlank()) throw new IllegalArgumentException("Тело шаблона обязательно");
        this.code = code;
        this.version = version;
        this.language = language;
        this.name = name;
        this.bodyHtml = bodyHtml;
        this.active = true;
    }

    public String getCode() { return code; }
    public int getVersion() { return version; }
    public String getLanguage() { return language; }
    public String getName() { return name; }
    public String getBodyHtml() { return bodyHtml; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentTemplate that = (DocumentTemplate) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
