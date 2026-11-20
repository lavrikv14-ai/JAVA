package ua.lab5;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        WordAutomaton automaton = new WordAutomaton();

        String[] examples = {"abcTESTabc", "abcTES", "TTEST", "TETEST", ""};
        for (String s : examples) {
            System.out.printf("\"%s\" -> %s%n", s, automaton.run(s));
        }

        System.out.println();
        System.out.print("Введіть рядок: ");
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println("\"" + line + "\" -> " + automaton.run(line));
        }
    }
}
