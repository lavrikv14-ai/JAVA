package com.example.order.processing;

/**
 * Помилка під час обробки конкретного замовлення.
 */
public class OrderProcessingException extends RuntimeException {

    private final long orderId;

    public OrderProcessingException(long orderId, String message, Throwable cause) {
        super("Order#" + orderId + ": " + message, cause);
        this.orderId = orderId;
    }

    public long getOrderId() {
        return orderId;
    }
}
