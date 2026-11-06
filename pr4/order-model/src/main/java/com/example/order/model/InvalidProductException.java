package com.example.order.model;

/**
 * Кидається, коли товар не пройшов валідацію.
 */
public class InvalidProductException extends RuntimeException {

    public InvalidProductException(String message) {
        super(message);
    }
}
