/**
 * Модуль доменної моделі: товари та замовлення.
 */
module order.model {
    // Lombok потрібен лише під час компіляції (генерація builder'ів)
    requires static lombok;

    // публічне API моделі
    exports com.example.order.model;

    // кваліфікований експорт: валідатор бачить лише модуль order.processing
    exports com.example.order.model.validation to order.processing;
}
