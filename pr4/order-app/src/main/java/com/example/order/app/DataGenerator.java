package com.example.order.app;

import com.example.order.model.Clothing;
import com.example.order.model.Electronics;
import com.example.order.model.Order;
import com.example.order.model.Product;
import com.github.javafaker.Faker;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Генерація тестових даних за допомогою JavaFaker + Lombok-builder'ів.
 */
public class DataGenerator {

    private final Faker faker = new Faker(Locale.ENGLISH);

    public Electronics electronics() {
        return Electronics.builder()
                .name(faker.commerce().productName())
                .price(randomPrice(50, 2500))
                .brand(faker.company().name())
                .warrantyMonths(faker.number().numberBetween(6, 37))
                .build();
    }

    public Clothing clothing() {
        return Clothing.builder()
                .name(faker.commerce().productName())
                .price(randomPrice(10, 300))
                .size(faker.options().option("XS", "S", "M", "L", "XL"))
                .material(faker.commerce().material())
                .color(faker.color().name())
                .build();
    }

    /** Генерує n замовлень для будь-якого підтипу Product. */
    public <T extends Product> List<Order<T>> orders(int n, Supplier<T> productSupplier) {
        return Stream.generate(productSupplier)
                .limit(n)
                .map(product -> new Order<>(product, faker.number().numberBetween(1, 4)))
                .toList();
    }

    private BigDecimal randomPrice(long min, long max) {
        return BigDecimal.valueOf(faker.number().randomDouble(2, min, max));
    }
}
