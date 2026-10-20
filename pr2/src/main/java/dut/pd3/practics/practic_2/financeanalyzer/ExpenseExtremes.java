package dut.pd3.practics.practic_2.financeanalyzer;

import lombok.Value;

/**
 * Найбільша і найменша витрати за період.
 * Витрати від'ємні: "найбільша" - найбільша за модулем (найменше число), "найменша" - найближча до нуля.
 */
@Value
public class ExpenseExtremes {
    Transaction largest;
    Transaction smallest;
}
