package dut.pd3.practics.practic_2.financeanalyzer;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Виведення звітів (Задача 4 + самостійна робота).
 * Абстрактний клас зі статичними методами.
 */
public abstract class TransactionReportGenerator {

    /** Один символ '*' у текстовій діаграмі = 1000 грн. */
    public static final double DEFAULT_UNIT = 1000.0;

    public static void printBalanceReport(double totalBalance) {
        System.out.println("Загальний баланс: " + totalBalance);
    }

    public static void printTransactionsCountByMonth(String monthYear, int count) {
        System.out.println("Кількість транзакцій за " + monthYear + ": " + count);
    }

    public static void printTopExpensesReport(List<Transaction> topExpenses) {
        System.out.println("10 найбільших витрат:");
        for (Transaction expense : topExpenses) {
            System.out.println(expense.getDescription() + ": " + expense.getAmount());
        }
    }

    public static void printExtremesReport(LocalDate from, LocalDate to, Optional<ExpenseExtremes> extremes) {
        System.out.println("Витрати за період " + from.format(Transaction.DATE_FORMAT)
                + " - " + to.format(Transaction.DATE_FORMAT) + ":");
        if (extremes.isEmpty()) {
            System.out.println("  витрат за цей період немає");
            return;
        }
        Transaction max = extremes.get().getLargest();
        Transaction min = extremes.get().getSmallest();
        System.out.println("  Найбільша: " + max.getDescription() + " " + max.getAmount() + " (" + max.getDate() + ")");
        System.out.println("  Найменша:  " + min.getDescription() + " " + min.getAmount() + " (" + min.getDate() + ")");
    }

    public static void printCategoryReport(Map<String, Double> byCategory) {
        System.out.print(buildBarChart("Витрати по категоріях", byCategory, DEFAULT_UNIT));
    }

    public static void printMonthlyReport(Map<String, Double> byMonth) {
        System.out.print(buildBarChart("Витрати по місяцях", byMonth, DEFAULT_UNIT));
    }

    /**
     * Текстова діаграма: кожен '*' = unit грн. Будь-яка ненульова сума має принаймні один символ.
     * Повертає рядок (а не друкує) - так метод легко тестувати.
     */
    public static String buildBarChart(String title, Map<String, Double> data, double unit) {
        StringBuilder sb = new StringBuilder();
        sb.append(title).append(" (1 символ = ").append((long) unit).append(" грн)\n");
        data.forEach((label, amount) -> {
            int stars = amount > 0 ? Math.max(1, (int) Math.round(amount / unit)) : 0;
            sb.append(String.format(Locale.ROOT, "%-22s | %-30s %10.2f%n", label, "*".repeat(stars), amount));
        });
        return sb.toString();
    }
}
