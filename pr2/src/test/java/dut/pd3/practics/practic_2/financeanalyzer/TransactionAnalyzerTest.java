package dut.pd3.practics.practic_2.financeanalyzer;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TransactionAnalyzerTest {

    @Test
    void testCalculateTotalBalance() {
        List<Transaction> transactions = Arrays.asList(
                new Transaction("01-01-2023", 100.0, "Дохід"),
                new Transaction("02-01-2023", -50.0, "Витрата"),
                new Transaction("03-01-2023", 150.0, "Дохід"));

        assertEquals(200.0, TransactionAnalyzer.calculateTotalBalance(transactions), 1e-9,
                "Розрахунок загального балансу неправильний");
    }

    @Test
    void testCalculateTotalBalanceEmptyList() {
        assertEquals(0.0, TransactionAnalyzer.calculateTotalBalance(List.of()), 1e-9);
    }

    @Test
    void testCountTransactionsByMonth() {
        List<Transaction> transactions = Arrays.asList(
                new Transaction("01-02-2023", 50.0, "Дохід"),
                new Transaction("15-02-2023", -20.0, "Витрата"),
                new Transaction("05-03-2023", 100.0, "Дохід"));

        assertEquals(2, TransactionAnalyzer.countTransactionsByMonth(transactions, "02-2023"),
                "Кількість транзакцій за лютий неправильна");
        assertEquals(1, TransactionAnalyzer.countTransactionsByMonth(transactions, "03-2023"),
                "Кількість транзакцій за березень неправильна");
        assertEquals(0, TransactionAnalyzer.countTransactionsByMonth(transactions, "02-2024"));
    }

    @Test
    void testFindTopExpenses() {
        List<Transaction> transactions = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            transactions.add(new Transaction("10-01-2024", -100.0 * i, "Витрата " + i));
        }
        transactions.add(new Transaction("11-01-2024", 5000.0, "Зарплата"));

        List<Transaction> top = TransactionAnalyzer.findTopExpenses(transactions);

        assertEquals(10, top.size());
        assertEquals(-1200.0, top.get(0).getAmount(), 1e-9, "Перша має бути найбільша витрата");
        assertEquals(-300.0, top.get(9).getAmount(), 1e-9);
        assertTrue(top.stream().allMatch(t -> t.getAmount() < 0), "Доходи не мають потрапити у топ");
    }

    private static List<Transaction> decemberData() {
        return Arrays.asList(
                new Transaction("05-12-2023", -450.0, "Сільпо"),
                new Transaction("07-12-2023", -120.0, "Аптека"),
                new Transaction("10-12-2023", 8000.0, "Зарплата"),
                new Transaction("14-12-2023", -3200.0, "Оренда квартири"),
                new Transaction("18-12-2023", -200.0, "Кав'ярня"),
                new Transaction("24-12-2023", -950.0, "Ресторан"));
    }

    @Test
    void testFindExtremeExpensesInPeriod() {
        Optional<ExpenseExtremes> result = TransactionAnalyzer.findExtremeExpenses(
                decemberData(), LocalDate.of(2023, 12, 10), LocalDate.of(2023, 12, 31));

        assertTrue(result.isPresent());
        assertEquals(-3200.0, result.get().getLargest().getAmount(), 1e-9);
        assertEquals(-200.0, result.get().getSmallest().getAmount(), 1e-9);
    }

    @Test
    void testFindExtremeExpensesBoundariesInclusive() {
        Optional<ExpenseExtremes> result = TransactionAnalyzer.findExtremeExpenses(
                decemberData(), LocalDate.of(2023, 12, 5), LocalDate.of(2023, 12, 7));

        assertTrue(result.isPresent());
        assertEquals(-450.0, result.get().getLargest().getAmount(), 1e-9);
        assertEquals(-120.0, result.get().getSmallest().getAmount(), 1e-9);
    }

    @Test
    void testFindExtremeExpensesEmptyPeriod() {
        assertTrue(TransactionAnalyzer.findExtremeExpenses(
                decemberData(), LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 31)).isEmpty());
    }

    @Test
    void testSumExpensesByCategory() {
        List<Transaction> transactions = Arrays.asList(
                new Transaction("05-12-2023", -450.0, "Сільпо"),
                new Transaction("05-01-2024", -500.0, "Сільпо"),
                new Transaction("07-12-2023", -120.0, "Аптека"),
                new Transaction("10-12-2023", 8000.0, "Зарплата"));

        Map<String, Double> result = TransactionAnalyzer.sumExpensesByCategory(transactions);

        assertEquals(2, result.size(), "Доходи не входять у витрати");
        assertEquals(950.0, result.get("Сільпо"), 1e-9);
        assertEquals(120.0, result.get("Аптека"), 1e-9);
        assertEquals("Сільпо", result.keySet().iterator().next(), "Спочатку найбільша категорія");
    }

    @Test
    void testSumExpensesByMonth() {
        List<Transaction> transactions = Arrays.asList(
                new Transaction("05-01-2024", -500.0, "Сільпо"),
                new Transaction("05-12-2023", -450.0, "Сільпо"),
                new Transaction("14-12-2023", -3200.0, "Оренда квартири"),
                new Transaction("10-12-2023", 8000.0, "Зарплата"));

        Map<String, Double> result = TransactionAnalyzer.sumExpensesByMonth(transactions);

        assertEquals(List.of("2023-12", "2024-01"), List.copyOf(result.keySet()), "Хронологічний порядок");
        assertEquals(3650.0, result.get("2023-12"), 1e-9);
        assertEquals(500.0, result.get("2024-01"), 1e-9);
    }
}
