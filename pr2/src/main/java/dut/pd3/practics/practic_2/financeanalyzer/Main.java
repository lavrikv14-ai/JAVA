package dut.pd3.practics.practic_2.financeanalyzer;

import java.time.LocalDate;
import java.util.List;

public class Main {

    private static final String DEFAULT_PATH = "https://informer.com.ua/dut/java/pr2.csv";

    public static void main(String[] args) {
        // Джерело можна передати аргументом: URL або шлях до локального файлу
        String filePath = args.length > 0 ? args[0] : DEFAULT_PATH;
        List<Transaction> transactions = TransactionCSVReader.readTransactions(filePath);

        transactions.forEach(System.out::println);
        System.out.println();

        TransactionReportGenerator.printBalanceReport(
                TransactionAnalyzer.calculateTotalBalance(transactions));

        String monthYear = "01-2024";
        TransactionReportGenerator.printTransactionsCountByMonth(monthYear,
                TransactionAnalyzer.countTransactionsByMonth(transactions, monthYear));

        TransactionReportGenerator.printTopExpensesReport(
                TransactionAnalyzer.findTopExpenses(transactions));

        System.out.println();
        LocalDate from = LocalDate.of(2023, 12, 1);
        LocalDate to = LocalDate.of(2023, 12, 31);
        TransactionReportGenerator.printExtremesReport(from, to,
                TransactionAnalyzer.findExtremeExpenses(transactions, from, to));

        System.out.println();
        TransactionReportGenerator.printCategoryReport(
                TransactionAnalyzer.sumExpensesByCategory(transactions));
        System.out.println();
        TransactionReportGenerator.printMonthlyReport(
                TransactionAnalyzer.sumExpensesByMonth(transactions));
    }
}