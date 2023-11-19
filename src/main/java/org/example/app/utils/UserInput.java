package org.example.app.utils;

import lombok.experimental.UtilityClass;

import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class UserInput {
    private static final Scanner scanner = new Scanner(System.in).useLocale(Locale.US);

    public static String readCardPin() {
        while (true) {
            String pin = scanner.nextLine();
            Matcher matcher = Pattern.compile("\\d{4}").matcher(pin);
            if (matcher.matches())
                return pin;
            else
                System.out.print("Invalid card PIN. Try again: ");
        }
    }

    public static int readOption() {
        while (true) {
            try {
                String input = scanner.nextLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException ex) {
                System.out.print("Please, enter an integer number: ");
            }
        }
    }

    public static double readAmount() {
        while (true) {
            try {
                String input = scanner.nextLine();
                return Double.parseDouble(input);
            } catch (NumberFormatException ex) {
                System.out.print("Please, enter a decimal integer number: ");
            }
        }
    }

    public static void readInput() {
        scanner.nextLine();
    }
}
