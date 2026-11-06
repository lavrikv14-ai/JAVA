package com.example.order.processing;

/**
 * Виключення, специфічне для узагальнень: через type erasure T невідомий під час
 * виконання, тому при "забрудненні купи" (raw types) тип перевіряється через Class&lt;T&gt;.
 */
public class UnsupportedProductTypeException extends RuntimeException {

    public UnsupportedProductTypeException(Class<?> expected, Class<?> actual) {
        super("Processor accepts " + expected.getSimpleName() + " but got " + actual.getSimpleName());
    }
}
