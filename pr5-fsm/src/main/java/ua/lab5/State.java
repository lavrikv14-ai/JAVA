package ua.lab5;

/**
 * Стани скінченного автомата (див. діаграму).
 * S  - початковий стан,
 * S1 - знайдено "T",
 * S2 - знайдено "TE",
 * S3 - знайдено "TES",
 * F  - знайдено "TEST" (кінцевий стан, з нього автомат не виходить).
 */
public enum State {
    S("S"),
    S1("1"),
    S2("2"),
    S3("3"),
    F("F");

    private final String label;

    State(String label) {
        this.label = label;
    }

    /** Позначення стану так, як воно подано в умові ("S", "1", "2", "3", "F"). */
    public String label() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
