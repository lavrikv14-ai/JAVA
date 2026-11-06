package com.example.order.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Замовлення на товар типу T. Обмеження {@code T extends Product}
 * не дозволяє створити замовлення на довільний тип (наприклад, String).
 */
public class Order<T extends Product> {

    private static final AtomicLong SEQUENCE = new AtomicLong();

    private final long id = SEQUENCE.incrementAndGet();
    private final T product;
    private final int quantity;
    private final Instant createdAt = Instant.now();
    private volatile OrderStatus status = OrderStatus.NEW;

    public Order(T product, int quantity) {
        this.product = Objects.requireNonNull(product, "product must not be null");
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive: " + quantity);
        }
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public T getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalPrice() {
        BigDecimal price = product.getPrice() == null ? BigDecimal.ZERO : product.getPrice();
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    @Override
    public String toString() {
        return "Order#" + id + " [" + status + ", qty=" + quantity + ", total=" + getTotalPrice()
                + ", " + product + "]";
    }
}
