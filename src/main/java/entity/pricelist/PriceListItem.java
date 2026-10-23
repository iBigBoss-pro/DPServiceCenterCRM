package entity.pricelist;

import entity.common.BaseEntity;
import entity.common.Money;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "price_list_items")

public class PriceListItem extends BaseEntity {

    @Column(name = "name", nullable = false, length = 500)
    private String name;          // «Замена экрана iPhone 13»

    @Column(name = "category", length = 255)
    private String category;      // «Apple», «Экраны»

    @Column(name = "price", precision = 15, scale = 2)
    private BigDecimal price;

    @Column
    private boolean active;
    @Column(name = "location_id")
    private Long locationId;      // null = для всех точек

    public PriceListItem(String name, String category, BigDecimal price) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Название обязательно");
        this.name = name;
        this.category = category;
        this.price = Money.of(price, "Цена");
        this.active = true;
    }

    public String getName() { return name; }
    public String getCategory() { return category; }
    public BigDecimal getPrice() { return price; }
    public boolean isActive() { return active; }
    public Long getLocationId() { return locationId; }

    public void setPrice(BigDecimal price) { this.price = Money.of(price, "Цена"); }
    public void setActive(boolean active) { this.active = active; }
    public void setLocationId(Long locationId) { this.locationId = locationId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PriceListItem that = (PriceListItem) o;
        return active == that.active && Objects.equals(name, that.name) && Objects.equals(category, that.category) && Objects.equals(price, that.price) && Objects.equals(locationId, that.locationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, category, price, active, locationId);
    }
}
