package entity.inventory;

import entity.common.BaseEntity;
import entity.common.Money;

import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.*;

@Entity
@Table(name = "parts")

public class Part extends BaseEntity {

    @Column(name = "name", nullable = false, length = 500)
    private String name;

    @Column(name = "sku", nullable = false, length = 100)
    private String sku;             // артикул

    @Column(name = "purchase_price", precision = 15, scale = 2)
    private BigDecimal purchasePrice;

    @Column(name = "sale_price", precision = 15, scale = 2)
    private BigDecimal salePrice;

    @Column(name = "quantity_in_stock")
    private int quantityInStock;

    @Column(name = "min_stock")
    private int minStock;           // порог для предупреждения

    @Column(name = "location_id")
    private Long locationId;

    @Column(name = "active")
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
    public void setName(String name) { this.name = name; }
    public void setSku(String sku) { this.sku = sku; }
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

    // ── equals & hashCode ────────────────────────────────────────

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Part part = (Part) o;
        return quantityInStock == part.quantityInStock
                && minStock == part.minStock
                && active == part.active
                && Objects.equals(name, part.name)
                && Objects.equals(sku, part.sku)
                && Objects.equals(purchasePrice, part.purchasePrice)
                && Objects.equals(salePrice, part.salePrice)
                && Objects.equals(locationId, part.locationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, sku, purchasePrice, salePrice,
                quantityInStock, minStock, locationId, active);
    }
}
