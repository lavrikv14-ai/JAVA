package com.example.order.model.validation;

import com.example.order.model.InvalidProductException;
import com.example.order.model.Product;

import java.math.BigDecimal;

/**
 * Валідатор товарів. Пакет експортується лише модулю order.processing.
 */
public final class ProductValidator {

    private ProductValidator() {
    }

    public static <T extends Product> T validate(T product) {
        if (product == null) {
            throw new InvalidProductException("Product is null");
        }
        if (product.getName() == null || product.getName().isBlank()) {
            throw new InvalidProductException("Product name is blank");
        }
        BigDecimal price = product.getPrice();
        if (price == null || price.signum() <= 0) {
            throw new InvalidProductException("Invalid price for '" + product.getName() + "': " + price);
        }
        return product;
    }
}
