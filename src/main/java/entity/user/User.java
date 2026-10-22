package main.java.entity.user;

import main.java.entity.common.BaseEntity;
import main.java.entity.common.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

public class User extends BaseEntity {

    private String username;
    private String passwordHash;
    private Role role;
    private String firstName;
    private String lastName;
    private String email;
    private Long locationId;      // к какой точке привязан (null = все)
    private boolean active;

    // ── Агрегаты мастера (обновляет сервис) ─────────────────────
    private int completedOrdersCount;   // заказы, где он masterId
    private int completedWorksCount;    // работы из OrderWork, где он исполнитель
    private BigDecimal totalRevenue;    // сумма finalPrice заказов, где он masterId
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

    // ── Геттеры ──────────────────────────────────────────────────
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

    // ── Сеттеры ──────────────────────────────────────────────────
    public void setRole(Role role) {
        if (role == null) throw new IllegalArgumentException("Роль обязательна");
        this.role = role;
    }
    public void setLocationId(Long locationId) { this.locationId = locationId; }
    public void setActive(boolean active) { this.active = active; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    // ── Бизнес-методы ────────────────────────────────────────────

    /**
     * Вызывается сервисом, когда заказ, где этот User — masterId, переходит в COMPLETED_AND_PAID.
     * Считает заказы, где он ГЛАВНЫЙ мастер.
     */
    public void registerCompletedOrderAsMaster(BigDecimal finalPrice) {
        if (finalPrice == null) throw new IllegalArgumentException("Сумма обязательна");
        completedOrdersCount++;
        totalRevenue = totalRevenue.add(finalPrice);
        lastCompletedAt = Instant.now();
    }

    /**
     * Вызывается сервисом, когда добавляется OrderWork с этим исполнителем.
     * Считает работы, где он ИСПОЛНИТЕЛЬ (может быть больше, чем заказов).
     */
    public void registerCompletedWork() {
        completedWorksCount++;
    }

    /**
     * Обратные методы для отката (возврат / удаление работы).
     */
    public void unregisterCompletedOrderAsMaster(BigDecimal finalPrice) {
        if (finalPrice == null) throw new IllegalArgumentException("Сумма обязательна");
        if (completedOrdersCount > 0) completedOrdersCount--;
        totalRevenue = totalRevenue.subtract(finalPrice);
    }

    public void unregisterCompletedWork() {
        if (completedWorksCount > 0) completedWorksCount--;
    }

    public BigDecimal getAverageRevenuePerOrder() {
        if (completedOrdersCount == 0) return Money.zero();
        return totalRevenue.divide(BigDecimal.valueOf(completedOrdersCount), 2, RoundingMode.HALF_UP);
    }
}