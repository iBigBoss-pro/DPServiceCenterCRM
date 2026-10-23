package entity.servicecenter;

import entity.common.BaseEntity;

/**
 * Точка / филиал. Даже если одна — заводим запись, чтобы не переделывать БД потом.
 */
public class Location extends BaseEntity {

    private String name;             // «Точка на Ленина»
    private String address;
    private String phone;
    private String workingHours;     // «Пн-Пт 10:00–19:00, Сб 11:00–17:00»
    private boolean active;
    private boolean defaultLocation; // true для основной точки

    public Location(String name, String address) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя точки обязательно");
        }
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Адрес точки обязателен");
        }
        this.name = name;
        this.address = address;
        this.active = true;
    }

    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public String getWorkingHours() { return workingHours; }
    public boolean isActive() { return active; }
    public boolean isDefaultLocation() { return defaultLocation; }

    public void setPhone(String phone) { this.phone = phone; }
    public void setWorkingHours(String workingHours) { this.workingHours = workingHours; }
    public void setActive(boolean active) { this.active = active; }
    public void setDefaultLocation(boolean defaultLocation) { this.defaultLocation = defaultLocation; }
}