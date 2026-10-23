package entity.servicecenter;

import entity.common.BaseEntity;

import java.util.Objects;

import jakarta.persistence.*;

/**
 * Сервисный центр (тенант). Реквизиты нужны для печати актов и договоров.
 * Пороги авто-статусов клиента — настраиваются per-SC.
 */
@Entity
@Table(name = "service_centers", uniqueConstraints = {
    @UniqueConstraint(columnNames = "inn")
})

public class ServiceCenter extends BaseEntity {

    @Column(name = "name", nullable = false, length = 500)
    private String name;

    @Column(name = "legal_name", nullable = false, length = 500)
    private String legalName;

    @Column(name = "inn", nullable = false, unique = true, length = 50)
    private String inn;

    @Column(name = "ogrn", length = 50)
    private String ogrn;

    @Column(name = "legal_address", length = 1000)
    private String legalAddress;

    @Column(name = "actual_address", length = 1000)
    private String actualAddress;

    @Column(name = "phone", length = 50)
    private String phone;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "director_name", length = 255)
    private String directorName;

    @Column(name = "logo_attachment_id")
    private Long logoAttachmentId;

    @Column(name = "last_order_number")
    private long lastOrderNumber;

    @Column(name = "last_document_number")
    private long lastDocumentNumber;

    @Column(name = "last_order_year")
    private int lastOrderYear;

    @Column(name = "last_document_year")
    private int lastDocumentYear;

    @Column(name = "regular_orders_threshold")
    private int regularOrdersThreshold;

    @Column(name = "vip_profit_threshold_rub")
    private long vipProfitThresholdRub;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceCenter that = (ServiceCenter) o;
        return Objects.equals(id, that.id) && Objects.equals(serviceCenterId, that.serviceCenterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serviceCenterId);
    }
}
