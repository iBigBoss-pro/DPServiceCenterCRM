package com.ibigboss.crm.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class OrderResponse {

    private Long id;
    private String orderNumber;
    private String publicToken;
    private Long clientId;
    private String clientName;
    private Long masterId;
    private Long receiverId;
    private Long locationId;
    private String status;
    private Instant acceptedAt;
    private Instant mustBeFinishedAt;
    private Instant finishedAt;
    private Instant pickUp;
    private String deviceType;
    private String manufacturer;
    private String model;
    private String serialNumber;
    private String malfunction;
    private String malfunctionDescription;
    private String accessories;
    private String conditionOnAcceptance;
    private boolean locked;
    private boolean passwordProvided;
    private BigDecimal priceEstimate;
    private BigDecimal finalPrice;
    private BigDecimal diagnosticsPrice;
    private BigDecimal costPrice;
    private BigDecimal profit;
    private BigDecimal approvedPrice;
    private Instant approvedDeadline;
    private Instant approvedAt;
    private int storageDeadlineDays;
    private List<String> statusHistory;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Long getMasterId() {
        return masterId;
    }

    public void setMasterId(Long masterId) {
        this.masterId = masterId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getAcceptedAt() {
        return acceptedAt;
    }

    public void setAcceptedAt(Instant acceptedAt) {
        this.acceptedAt = acceptedAt;
    }

    public Instant getMustBeFinishedAt() {
        return mustBeFinishedAt;
    }

    public void setMustBeFinishedAt(Instant mustBeFinishedAt) {
        this.mustBeFinishedAt = mustBeFinishedAt;
    }

    public Instant getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Instant finishedAt) {
        this.finishedAt = finishedAt;
    }

    public Instant getPickUp() {
        return pickUp;
    }

    public void setPickUp(Instant pickUp) {
        this.pickUp = pickUp;
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

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }

    public BigDecimal getDiagnosticsPrice() {
        return diagnosticsPrice;
    }

    public void setDiagnosticsPrice(BigDecimal diagnosticsPrice) {
        this.diagnosticsPrice = diagnosticsPrice;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public BigDecimal getApprovedPrice() {
        return approvedPrice;
    }

    public void setApprovedPrice(BigDecimal approvedPrice) {
        this.approvedPrice = approvedPrice;
    }

    public Instant getApprovedDeadline() {
        return approvedDeadline;
    }

    public void setApprovedDeadline(Instant approvedDeadline) {
        this.approvedDeadline = approvedDeadline;
    }

    public Instant getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(Instant approvedAt) {
        this.approvedAt = approvedAt;
    }

    public int getStorageDeadlineDays() {
        return storageDeadlineDays;
    }

    public void setStorageDeadlineDays(int storageDeadlineDays) {
        this.storageDeadlineDays = storageDeadlineDays;
    }

    public List<String> getStatusHistory() {
        return statusHistory;
    }

    public void setStatusHistory(List<String> statusHistory) {
        this.statusHistory = statusHistory;
    }
}
