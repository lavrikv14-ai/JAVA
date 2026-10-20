package dut.pd3.practics.practic_2.financeanalyzer;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TransactionReportGeneratorTest {

    private static long stars(String chart, String labelPrefix) {
        return chart.lines()
                .filter(l -> l.startsWith(labelPrefix))
                .findFirst().orElseThrow()
                .chars().filter(c -> c == '*').count();
    }

    @Test
    void barChartScalesStarsByUnit() {
        Map<String, Double> data = new LinkedHashMap<>();
        data.put("A", 2000.0);
        data.put("B", 300.0);   // менше одиниці, але > 0 -> 1 символ
        data.put("C", 0.0);

        String chart = TransactionReportGenerator.buildBarChart("Тест", data, 1000.0);

        assertEquals(2, stars(chart, "A"));
        assertEquals(1, stars(chart, "B"));
        assertEquals(0, stars(chart, "C"));
        assertTrue(chart.contains("2000.00"));
    }
}
