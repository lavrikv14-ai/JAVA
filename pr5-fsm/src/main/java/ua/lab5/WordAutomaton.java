package ua.lab5;

/**
 * Скінченний автомат для розпізнавання наявності слова "TEST" у рядку.
 *
 * Автомат читає рядок символ за символом. Якщо символи закінчилися,
 * повертається поточний (кінцевий) стан. Стан F означає, що "TEST" у рядку є.
 *
 * Виправлення (завдання 3): у діаграмі з умови переходи !E (зі стану 1) та
 * !S (зі стану 2) ведуть у стан S. Але якщо "неправильний" символ сам є 'T',
 * то він може бути початком нового слова "TEST", тому в такому разі
 * перехід має бути не в S, а в стан 1.
 * Приклади, що ламали автомат: "TTEST", "TETEST", "abcTTESTabc".
 * Зі стану 3 для символу !T виправлення не потрібне: цей символ за
 * визначенням не 'T', тож нового початку слова в ньому немає.
 */
public class WordAutomaton {

    private static final char T = 'T';
    private static final char E = 'E';
    private static final char S_CHAR = 'S';

    /**
     * Робить один перехід автомата.
     *
     * @param state поточний стан
     * @param c     прийнятий символ
     * @return наступний стан
     */
    public State next(State state, char c) {
        switch (state) {
            case S:
                return (c == T) ? State.S1 : State.S;
            case S1:
                if (c == E) return State.S2;
                return (c == T) ? State.S1 : State.S;   // виправлення: "TT..."
            case S2:
                if (c == S_CHAR) return State.S3;
                return (c == T) ? State.S1 : State.S;   // виправлення: "TET..."
            case S3:
                return (c == T) ? State.F : State.S;
            case F:
                return State.F;                         // * -> F
            default:
                throw new IllegalStateException("Unknown state: " + state);
        }
    }

    /**
     * Проганяє рядок через автомат.
     *
     * @param input вхідний рядок (довільна кількість символів, у т.ч. порожній)
     * @return кінцевий стан автомата
     * @throws IllegalArgumentException якщо input == null
     */
    public State run(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Input string must not be null");
        }
        State state = State.S;
        for (int i = 0; i < input.length(); i++) {
            state = next(state, input.charAt(i));
            if (state == State.F) {
                break; // F поглинаючий стан, далі можна не читати
            }
        }
        return state;
    }

    /** Зручний метод: чи містить рядок слово "TEST". */
    public boolean contains(String input) {
        return run(input) == State.F;
    }
}
