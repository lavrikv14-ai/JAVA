package com.example.order.storage;

import com.example.order.model.Product;
import com.example.order.storage.internal.InMemoryOrderStorage;

/**
 * Фабрика сховищ - єдина точка доступу до внутрішньої реалізації.
 */
public final class OrderStorages {

    private OrderStorages() {
    }

    public static <T extends Product> OrderStorage<T> inMemory() {
        return new InMemoryOrderStorage<>();
    }
}
