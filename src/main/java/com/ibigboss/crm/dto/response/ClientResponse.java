package com.ibigboss.crm.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

public class ClientResponse {

    private Long id;
    private String lastName;
    private String firstName;
    private String patronymic;
    private String phoneNumber;
    private String email;
    private String telegramChatId;
    private String status;
    private boolean important;
    private BigDecimal discountPercent;
    private int totalOrders;
    private BigDecimal totalSpent;
    private BigDecimal totalProfit;
    private Instant firstVisitAt;
    private Instant lastVisitAt;
    private String note;
    private String importanceNote;

    public String getFullName() {
        StringBuilder sb = new StringBuilder();
        if (lastName != null && !lastName.isBlank()) sb.append(lastName);
        if (firstName != null && !firstName.isBlank()) {
            if (sb.length() > 0) sb.append(' ');
            sb.append(firstName);
        }
        if (patronymic != null && !patronymic.isBlank()) {
            if (sb.length() > 0) sb.append(' ');
            sb.append(patronymic);
        }
        return sb.toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelegramChatId() {
        return telegramChatId;
    }

    public void setTelegramChatId(String telegramChatId) {
        this.telegramChatId = telegramChatId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isImportant() {
        return important;
    }

    public void setImportant(boolean important) {
        this.important = important;
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    public BigDecimal getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(BigDecimal totalSpent) {
        this.totalSpent = totalSpent;
    }

    public BigDecimal getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(BigDecimal totalProfit) {
        this.totalProfit = totalProfit;
    }

    public Instant getFirstVisitAt() {
        return firstVisitAt;
    }

    public void setFirstVisitAt(Instant firstVisitAt) {
        this.firstVisitAt = firstVisitAt;
    }

    public Instant getLastVisitAt() {
        return lastVisitAt;
    }

    public void setLastVisitAt(Instant lastVisitAt) {
        this.lastVisitAt = lastVisitAt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getImportanceNote() {
        return importanceNote;
    }

    public void setImportanceNote(String importanceNote) {
        this.importanceNote = importanceNote;
    }
}
