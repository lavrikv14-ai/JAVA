package com.example.calculator;

/**
 * Калькулятор з базовими арифметичними операціями.
 */
public class Calculator {

    public double add(double a, double b) throws InvalidInputException {
        validate(a, b);
        return checkResult(a + b);
    }

    public double subtract(double a, double b) throws InvalidInputException {
        validate(a, b);
        return checkResult(a - b);
    }

    public double multiply(double a, double b) throws InvalidInputException {
        validate(a, b);
        return checkResult(a * b);
    }

    /**
     * @throws ArithmeticException якщо дільник дорівнює нулю
     */
    public double divide(double a, double b) throws InvalidInputException {
        validate(a, b);
        if (b == 0) {
            throw new ArithmeticException("Ділення на нуль неможливе!");
        }
        return checkResult(a / b);
    }

    /**
     * Квадратний корінь. Число не може бути від'ємним.
     */
    public double sqrt(double a) throws InvalidInputException {
        validate(a);
        if (a < 0) {
            throw new InvalidInputException(
                    "Квадратний корінь з від'ємного числа (" + a + ") не існує.");
        }
        return Math.sqrt(a);
    }

    private void validate(double... values) throws InvalidInputException {
        for (double v : values) {
            if (Double.isNaN(v) || Double.isInfinite(v)) {
                throw new InvalidInputException("Некоректне число: " + v);
            }
        }
    }

    private double checkResult(double result) {
        if (Double.isInfinite(result) || Double.isNaN(result)) {
            throw new ArithmeticException("Переповнення: результат занадто великий.");
        }
        return result;
    }
}
