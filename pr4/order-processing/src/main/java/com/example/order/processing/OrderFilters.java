package com.example.order.processing;

import com.example.order.model.Order;
import com.example.order.model.OrderStatus;
import com.example.order.model.Product;

import java.math.BigDecimal;
import java.util.function.Predicate;

/**
 * Готові предикати для фільтрації замовлень (узагальнені статичні методи).
 */
public final class OrderFilters {

    private OrderFilters() {
    }

    public static <T extends Product> Predicate<Order<T>> totalAtLeast(BigDecimal min) {
        return order -> order.getTotalPrice().compareTo(min) >= 0;
    }

    public static <T extends Product> Predicate<Order<T>> withStatus(OrderStatus status) {
        return order -> order.getStatus() == status;
    }
}
