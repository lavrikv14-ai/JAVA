package com.example.calculator;

/**
 * Власне виключення для некоректних вхідних даних
 * (наприклад, NaN, нескінченність, від'ємне число під коренем, текст замість числа).
 */
public class InvalidInputException extends Exception {

    public InvalidInputException(String message) {
        super(message);
    }
}
