package entity.servicecenter;

import entity.common.BaseEntity;
import jakarta.persistence.*;
import java.util.Objects;

/**
 * Точка / филиал. Даже если одна — заводим запись, чтобы не переделывать БД потом.
 */
@Entity
@Table(name = "locations")

public class Location extends BaseEntity {

    @Column(name = "name", nullable = false, length = 500)
    private String name;             // «Точка на Ленина»

    @Column(name = "address", length = 1000)
    private String address;

    @Column(length = 50)
    private String phone;
    @Column(name = "working_hours", length = 500)
    private String workingHours;     // «Пн-Пт 10:00–19:00, Сб 11:00–17:00»
    @Column
    private boolean active;
    @Column(name = "default_location")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Location location = (Location) o;
        return active == location.active && defaultLocation == location.defaultLocation && Objects.equals(name, location.name) && Objects.equals(address, location.address) && Objects.equals(phone, location.phone) && Objects.equals(workingHours, location.workingHours);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, address, phone, workingHours, active, defaultLocation);
    }
}
