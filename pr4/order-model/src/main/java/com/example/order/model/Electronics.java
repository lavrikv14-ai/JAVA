package com.example.order.model;

import lombok.Builder;

import java.math.BigDecimal;

/**
 * Електроніка. Об'єкти створюються через Lombok-builder:
 * <pre>Electronics.builder().name("TV").price(BigDecimal.TEN).brand("LG").warrantyMonths(24).build()</pre>
 */
public class Electronics extends Product {

    private final String brand;
    private final int warrantyMonths;

    @Builder
    public Electronics(String name, BigDecimal price, String brand, int warrantyMonths) {
        super(name, price);
        this.brand = brand;
        this.warrantyMonths = warrantyMonths;
    }

    public String getBrand() {
        return brand;
    }

    public int getWarrantyMonths() {
        return warrantyMonths;
    }

    @Override
    public String getCategory() {
        return "Electronics";
    }

    @Override
    public String toString() {
        return "Electronics[" + getName() + ", brand=" + brand + ", warranty=" + warrantyMonths
                + "m, price=" + getPrice() + "]";
    }
}
