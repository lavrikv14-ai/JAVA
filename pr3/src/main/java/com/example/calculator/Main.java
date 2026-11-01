package com.example.calculator;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        Calculator calculator = new Calculator();

        System.out.println("=== Калькулятор ===");
        System.out.println("Для виходу введіть 'q' замість першого числа.");

        while (true) {
            System.out.print("\nВведіть перше число: ");
            if (!scanner.hasNextLine()) {
                break;
            }
            String first = scanner.nextLine().trim();
            if (first.equalsIgnoreCase("q")) {
                break;
            }

            System.out.print("Введіть друге число (для sqrt не враховується): ");
            String second = scanner.nextLine().trim();

            System.out.print("Введіть операцію (+, -, *, /, sqrt): ");
            String operation = scanner.nextLine().trim();

            try {
                double result = calculate(first, second, operation, calculator);
                System.out.println("Результат: " + result);
            } catch (InvalidInputException e) {
                System.out.println("Помилка вводу: " + e.getMessage());
            } catch (ArithmeticException e) {
                System.out.println("Арифметична помилка: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Непередбачена помилка: " + e.getMessage());
            } finally {
                System.out.println("Обробку запиту завершено.");
            }
        }

        System.out.println("Роботу завершено. До побачення!");
        scanner.close();
    }

    /**
     * Виконує обчислення за рядковими вхідними даними.
     */
    public static double calculate(String first, String second, String operation, Calculator calc)
            throws InvalidInputException {
        if (operation == null) {
            throw new InvalidInputException("Операцію не вказано.");
        }
        String op = operation.trim().toLowerCase();

        if (op.equals("sqrt")) {
            return calc.sqrt(parseNumber(first));
        }

        double a = parseNumber(first);
        double b = parseNumber(second);

        switch (op) {
            case "+":
                return calc.add(a, b);
            case "-":
                return calc.subtract(a, b);
            case "*":
                return calc.multiply(a, b);
            case "/":
                return calc.divide(a, b);
            default:
                throw new InvalidInputException("Невідома операція: '" + operation + "'");
        }
    }

    public static double parseNumber(String text) throws InvalidInputException {
        if (text == null || text.isBlank()) {
            throw new InvalidInputException("Порожнє значення замість числа.");
        }
        try {
            return Double.parseDouble(text.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new InvalidInputException("'" + text + "' не є числом.");
        }
    }
}
