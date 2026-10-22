package main.java.entity.pricelist;

import main.java.entity.common.BaseEntity;
import main.java.entity.common.Money;
import java.math.BigDecimal;

public class PriceListItem extends BaseEntity {

    private String name;          // «Замена экрана iPhone 13»
    private String category;      // «Apple», «Экраны»
    private BigDecimal price;
    private boolean active;
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
}