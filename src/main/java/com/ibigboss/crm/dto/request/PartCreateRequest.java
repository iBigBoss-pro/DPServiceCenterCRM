package com.ibigboss.crm.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class PartCreateRequest {

    @NotBlank(message = "Название обязательно")
    private String name;

    @NotBlank(message = "Артикул обязателен")
    private String sku;

    @DecimalMin(value = "0", message = "Цена закупки не может быть отрицательной")
    private BigDecimal purchasePrice;

    @DecimalMin(value = "0", message = "Цена продажи не может быть отрицательной")
    private BigDecimal salePrice;

    @Min(value = 0, message = "Минимальный остаток не может быть отрицательным")
    private int minStock;

    private Long locationId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public int getMinStock() {
        return minStock;
    }

    public void setMinStock(int minStock) {
        this.minStock = minStock;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }
}
