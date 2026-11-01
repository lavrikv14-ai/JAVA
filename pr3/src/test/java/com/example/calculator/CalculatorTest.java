package com.example.calculator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CalculatorTest {

    private Calculator calc;

    @BeforeEach
    void setUp() {
        calc = new Calculator();
    }

    // ---------- Базові операції ----------

    @Test
    void addWorks() throws Exception {
        assertEquals(5.0, calc.add(2, 3));
    }

    @Test
    void subtractWorks() throws Exception {
        assertEquals(-1.0, calc.subtract(2, 3));
    }

    @Test
    void multiplyWorks() throws Exception {
        assertEquals(6.0, calc.multiply(2, 3));
    }

    @Test
    void divideWorks() throws Exception {
        assertEquals(2.5, calc.divide(5, 2));
    }

    @Test
    void sqrtWorks() throws Exception {
        assertEquals(4.0, calc.sqrt(16));
        assertEquals(0.0, calc.sqrt(0));
    }

    // ---------- Ділення на нуль ----------

    @Test
    void divideByZeroThrowsArithmeticException() {
        assertThrows(ArithmeticException.class, () -> calc.divide(10, 0));
    }

    @Test
    void divideByNegativeZeroThrowsArithmeticException() {
        assertThrows(ArithmeticException.class, () -> calc.divide(10, -0.0));
    }

    @Test
    void divideByZeroThroughMainThrowsArithmeticException() {
        assertThrows(ArithmeticException.class, () -> Main.calculate("5", "0", "/", calc));
    }

    // ---------- Власне виключення InvalidInputException ----------

    @Test
    void sqrtOfNegativeThrowsInvalidInput() {
        assertThrows(InvalidInputException.class, () -> calc.sqrt(-4));
    }

    @Test
    void nanThrowsInvalidInput() {
        assertThrows(InvalidInputException.class, () -> calc.add(Double.NaN, 1));
        assertThrows(InvalidInputException.class, () -> calc.subtract(1, Double.NaN));
    }

    @Test
    void infinityThrowsInvalidInput() {
        assertThrows(InvalidInputException.class, () -> calc.multiply(Double.POSITIVE_INFINITY, 2));
        assertThrows(InvalidInputException.class, () -> calc.divide(1, Double.NEGATIVE_INFINITY));
    }

    @Test
    void overflowThrowsArithmeticException() {
        assertThrows(ArithmeticException.class, () -> calc.multiply(Double.MAX_VALUE, 2));
    }

    // ---------- Невірні вхідні дані (рядки замість чисел) ----------

    @Test
    void textInsteadOfNumberThrowsInvalidInput() {
        assertThrows(InvalidInputException.class, () -> Main.calculate("abc", "5", "+", calc));
        assertThrows(InvalidInputException.class, () -> Main.calculate("5", "xyz", "*", calc));
    }

    @Test
    void emptyInputThrowsInvalidInput() {
        assertThrows(InvalidInputException.class, () -> Main.calculate("", "5", "+", calc));
        assertThrows(InvalidInputException.class, () -> Main.calculate("5", "   ", "-", calc));
    }

    @Test
    void unknownOperationThrowsInvalidInput() {
        assertThrows(InvalidInputException.class, () -> Main.calculate("1", "2", "%", calc));
        assertThrows(InvalidInputException.class, () -> Main.calculate("1", "2", null, calc));
    }

    @Test
    void sqrtOfNegativeThroughMainThrowsInvalidInput() {
        assertThrows(InvalidInputException.class, () -> Main.calculate("-9", "0", "sqrt", calc));
    }

    // ---------- Допоміжні перевірки Main ----------

    @Test
    void commaIsAcceptedAsDecimalSeparator() throws Exception {
        assertEquals(3.5, Main.parseNumber("3,5"));
    }

    @Test
    void calculateWorksForAllOperations() throws Exception {
        assertEquals(7.0, Main.calculate("3", "4", "+", calc));
        assertEquals(-1.0, Main.calculate("3", "4", "-", calc));
        assertEquals(12.0, Main.calculate("3", "4", "*", calc));
        assertEquals(0.75, Main.calculate("3", "4", "/", calc));
        assertEquals(5.0, Main.calculate("25", "ignored", "sqrt", calc));
    }
}
