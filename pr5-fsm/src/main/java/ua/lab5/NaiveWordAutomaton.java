package ua.lab5;

/**
 * Автомат "у лоб" - рівно за діаграмою з умови (переходи !E, !S, !T ведуть у S).
 * Має помилку: на рядках на кшталт "TTEST" або "TETEST" не доходить до F.
 * Залишено лише для демонстрації помилки із завдання 3.
 * Для роботи використовуйте {@link WordAutomaton}.
 */
public class NaiveWordAutomaton {

    public State run(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Input string must not be null");
        }
        State state = State.S;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            switch (state) {
                case S:  state = (c == 'T') ? State.S1 : State.S; break;
                case S1: state = (c == 'E') ? State.S2 : State.S; break;   // !E -> S
                case S2: state = (c == 'S') ? State.S3 : State.S; break;   // !S -> S
                case S3: state = (c == 'T') ? State.F  : State.S; break;   // !T -> S
                case F:  break;                                            // * -> F
            }
        }
        return state;
    }
}
