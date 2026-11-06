/**
 * Модуль керування потоками.
 */
module order.threading {
    requires transitive order.processing;

    // internal-пакет (фабрика потоків) не експортується
    exports com.example.order.threading;
}
