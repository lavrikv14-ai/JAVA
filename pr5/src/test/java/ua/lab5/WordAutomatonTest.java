package ua.lab5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class WordAutomatonTest {

    private final WordAutomaton automaton = new WordAutomaton();

    // ---------- Завдання 2: функціональні кейси ----------

    @ParameterizedTest(name = "\"{0}\" -> {1}")
    @DisplayName("Кінцевий стан для різних рядків")
    @CsvSource({
            // приклади з умови
            "abcTESTabc, F",
            "abcTES,     S3",
            // слово на початку / в кінці / посередині / єдине
            "TEST,       F",
            "TESTabc,    F",
            "abcTEST,    F",
            "xxTESTyy,   F",
            // автомат зупиняється в проміжних станах
            "T,          S1",
            "TE,         S2",
            "TES,        S3",
            "abcT,       S1",
            "abcTE,      S2",
            "abcTES,     S3",
            // слова немає, стан S
            "abc,        S",
            "abcdef,     S",
            "EST,        S1",
            "TXST,       S1",
            "TEXT,       S1",
            "TESX,       S",
            "TESTx,      F",
            // регістр має значення
            "test,       S",
            "Test,       S",
            "tEST,       S1",
            "TEst,       S",
            // повторне входження та символи після F
            "TESTTEST,   F",
            "TESTEST,    F",
            "TESTxxxxx,  F",
            // пробіли та спецсимволи
            "T E S T,    S1",
            "'TEST ',    F",
            "'  TEST',   F",
            "TEST!@#,    F",
            "12345,      S"
    })
    void shouldReturnExpectedFinalState(String input, State expected) {
        assertEquals(expected, automaton.run(input));
    }

    @ParameterizedTest(name = "порожній/null: \"{0}\"")
    @NullAndEmptySource
    @DisplayName("null кидає виняток, порожній рядок дає S")
    void shouldHandleNullAndEmpty(String input) {
        if (input == null) {
            assertThrows(IllegalArgumentException.class, () -> automaton.run(null));
        } else {
            assertEquals(State.S, automaton.run(input));
        }
    }

    @ParameterizedTest(name = "contains(\"{0}\") == true")
    @ValueSource(strings = {"TEST", "abcTESTabc", "TESTTEST", "1TEST2"})
    void containsShouldBeTrueWhenWordPresent(String input) {
        assertTrue(automaton.contains(input));
    }

    @ParameterizedTest(name = "contains(\"{0}\") == false")
    @ValueSource(strings = {"", "T", "TES", "tEsT", "TEXT", "abc"})
    void containsShouldBeFalseWhenWordAbsent(String input) {
        assertFalse(automaton.contains(input));
    }

    // ---------- Тести окремих переходів (за діаграмою) ----------

    @ParameterizedTest(name = "{0} --{1}--> {2}")
    @CsvSource({
            "S,  T, S1",
            "S,  a, S",
            "S,  E, S",
            "S1, E, S2",
            "S1, a, S",
            "S1, S, S",
            "S2, S, S3",
            "S2, a, S",
            "S2, E, S",
            "S3, T, F",
            "S3, a, S",
            "S3, E, S"
    })
    @DisplayName("Переходи між станами")
    void shouldMakeCorrectTransition(State from, char symbol, State expected) {
        assertEquals(expected, automaton.next(from, symbol));
    }

    @ParameterizedTest(name = "F --{0}--> F")
    @ValueSource(chars = {'T', 'E', 'S', 'a', ' ', '1', '*'})
    @DisplayName("Зі стану F автомат не виходить (перехід *)")
    void finalStateShouldBeAbsorbing(char symbol) {
        assertEquals(State.F, automaton.next(State.F, symbol));
    }

    @ParameterizedTest
    @EnumSource(State.class)
    @DisplayName("Кожен стан має коректну мітку")
    void everyStateHasLabel(State state) {
        assertNotNull(state.label());
        assertEquals(state.label(), state.toString());
    }

    @Test
    @DisplayName("Довгий рядок з одним входженням")
    void shouldWorkOnLongInput() {
        String input = "a".repeat(100_000) + "TEST" + "b".repeat(100_000);
        assertEquals(State.F, automaton.run(input));
    }

    // ---------- Завдання 3*: рядки, на яких початковий автомат помилявся ----------

    @ParameterizedTest(name = "\"{0}\" -> F (виправлений автомат)")
    @DisplayName("Рядки з 'T' на місці помилкового символу")
    @ValueSource(strings = {
            "TTEST",        // T у стані 1: має лишитися в 1, а не йти в S
            "TTTEST",
            "TETEST",       // T у стані 2: має перейти в 1
            "TESTEST",
            "TETTEST",
            "TETETEST",
            "abcTTESTabc",
            "abcTETESTabc",
            "TTETEST",
            "TETTEST"
    })
    void fixedAutomatonShouldFindWordAfterRestart(String input) {
        assertEquals(State.F, automaton.run(input));
    }

    @ParameterizedTest(name = "\"{0}\" -> {1}")
    @DisplayName("Перекриття префіксів: проміжні стани після рестарту")
    @CsvSource({
            "TT,     S1",
            "TTE,    S2",
            "TTES,   S3",
            "TET,    S1",
            "TETE,   S2",
            "TETES,  S3",
            "TETEST, F"
    })
    void fixedAutomatonShouldRestartFromStateOne(String input, State expected) {
        assertEquals(expected, automaton.run(input));
    }

    // ---------- Демонстрація помилки в початковій реалізації ----------

    @ParameterizedTest(name = "наївний автомат: \"{0}\" НЕ дає F")
    @DisplayName("Початковий автомат (за діаграмою) помиляється")
    @ValueSource(strings = {"TTEST", "TETEST", "abcTTESTabc", "abcTETESTabc"})
    void naiveAutomatonFailsOnOverlappingInput(String input) {
        NaiveWordAutomaton naive = new NaiveWordAutomaton();
        assertNotEquals(State.F, naive.run(input));
        assertEquals(State.F, automaton.run(input));
    }
}
