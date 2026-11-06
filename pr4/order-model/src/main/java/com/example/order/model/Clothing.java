package com.example.order.model;

import lombok.Builder;

import java.math.BigDecimal;

/**
 * Одяг. Об'єкти створюються через Lombok-builder.
 */
public class Clothing extends Product {

    private final String size;
    private final String material;
    private final String color;

    @Builder
    public Clothing(String name, BigDecimal price, String size, String material, String color) {
        super(name, price);
        this.size = size;
        this.material = material;
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public String getMaterial() {
        return material;
    }

    public String getColor() {
        return color;
    }

    @Override
    public String getCategory() {
        return "Clothing";
    }

    @Override
    public String toString() {
        return "Clothing[" + getName() + ", size=" + size + ", material=" + material
                + ", color=" + color + ", price=" + getPrice() + "]";
    }
}
