package entity.servicecenter;

import entity.common.BaseEntity;

/**
 * Сервисный центр (тенант). Реквизиты нужны для печати актов и договоров.
 * Пороги авто-статусов клиента — настраиваются per-SC.
 */
public class ServiceCenter extends BaseEntity {

    private String name;
    private String legalName;
    private String inn;
    private String ogrn;
    private String legalAddress;
    private String actualAddress;
    private String phone;
    private String email;
    private String directorName;
    private Long logoAttachmentId;

    // Счётчики для человекочитаемых номеров
    private long lastOrderNumber;
    private long lastDocumentNumber;
    private int lastOrderYear;
    private int lastDocumentYear;

    // ── Пороги авто-статусов клиента ────────────────────────────
    private int regularOrdersThreshold;     // сколько заказов → REGULAR (по умолчанию 3)
    private long vipProfitThresholdRub;     // накопленная прибыль в рублях → VIP (по умолчанию 30000)

    public ServiceCenter(String name, String legalName, String inn,
                         String legalAddress, String actualAddress,
                         String phone, String email, String directorName) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Имя СЦ обязательно");
        if (legalName == null || legalName.isBlank()) throw new IllegalArgumentException("Юридическое название обязательно");
        if (inn == null || inn.isBlank()) throw new IllegalArgumentException("ИНН обязателен");
        if (legalAddress == null || legalAddress.isBlank()) throw new IllegalArgumentException("Юридический адрес обязателен");
        this.name = name;
        this.legalName = legalName;
        this.inn = inn;
        this.legalAddress = legalAddress;
        this.actualAddress = actualAddress;
        this.phone = phone;
        this.email = email;
        this.directorName = directorName;
        this.regularOrdersThreshold = 3;
        this.vipProfitThresholdRub = 30_000L;
    }

    // ── Геттеры (сокращённо) ────────────────────────────────────
    public String getName() { return name; }
    public String getLegalName() { return legalName; }
    public String getInn() { return inn; }
    public String getOgrn() { return ogrn; }
    public String getLegalAddress() { return legalAddress; }
    public String getActualAddress() { return actualAddress; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getDirectorName() { return directorName; }
    public Long getLogoAttachmentId() { return logoAttachmentId; }
    public long getLastOrderNumber() { return lastOrderNumber; }
    public long getLastDocumentNumber() { return lastDocumentNumber; }
    public int getLastOrderYear() { return lastOrderYear; }
    public int getLastDocumentYear() { return lastDocumentYear; }
    public int getRegularOrdersThreshold() { return regularOrdersThreshold; }
    public long getVipProfitThresholdRub() { return vipProfitThresholdRub; }

    public void setRegularOrdersThreshold(int v) {
        if (v < 1) throw new IllegalArgumentException("Порог REGULAR должен быть >= 1");
        this.regularOrdersThreshold = v;
    }
    public void setVipProfitThresholdRub(long v) {
        if (v < 1) throw new IllegalArgumentException("Порог VIP должен быть > 0");
        this.vipProfitThresholdRub = v;
    }

    // ── Генерация номеров (в JPA — в транзакции с пессимистичным локом) ──
    public synchronized String nextOrderNumber(int currentYear) {
        if (currentYear < 1) throw new IllegalArgumentException("Год должен быть положительным");
        if (currentYear != lastOrderYear) { lastOrderYear = currentYear; lastOrderNumber = 0; }
        lastOrderNumber++;
        return "%s-%d-%06d".formatted(shortCode(), currentYear, lastOrderNumber);
    }
    public synchronized String nextDocumentNumber(int currentYear) {
        if (currentYear < 1) throw new IllegalArgumentException("Год должен быть положительным");
        if (currentYear != lastDocumentYear) { lastDocumentYear = currentYear; lastDocumentNumber = 0; }
        lastDocumentNumber++;
        return "%s-%d-%06d".formatted(shortCode(), currentYear, lastDocumentNumber);
    }
    private String shortCode() {
        String n = name.replaceAll("[^A-Za-zА-Яа-я0-9]", "");
        return n.length() >= 3 ? n.substring(0, 3).toUpperCase() : "SC";
    }
}