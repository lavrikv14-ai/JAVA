package com.example.order.processing;

import com.example.order.model.Order;
import com.example.order.model.Product;

/**
 * Один крок конвеєра обробки. Функціональний інтерфейс -
 * реалізується лямбдою або посиланням на метод.
 */
@FunctionalInterface
public interface OrderStep<T extends Product> {

    void apply(Order<T> order);
}
