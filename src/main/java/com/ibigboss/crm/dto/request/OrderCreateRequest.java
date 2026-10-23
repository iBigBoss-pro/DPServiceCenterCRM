package com.ibigboss.crm.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;

public class OrderCreateRequest {

    @NotBlank(message = "Номер заказа обязателен")
    private String orderNumber;

    @NotBlank(message = "publicToken обязателен")
    private String publicToken;

    @NotNull(message = "ID клиента обязателен")
    private Long clientId;

    @NotNull(message = "ID приёмщика обязателен")
    private Long receiverId;

    @NotNull(message = "ID точки приёма обязателен")
    private Long locationId;

    @NotNull(message = "Срок сдачи обязателен")
    private Instant mustBeFinishedAt;

    @NotNull(message = "Тип устройства обязателен")
    private String deviceType;

    @NotBlank(message = "Производитель обязателен")
    private String manufacturer;

    @NotBlank(message = "Модель обязательна")
    private String model;

    @NotBlank(message = "Серийный номер обязателен")
    private String serialNumber;

    @NotBlank(message = "Неисправность обязательна")
    private String malfunction;

    private String malfunctionDescription;
    private String accessories;
    private String conditionOnAcceptance;
    private boolean locked;
    private boolean passwordProvided;
    private BigDecimal priceEstimate;

    @Min(value = 1, message = "Срок хранения должен быть >= 1")
    private int storageDeadlineDays = 30;

    private Long masterId;
    private boolean clientAgreedToDiagnostics;
    private boolean clientAgreedToPasswordUse;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getPublicToken() {
        return publicToken;
    }

    public void setPublicToken(String publicToken) {
        this.publicToken = publicToken;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Instant getMustBeFinishedAt() {
        return mustBeFinishedAt;
    }

    public void setMustBeFinishedAt(Instant mustBeFinishedAt) {
        this.mustBeFinishedAt = mustBeFinishedAt;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getMalfunction() {
        return malfunction;
    }

    public void setMalfunction(String malfunction) {
        this.malfunction = malfunction;
    }

    public String getMalfunctionDescription() {
        return malfunctionDescription;
    }

    public void setMalfunctionDescription(String malfunctionDescription) {
        this.malfunctionDescription = malfunctionDescription;
    }

    public String getAccessories() {
        return accessories;
    }

    public void setAccessories(String accessories) {
        this.accessories = accessories;
    }

    public String getConditionOnAcceptance() {
        return conditionOnAcceptance;
    }

    public void setConditionOnAcceptance(String conditionOnAcceptance) {
        this.conditionOnAcceptance = conditionOnAcceptance;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isPasswordProvided() {
        return passwordProvided;
    }

    public void setPasswordProvided(boolean passwordProvided) {
        this.passwordProvided = passwordProvided;
    }

    public BigDecimal getPriceEstimate() {
        return priceEstimate;
    }

    public void setPriceEstimate(BigDecimal priceEstimate) {
        this.priceEstimate = priceEstimate;
    }

    public int getStorageDeadlineDays() {
        return storageDeadlineDays;
    }

    public void setStorageDeadlineDays(int storageDeadlineDays) {
        this.storageDeadlineDays = storageDeadlineDays;
    }

    public Long getMasterId() {
        return masterId;
    }

    public void setMasterId(Long masterId) {
        this.masterId = masterId;
    }

    public boolean isClientAgreedToDiagnostics() {
        return clientAgreedToDiagnostics;
    }

    public void setClientAgreedToDiagnostics(boolean clientAgreedToDiagnostics) {
        this.clientAgreedToDiagnostics = clientAgreedToDiagnostics;
    }

    public boolean isClientAgreedToPasswordUse() {
        return clientAgreedToPasswordUse;
    }

    public void setClientAgreedToPasswordUse(boolean clientAgreedToPasswordUse) {
        this.clientAgreedToPasswordUse = clientAgreedToPasswordUse;
    }
}
