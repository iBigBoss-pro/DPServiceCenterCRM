package entity.inventory;

import entity.common.BaseEntity;
import entity.common.Money;
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
    public void setMinStock(int minStock) {
        if (minStock < 0) throw new IllegalArgumentException("Минимальный остаток не может быть отрицательным");
        this.minStock = minStock;
    }
    public void setLocationId(Long locationId) {
        if (locationId != null && locationId <= 0) throw new IllegalArgumentException("locationId должен быть > 0");
        this.locationId = locationId;
    }
    public void setActive(boolean active) { this.active = active; }
    public void adjustStock(int delta) {
        final int updated;
        try {
            updated = Math.addExact(this.quantityInStock, delta);
        } catch (ArithmeticException ex) {
            throw new IllegalArgumentException("Остаток выходит за допустимый диапазон", ex);
        }
        if (updated < 0) {
            throw new IllegalStateException("Недостаточно запчастей на складе: доступно "
                    + this.quantityInStock + ", запрошено " + Math.abs((long) delta));
        }
        this.quantityInStock = updated;
    }
}