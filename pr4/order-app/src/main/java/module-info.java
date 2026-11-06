/**
 * Точка входу: збирає всі модулі разом.
 */
module order.app {
    requires order.threading; // order.processing, order.storage, order.model - транзитивно
    requires javafaker;       // automatic module (com.github.javafaker)
}
