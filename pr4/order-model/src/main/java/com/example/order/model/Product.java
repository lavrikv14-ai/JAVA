package com.example.order.model;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Базовий клас товару. Уся система працює лише з Product та його підтипами.
 */
public abstract class Product {

    private final UUID id = UUID.randomUUID();
    private final String name;
    private final BigDecimal price;

    protected Product(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    /** Назва категорії товару. */
    public abstract String getCategory();

    @Override
    public String toString() {
        return getCategory() + "[" + name + ", " + price + "]";
    }
}
