package dut.pd3.practics.practic_2.financeanalyzer;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Аналіз транзакцій (Задачі 2, 3, 5 + самостійна робота).
 * Абстрактний клас зі статичними методами; список транзакцій передається параметром.
 */
public abstract class TransactionAnalyzer {

    private static final DateTimeFormatter MONTH_YEAR = DateTimeFormatter.ofPattern("MM-yyyy");

    /** Задача 2: загальний баланс. */
    public static double calculateTotalBalance(List<Transaction> transactions) {
        return transactions.stream().mapToDouble(Transaction::getAmount).sum();
    }

    /** Задача 3: кількість транзакцій за місяць у форматі "MM-yyyy". */
    public static int countTransactionsByMonth(List<Transaction> transactions, String monthYear) {
        YearMonth target = YearMonth.parse(monthYear, MONTH_YEAR);
        return (int) transactions.stream()
                .filter(t -> YearMonth.from(t.getLocalDate()).equals(target))
                .count();
    }

    /** Задача 5: 10 найбільших витрат. */
    public static List<Transaction> findTopExpenses(List<Transaction> transactions) {
        return findTopExpenses(transactions, 10);
    }

    public static List<Transaction> findTopExpenses(List<Transaction> transactions, int limit) {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)                          // лише витрати
                .sorted(Comparator.comparingDouble(Transaction::getAmount)) // від найбільшої за модулем
                .limit(limit)
                .toList();
    }

    /** Самостійна: найбільша і найменша витрати за період [from; to] включно. */
    public static Optional<ExpenseExtremes> findExtremeExpenses(List<Transaction> transactions,
                                                                LocalDate from, LocalDate to) {
        List<Transaction> expenses = transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .filter(t -> {
                    LocalDate d = t.getLocalDate();
                    return !d.isBefore(from) && !d.isAfter(to);
                })
                .toList();
        if (expenses.isEmpty()) {
            return Optional.empty();
        }
        Transaction largest = expenses.stream().min(Comparator.comparingDouble(Transaction::getAmount)).get();
        Transaction smallest = expenses.stream().max(Comparator.comparingDouble(Transaction::getAmount)).get();
        return Optional.of(new ExpenseExtremes(largest, smallest));
    }

    /** Задача 5 (аналіз): сумарні витрати (додатні числа) по категоріях, від найбільшої до найменшої. */
    public static Map<String, Double> sumExpensesByCategory(List<Transaction> transactions) {
        Map<String, Double> sums = transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .collect(Collectors.groupingBy(Transaction::getDescription,
                        Collectors.summingDouble(t -> -t.getAmount())));
        return sums.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (a, b) -> a, LinkedHashMap::new));
    }

    /** Самостійна: сумарні витрати по місяцях; ключ "yyyy-MM", у хронологічному порядку. */
    public static Map<String, Double> sumExpensesByMonth(List<Transaction> transactions) {
        Map<YearMonth, Double> sums = new TreeMap<>();
        for (Transaction t : transactions) {
            if (t.getAmount() < 0) {
                sums.merge(YearMonth.from(t.getLocalDate()), -t.getAmount(), Double::sum);
            }
        }
        Map<String, Double> result = new LinkedHashMap<>();
        sums.forEach((month, sum) -> result.put(month.toString(), sum));
        return result;
    }
}
