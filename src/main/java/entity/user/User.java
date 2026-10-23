package entity.user;

import entity.common.BaseEntity;
import entity.common.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Objects;

import jakarta.persistence.*;

/**
 * Пользователь системы (сотрудник сервисного центра).
 */
@Entity
@Table(name = "users")

public class User extends BaseEntity {

    @Column(name = "username", nullable = false, unique = true, length = 100)
    private String username;

    @Column(name = "password_hash", nullable = false, length = 500)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 50, nullable = false)
    private Role role;

    @Column(name = "first_name", nullable = false, length = 255)
    private String firstName;

    @Column(name = "last_name", length = 255)
    private String lastName;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "location_id")
    private Long locationId;

    @Column(name = "active")
    private boolean active;

    @Column(name = "completed_orders_count")
    private int completedOrdersCount;

    @Column(name = "completed_works_count")
    private int completedWorksCount;

    @Column(name = "total_revenue", precision = 15, scale = 2)
    private BigDecimal totalRevenue;

    @Column(name = "last_completed_at")
    private Instant lastCompletedAt;

    public User(String username, String passwordHash, Role role,
                String firstName, String lastName, String email) {
        if (role == null) throw new IllegalArgumentException("Роль обязательна");
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Поле 'Логин' не может быть пустым");
        }
        if (passwordHash == null || passwordHash.isBlank()) {
            throw new IllegalArgumentException("Пароль обязателен");
        }
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("Поле 'Имя' не может быть пустым");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Электронная почта обязательна");
        }
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.active = true;
        this.totalRevenue = Money.zero();
        this.completedOrdersCount = 0;
        this.completedWorksCount = 0;
    }

    public String getUsername() { return username; }
    public Role getRole() { return role; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public Long getLocationId() { return locationId; }
    public boolean isActive() { return active; }
    public int getCompletedOrdersCount() { return completedOrdersCount; }
    public int getCompletedWorksCount() { return completedWorksCount; }
    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public Instant getLastCompletedAt() { return lastCompletedAt; }

    String getPasswordHash() { return passwordHash; }

    public void setUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Логин не может быть пустым");
        }
        this.username = username;
    }

    public void setRole(Role role) {
        if (role == null) throw new IllegalArgumentException("Роль обязательна");
        this.role = role;
    }

    public void setEmail(String email) { this.email = email; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public void setLocationId(Long locationId) { this.locationId = locationId; }
    public void setActive(boolean active) { this.active = active; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public void registerCompletedOrderAsMaster(BigDecimal finalPrice) {
        BigDecimal validatedPrice = Money.of(finalPrice, "Сумма заказа");
        completedOrdersCount = Math.addExact(completedOrdersCount, 1);
        totalRevenue = totalRevenue.add(validatedPrice);
        lastCompletedAt = Instant.now();
    }

    public void registerCompletedWork() {
        completedWorksCount = Math.addExact(completedWorksCount, 1);
    }

    public void unregisterCompletedOrderAsMaster(BigDecimal finalPrice) {
        BigDecimal validatedPrice = Money.of(finalPrice, "Сумма заказа");
        if (completedOrdersCount <= 0) {
            throw new IllegalStateException("Нет завершённых заказов для отката");
        }
        if (totalRevenue.compareTo(validatedPrice) < 0) {
            throw new IllegalStateException("Откат превышает учтённую выручку");
        }
        completedOrdersCount--;
        totalRevenue = totalRevenue.subtract(validatedPrice);
    }

    public void unregisterCompletedWork() {
        if (completedWorksCount <= 0) {
            throw new IllegalStateException("Нет завершённых работ для отката");
        }
        completedWorksCount--;
    }

    public BigDecimal getAverageRevenuePerOrder() {
        if (completedOrdersCount == 0) return Money.zero();
        return totalRevenue.divide(BigDecimal.valueOf(completedOrdersCount), 2, RoundingMode.HALF_UP);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(serviceCenterId, user.serviceCenterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serviceCenterId);
    }
}
