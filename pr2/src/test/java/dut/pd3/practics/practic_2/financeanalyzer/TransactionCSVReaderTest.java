package dut.pd3.practics.practic_2.financeanalyzer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionCSVReaderTest {

    @Test
    void shouldParseLinesAndSkipHeader() {
        List<String> lines = List.of(
                "Дата,Сума,Категорія",
                "05-12-2023,-450,Сільпо",
                "10-12-2023,8000,Зарплата");

        List<Transaction> result = TransactionCSVReader.parseTransactions(lines);

        assertEquals(2, result.size());
        assertEquals(new Transaction("05-12-2023", -450.0, "Сільпо"), result.get(0));
        assertEquals(8000.0, result.get(1).getAmount(), 1e-9);
    }

    @Test
    void shouldSkipInvalidAndBlankLines() {
        List<String> lines = List.of(
                "Дата,Сума,Категорія",
                "",
                "05-12-2023,abc,Сільпо",     // сума не число
                "2023-12-05,-100,Сільпо",    // неправильний формат дати
                "05-12-2023,-100",           // немає категорії
                "07-12-2023,-120,Аптека");

        List<Transaction> result = TransactionCSVReader.parseTransactions(lines);

        assertEquals(1, result.size());
        assertEquals("Аптека", result.get(0).getDescription());
    }

    @Test
    void shouldKeepCommaInsideDescription() {
        Transaction t = TransactionCSVReader.parseLine("18-12-2023,-200,Кав'ярня, центр").orElseThrow();
        assertEquals("Кав'ярня, центр", t.getDescription());
    }

    @Test
    void shouldReadTransactionsFromLocalFile(@TempDir Path dir) throws IOException {
        Path file = dir.resolve("test.csv");
        Files.writeString(file, """
                Дата,Сума,Категорія
                05-12-2023,-450,Сільпо
                07-12-2023,-120,Аптека
                10-12-2023,8000,Зарплата
                """, StandardCharsets.UTF_8);

        List<Transaction> result = TransactionCSVReader.readTransactions(file.toString());

        assertEquals(3, result.size());
        assertEquals("Сільпо", result.get(0).getDescription());
        assertEquals(7430.0, TransactionAnalyzer.calculateTotalBalance(result), 1e-9);
    }

    @Test
    void shouldThrowWhenFileMissing() {
        assertThrows(java.io.UncheckedIOException.class,
                () -> TransactionCSVReader.readTransactions("no/such/file.csv"));
    }
}
