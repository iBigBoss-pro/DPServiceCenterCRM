package com.ibigboss.crm.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class PaymentCreateRequest {

    @NotNull(message = "Направление обязательно")
    private String direction;

    private Long orderId;
    private Long orderWorkId;

    private String expenseCategory;

    private String recipientName;
    private String recipientContact;
    private Long supplierId;

    @NotNull(message = "Сумма обязательна")
    @DecimalMin(value = "0.01", message = "Сумма должна быть > 0")
    private BigDecimal amount;

    @NotNull(message = "Способ оплаты обязателен")
    private String method;

    private Long acceptedByUserId;
    private String comment;

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderWorkId() {
        return orderWorkId;
    }

    public void setOrderWorkId(Long orderWorkId) {
        this.orderWorkId = orderWorkId;
    }

    public String getExpenseCategory() {
        return expenseCategory;
    }

    public void setExpenseCategory(String expenseCategory) {
        this.expenseCategory = expenseCategory;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientContact() {
        return recipientContact;
    }

    public void setRecipientContact(String recipientContact) {
        this.recipientContact = recipientContact;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Long getAcceptedByUserId() {
        return acceptedByUserId;
    }

    public void setAcceptedByUserId(Long acceptedByUserId) {
        this.acceptedByUserId = acceptedByUserId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
