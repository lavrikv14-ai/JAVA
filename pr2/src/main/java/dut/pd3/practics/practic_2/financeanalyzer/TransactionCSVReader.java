package dut.pd3.practics.practic_2.financeanalyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Читання транзакцій з CSV (Задача 1 + 1.2).
 * Абстрактний клас лише зі статичними методами - створити екземпляр неможливо.
 *
 * Процес розділено на дві незалежні частини:
 *  1) отримання рядків з джерела (URL або локальний файл) - {@link #readLines(String)};
 *  2) розбір рядків у {@link Transaction} - {@link #parseTransactions(List)}.
 * Щоб додати нове джерело, достатньо змінити лише п.1; логіка розбору лишається без змін.
 */
public abstract class TransactionCSVReader {

    private static final String SEPARATOR = ",";

    /** Читає транзакції з URL (http/https/file) або зі шляху до локального файлу. */
    public static List<Transaction> readTransactions(String location) {
        return parseTransactions(readLines(location));
    }

    /** Частина 1: читання даних, незалежно від джерела. */
    public static List<String> readLines(String location) {
        try (BufferedReader br = openReader(location)) {
            return br.lines().toList();
        } catch (IOException e) {
            throw new UncheckedIOException("Не вдалося прочитати: " + location, e);
        }
    }

    /** Частина 2: обробка даних. Перший рядок - заголовок, пропускається. */
    public static List<Transaction> parseTransactions(List<String> lines) {
        List<Transaction> result = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.isBlank()) {
                continue;
            }
            Optional<Transaction> parsed = parseLine(line);
            if (parsed.isPresent()) {
                result.add(parsed.get());
            } else {
                System.err.println("Пропущено некоректний рядок " + (i + 1) + ": " + line);
            }
        }
        return result;
    }

    /** Розбір одного рядка "дата,сума,категорія". Некоректний рядок -> Optional.empty(). */
    public static Optional<Transaction> parseLine(String line) {
        // limit = 3: кома в назві категорії не ламає розбір
        String[] values = line.split(SEPARATOR, 3);
        if (values.length < 3) {
            return Optional.empty();
        }
        try {
            String date = values[0].trim();
            Transaction.DATE_FORMAT.parse(date); // перевірка формату дати
            double amount = Double.parseDouble(values[1].trim());
            return Optional.of(new Transaction(date, amount, values[2].trim()));
        } catch (NumberFormatException | DateTimeParseException e) {
            return Optional.empty();
        }
    }

    private static BufferedReader openReader(String location) throws IOException {
        boolean isUrl = location.startsWith("http://")
                || location.startsWith("https://")
                || location.startsWith("file:");
        var stream = isUrl
                ? URI.create(location).toURL().openStream()
                : Files.newInputStream(Path.of(location));
        return new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
    }
}
