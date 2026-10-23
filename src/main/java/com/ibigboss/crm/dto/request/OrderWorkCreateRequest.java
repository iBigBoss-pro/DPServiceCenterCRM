package com.ibigboss.crm.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class OrderWorkCreateRequest {

    @NotBlank(message = "Название работы обязательно")
    private String name;

    @NotNull(message = "Цена для клиента обязательна")
    @DecimalMin(value = "0.01", message = "Цена должна быть > 0")
    private BigDecimal clientPrice;

    @NotNull(message = "Количество обязательно")
    @Min(value = 1, message = "Количество должно быть >= 1")
    private Integer quantity;

    @NotNull(message = "Тип исполнителя обязателен")
    private String executorType;

    // For INTERNAL
    private Long masterId;

    // For EXTERNAL
    private String externalMasterName;
    private String externalMasterContact;

    @DecimalMin(value = "0", message = "Себестоимость не может быть отрицательной")
    private BigDecimal costPrice;

    private String comment;

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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
