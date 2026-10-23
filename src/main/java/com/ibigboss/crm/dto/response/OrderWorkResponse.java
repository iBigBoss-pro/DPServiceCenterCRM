package com.ibigboss.crm.dto.response;

import java.math.BigDecimal;

public class OrderWorkResponse {

    private Long id;
    private Long orderId;
    private String name;
    private BigDecimal clientPrice;
    private int quantity;
    private String executorType;
    private Long masterId;
    private String externalMasterName;
    private String externalMasterContact;
    private BigDecimal costPrice;
    private BigDecimal paidToExecutor;
    private BigDecimal totalClientPrice;
    private BigDecimal profit;
    private BigDecimal debtToExecutor;
    private boolean fullyPaidToExecutor;
    private String comment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getClientPrice() {
        return clientPrice;
    }

    public void setClientPrice(BigDecimal clientPrice) {
        this.clientPrice = clientPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getExecutorType() {
        return executorType;
    }

    public void setExecutorType(String executorType) {
        this.executorType = executorType;
    }

    public Long getMasterId() {
        return masterId;
    }

    public void setMasterId(Long masterId) {
        this.masterId = masterId;
    }

    public String getExternalMasterName() {
        return externalMasterName;
    }

    public void setExternalMasterName(String externalMasterName) {
        this.externalMasterName = externalMasterName;
    }

    public String getExternalMasterContact() {
        return externalMasterContact;
    }

    public void setExternalMasterContact(String externalMasterContact) {
        this.externalMasterContact = externalMasterContact;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getPaidToExecutor() {
        return paidToExecutor;
    }

    public void setPaidToExecutor(BigDecimal paidToExecutor) {
        this.paidToExecutor = paidToExecutor;
    }

    public BigDecimal getTotalClientPrice() {
        return totalClientPrice;
    }

    public void setTotalClientPrice(BigDecimal totalClientPrice) {
        this.totalClientPrice = totalClientPrice;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public BigDecimal getDebtToExecutor() {
        return debtToExecutor;
    }

    public void setDebtToExecutor(BigDecimal debtToExecutor) {
        this.debtToExecutor = debtToExecutor;
    }

    public boolean isFullyPaidToExecutor() {
        return fullyPaidToExecutor;
    }

    public void setFullyPaidToExecutor(boolean fullyPaidToExecutor) {
        this.fullyPaidToExecutor = fullyPaidToExecutor;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
