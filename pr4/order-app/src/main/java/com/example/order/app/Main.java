package com.example.order.app;

import com.example.order.model.Clothing;
import com.example.order.model.Electronics;
import com.example.order.model.Order;
import com.example.order.model.Product;
import com.example.order.processing.OrderFilters;
import com.example.order.processing.OrderProcessor;
import com.example.order.processing.UnsupportedProductTypeException;
import com.example.order.storage.OrderStorage;
import com.example.order.storage.OrderStorages;
import com.example.order.threading.OrderThreadPool;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Main {

    public static void main(String[] args) {
        DataGenerator generator = new DataGenerator();

        // 1. Сховища та обробники для кожного типу товарів (Generics)
        OrderStorage<Electronics> electronicsStorage = OrderStorages.inMemory();
        OrderStorage<Clothing> clothingStorage = OrderStorages.inMemory();
        OrderProcessor<Electronics> electronicsProcessor = OrderProcessor.of(Electronics.class, electronicsStorage);
        OrderProcessor<Clothing> clothingProcessor = OrderProcessor.of(Clothing.class, clothingStorage);

        // 2. Дані (JavaFaker + Lombok @Builder)
        List<Order<Electronics>> electronicsOrders = new ArrayList<>(generator.orders(6, generator::electronics));
        List<Order<Clothing>> clothingOrders = generator.orders(6, generator::clothing);

        // навмисно некоректний товар (від'ємна ціна) - замовлення має бути відхилене
        Electronics broken = Electronics.builder()
                .name("Broken gadget").price(new BigDecimal("-5")).brand("NoName").warrantyMonths(0).build();
        electronicsOrders.add(new Order<>(broken, 1));

        // 3. Багатопотокова обробка (лямбда, method reference, фільтр за сумою)
        System.out.println("=== Обробка замовлень ===");
        try (OrderThreadPool pool = new OrderThreadPool(4)) {
            List<CompletableFuture<Void>> futures = new ArrayList<>();
            futures.addAll(pool.dispatch(electronicsProcessor, electronicsOrders));
            // одяг - лише замовлення від 30 одиниць
            futures.addAll(pool.dispatch(clothingProcessor, clothingOrders,
                    OrderFilters.totalAtLeast(new BigDecimal("30"))));
            OrderThreadPool.awaitAll(futures);
        }

        // 4. Результати
        System.out.println("\n=== Результати ===");
        print("Electronics", electronicsStorage);
        print("Clothing", clothingStorage);

        // 5. Виключення, специфічне для узагальнень (raw type обходить перевірку компілятора)
        System.out.println("\n=== Демонстрація UnsupportedProductTypeException ===");
        demoGenericsViolation(clothingProcessor, electronicsOrders.get(0));
    }

    private static <T extends Product> void print(String title, OrderStorage<T> storage) {
        System.out.println(title + " (" + storage.count() + " оброблено):");
        storage.findAll().forEach(System.out::println);
        BigDecimal sum = storage.findAll().stream()
                .map(Order::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("Загальна сума: " + sum);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void demoGenericsViolation(OrderProcessor<Clothing> processor, Order<Electronics> order) {
        OrderProcessor raw = processor; // heap pollution
        try {
            raw.process(order);
        } catch (UnsupportedProductTypeException e) {
            System.out.println("Перехоплено: " + e.getMessage());
        }
    }
}
