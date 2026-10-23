package com.ibigboss.crm.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class OrderFinalizePricingRequest {

    @NotNull(message = "Финальная цена обязательна")
    @DecimalMin(value = "0.01", message = "Цена должна быть > 0")
    private BigDecimal finalPrice;

    @NotNull(message = "Себестоимость запчастей обязательна")
    @DecimalMin(value = "0", message = "Себестоимость запчастей не может быть отрицательной")
    private BigDecimal partsCost;

    @NotNull(message = "Стоимость аутсорса обязательна")
    @DecimalMin(value = "0", message = "Стоимость аутсорса не может быть отрицательной")
    private BigDecimal outsourcedCost;

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }

    public BigDecimal getPartsCost() {
        return partsCost;
    }

    public void setPartsCost(BigDecimal partsCost) {
        this.partsCost = partsCost;
    }

    public BigDecimal getOutsourcedCost() {
        return outsourcedCost;
    }

    public void setOutsourcedCost(BigDecimal outsourcedCost) {
        this.outsourcedCost = outsourcedCost;
    }
}
