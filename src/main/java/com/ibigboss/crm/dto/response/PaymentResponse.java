package com.ibigboss.crm.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

public class PaymentResponse {

    private Long id;
    private String direction;
    private Long orderId;
    private String expenseCategory;
    private Long orderWorkId;
    private String recipientName;
    private String recipientContact;
    private BigDecimal amount;
    private String method;
    private Instant paidAt;
    private Long acceptedByUserId;
    private String comment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getExpenseCategory() {
        return expenseCategory;
    }

    public void setExpenseCategory(String expenseCategory) {
        this.expenseCategory = expenseCategory;
    }

    public Long getOrderWorkId() {
        return orderWorkId;
    }

    public void setOrderWorkId(Long orderWorkId) {
        this.orderWorkId = orderWorkId;
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

    public Instant getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(Instant paidAt) {
        this.paidAt = paidAt;
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
