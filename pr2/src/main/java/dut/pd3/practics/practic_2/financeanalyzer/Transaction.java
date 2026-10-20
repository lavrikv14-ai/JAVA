package dut.pd3.practics.practic_2.financeanalyzer;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Модель банківської транзакції.
 * Lombok генерує getters/setters, equals/hashCode, toString та конструктор з усіма полями.
 */
@Data
@AllArgsConstructor
public class Transaction {

    /** Формат дати у CSV файлі. */
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private String date;
    private double amount;
    /** У CSV це колонка "Категорія". */
    private String description;

    /** Дата транзакції як LocalDate (парситься з рядка у форматі dd-MM-yyyy). */
    public LocalDate getLocalDate() {
        return LocalDate.parse(date, DATE_FORMAT);
    }
}
