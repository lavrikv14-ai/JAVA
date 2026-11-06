/**
 * Модуль зберігання замовлень.
 */
module order.storage {
    requires transitive order.model;

    // Експортуємо лише інтерфейс і фабрику.
    // Пакет com.example.order.storage.internal НЕ експортується -
    // реалізація недоступна з інших модулів.
    exports com.example.order.storage;
}
