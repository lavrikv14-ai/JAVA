package com.example.order.processing;

import com.example.order.model.InvalidProductException;
import com.example.order.model.Order;
import com.example.order.model.OrderStatus;
import com.example.order.model.Product;
import com.example.order.model.validation.ProductValidator;
import com.example.order.storage.OrderStorage;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Обробник замовлень для товарів типу T (T обмежений типом Product).
 * Конвеєр обробки складається з кроків, заданих посиланнями на методи.
 */
public class OrderProcessor<T extends Product> {

    private final Class<T> productType;
    private final OrderStorage<T> storage;
    private final List<OrderStep<T>> pipeline;

    public OrderProcessor(Class<T> productType, OrderStorage<T> storage) {
        this.productType = Objects.requireNonNull(productType);
        this.storage = Objects.requireNonNull(storage);
        // посилання на методи як реалізація функціонального інтерфейсу
        this.pipeline = List.of(this::validate, this::pay, this::complete);
    }

    public static <T extends Product> OrderProcessor<T> of(Class<T> type, OrderStorage<T> storage) {
        return new OrderProcessor<>(type, storage);
    }

    /**
     * Обробляє замовлення. Якщо відбулась помилка - статус FAILED і виключення пробрасується.
     */
    public void process(Order<T> order) {
        checkType(order);
        order.setStatus(OrderStatus.PROCESSING);
        log(order, "start");
        try {
            pipeline.forEach(step -> step.apply(order)); // лямбда
        } catch (RuntimeException e) {
            order.setStatus(OrderStatus.FAILED);
            log(order, "FAILED: " + e.getMessage());
            throw e;
        }
    }

    private void checkType(Order<T> order) {
        Object product = order.getProduct();
        if (!productType.isInstance(product)) {
            throw new UnsupportedProductTypeException(productType, product.getClass());
        }
    }

    private void validate(Order<T> order) {
        try {
            ProductValidator.validate(order.getProduct());
        } catch (InvalidProductException e) {
            throw new OrderProcessingException(order.getId(), "validation failed - " + e.getMessage(), e);
        }
    }

    private void pay(Order<T> order) {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextLong(100, 300)); // імітація оплати
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new OrderProcessingException(order.getId(), "interrupted", e);
        }
    }

    private void complete(Order<T> order) {
        order.setStatus(OrderStatus.COMPLETED);
        storage.save(order);
        log(order, "completed, total=" + order.getTotalPrice());
    }

    private void log(Order<T> order, String message) {
        System.out.printf("[%s] Order#%d (%s): %s%n",
                Thread.currentThread().getName(), order.getId(),
                order.getProduct().getCategory(), message);
    }
}
