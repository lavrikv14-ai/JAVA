package com.example.order.storage;

import com.example.order.model.Order;
import com.example.order.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Сховище замовлень для товарів типу T.
 */
public interface OrderStorage<T extends Product> {

    void save(Order<T> order);

    Optional<Order<T>> findById(long id);

    List<Order<T>> findAll();

    List<Order<T>> findBy(Predicate<? super Order<T>> filter);

    int count();
}
