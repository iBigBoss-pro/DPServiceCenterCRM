package com.ibigboss.crm.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

public class UserResponse {

    private Long id;
    private String username;
    private String role;
    private String firstName;
    private String lastName;
    private String email;
    private Long locationId;
    private boolean active;
    private int completedOrdersCount;
    private int completedWorksCount;
    private BigDecimal totalRevenue;
    private Instant lastCompletedAt;

    public String getFullName() {
        StringBuilder sb = new StringBuilder();
        if (lastName != null && !lastName.isBlank()) sb.append(lastName);
        if (firstName != null && !firstName.isBlank()) {
            if (sb.length() > 0) sb.append(' ');
            sb.append(firstName);
        }
        return sb.toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getCompletedOrdersCount() {
        return completedOrdersCount;
    }

    public void setCompletedOrdersCount(int completedOrdersCount) {
        this.completedOrdersCount = completedOrdersCount;
    }

    public int getCompletedWorksCount() {
        return completedWorksCount;
    }

    public void setCompletedWorksCount(int completedWorksCount) {
        this.completedWorksCount = completedWorksCount;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Instant getLastCompletedAt() {
        return lastCompletedAt;
    }

    public void setLastCompletedAt(Instant lastCompletedAt) {
        this.lastCompletedAt = lastCompletedAt;
    }
}
