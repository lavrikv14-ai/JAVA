package com.example.order.threading;

import com.example.order.model.Order;
import com.example.order.model.Product;
import com.example.order.processing.OrderProcessor;
import com.example.order.threading.internal.NamedThreadFactory;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Пул потоків для паралельної обробки замовлень.
 */
public class OrderThreadPool implements AutoCloseable {

    private final ExecutorService executor;

    public OrderThreadPool(int threads) {
        this.executor = Executors.newFixedThreadPool(threads, new NamedThreadFactory("order-worker"));
    }

    /** Відправляє всі замовлення на обробку. */
    public <T extends Product> List<CompletableFuture<Void>> dispatch(
            OrderProcessor<T> processor, Collection<Order<T>> orders) {
        return dispatch(processor, orders, order -> true);
    }

    /** Відправляє на обробку лише замовлення, що задовольняють фільтр (лямбда/Predicate). */
    public <T extends Product> List<CompletableFuture<Void>> dispatch(
            OrderProcessor<T> processor, Collection<Order<T>> orders, Predicate<? super Order<T>> filter) {

        Consumer<Order<T>> handler = processor::process; // посилання на метод

        return orders.stream()
                .filter(filter)
                .map(order -> CompletableFuture
                        .runAsync(() -> handler.accept(order), executor) // лямбда-вираз як Runnable
                        .exceptionally(ex -> {
                            Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                            System.err.printf("[%s] Order#%d error: %s%n",
                                    Thread.currentThread().getName(), order.getId(), cause.getMessage());
                            return null;
                        }))
                .toList();
    }

    /** Чекає завершення всіх задач. */
    public static void awaitAll(List<? extends CompletableFuture<?>> futures) {
        CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();
    }

    @Override
    public void close() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
