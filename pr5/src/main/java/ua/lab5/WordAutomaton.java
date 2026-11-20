package ua.lab5;


public class WordAutomaton {

    private static final char T = 'T';
    private static final char E = 'E';
    private static final char S_CHAR = 'S';


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

    public boolean contains(String input) {
        return run(input) == State.F;
    }
}
