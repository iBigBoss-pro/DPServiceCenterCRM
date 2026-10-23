package com.ibigboss.crm.dto.request;

import jakarta.validation.constraints.NotNull;

public class OrderStatusChangeRequest {

    @NotNull(message = "Новый статус обязателен")
    private String newStatus;

    private Long userId;
    private String comment;

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
