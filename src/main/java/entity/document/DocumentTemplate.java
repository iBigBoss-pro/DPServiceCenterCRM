package main.java.entity.document;

import main.java.entity.common.BaseEntity;

/**
 * Шаблон документа с ВЕРСИОНИРОВАНИЕМ. При изменении шаблона — новая версия,
 * старые документы продолжают ссылаться на старую версию.
 */
public class DocumentTemplate extends BaseEntity {

    private String code;           // ACCEPTANCE_ACT, PAYMENT_RECEIPT...
    private int version;           // 1, 2, 3...
    private String language;       // ru, en
    private String name;           // «Акт приёма (основной)»
    private String bodyHtml;       // с плейсхолдерами {clientName} и т.п.
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
}