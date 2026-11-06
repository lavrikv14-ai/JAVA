/**
 * Модуль обробки замовлень.
 */
module order.processing {
    requires transitive order.model;
    requires transitive order.storage;

    exports com.example.order.processing;
}
