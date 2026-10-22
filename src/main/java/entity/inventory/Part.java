package main.java.entity.inventory;

import main.java.entity.common.BaseEntity;
import main.java.entity.common.Money;
import java.math.BigDecimal;

public class Part extends BaseEntity {

    private String name;
    private String sku;             // артикул
    private BigDecimal purchasePrice;
    private BigDecimal salePrice;
    private int quantityInStock;
    private int minStock;           // порог для предупреждения
    private Long locationId;
    private boolean active;

    public Part(String name, String sku) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Название запчасти обязательно");
        this.name = name;
        this.sku = sku;
        this.purchasePrice = Money.zero();
        this.salePrice = Money.zero();
        this.quantityInStock = 0;
        this.active = true;
    }

    public String getName() { return name; }
    public String getSku() { return sku; }
    public BigDecimal getPurchasePrice() { return purchasePrice; }
    public BigDecimal getSalePrice() { return salePrice; }
    public int getQuantityInStock() { return quantityInStock; }
    public int getMinStock() { return minStock; }
    public Long getLocationId() { return locationId; }
    public boolean isActive() { return active; }
    public boolean isLowStock() { return quantityInStock <= minStock; }

    public void setPurchasePrice(BigDecimal v) { this.purchasePrice = Money.of(v, "Цена закупки"); }
    public void setSalePrice(BigDecimal v) { this.salePrice = Money.of(v, "Цена продажи"); }
    public void setMinStock(int minStock) { this.minStock = minStock; }
    public void setLocationId(Long locationId) { this.locationId = locationId; }
    public void setActive(boolean active) { this.active = active; }
    public void adjustStock(int delta) { this.quantityInStock += delta; }
}