package com.example.order.storage.internal;

import com.example.order.model.Order;
import com.example.order.model.Product;
import com.example.order.storage.OrderStorage;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * Потокобезпечне сховище в пам'яті. Пакет не експортується з модуля.
 */
public class InMemoryOrderStorage<T extends Product> implements OrderStorage<T> {

    private final Map<Long, Order<T>> orders = new ConcurrentHashMap<>();

    @Override
    public void save(Order<T> order) {
        Objects.requireNonNull(order, "order must not be null");
        orders.put(order.getId(), order);
    }

    @Override
    public Optional<Order<T>> findById(long id) {
        return Optional.ofNullable(orders.get(id));
    }

    @Override
    public List<Order<T>> findAll() {
        return orders.values().stream()
                .sorted((a, b) -> Long.compare(a.getId(), b.getId()))
                .toList();
    }

    @Override
    public List<Order<T>> findBy(Predicate<? super Order<T>> filter) {
        return findAll().stream().filter(filter).toList();
    }

    @Override
    public int count() {
        return orders.size();
    }
}
