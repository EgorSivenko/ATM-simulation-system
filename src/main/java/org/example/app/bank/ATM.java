package org.example.app.bank;

import org.example.app.utils.UserInput;
import org.example.app.bank.BankCard.CardIssuer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;

import static org.example.app.utils.AppConstants.*;

public class ATM {
    private final CardIssuer bankName;

    public ATM(CardIssuer bankName) {
        if (bankName == null)
            throw new IllegalArgumentException("Null was passed as an argument for the bank name.");

        this.bankName = bankName;
    }

    public void run(BankCard bankCard) {
        if (bankCard == null)
            throw new IllegalArgumentException("Null was passed as an argument for the bank card.");

        System.out.printf("%nWelcome to \"%s\" ATM.%n", bankName.getLabel());
        System.out.print("Enter your card PIN code: ");

        int attemptsLeft = ATTEMPTS_AMOUNT;

        while (true) {
            String enteredPin = UserInput.readCardPin();

            if (!isPinCorrect(enteredPin, bankCard.getPin())) {
                attemptsLeft--;
                if (attemptsLeft == ZERO) {
                    System.out.println("The maximum number of PIN entry attempts has been exceeded. Access to your bank account is temporarily restricted. For further actions, contact technical support.");
                    return;
                }
                System.out.printf("You entered the incorrect PIN code. You have %d attempt(s) left. Try again: ", attemptsLeft);
            }
            else break;
        }

        BigDecimal percentage = bankCard.getIssuer() != bankName ? COMMISSION_PERCENTAGE : BigDecimal.ZERO;

        while (true) {
            System.out.println("\nMENU");
            System.out.println("1. Top up your bank account.");
            System.out.println("2. Withdraw money from your bank account.");
            System.out.println("3. Check bank account balance.");
            System.out.println("4. View bank account information.");
            System.out.println("5. Exit.");
            System.out.print("\nChoose the option: ");

            switch (UserInput.readOption()) {
                case 1 -> {
                    while (true) {
                        System.out.print("Please, enter an amount to top up: ");
                        BigDecimal amount = BigDecimal.valueOf(UserInput.readAmount());
                        BigDecimal amountWithCommission;

                        try {
                            amountWithCommission = calculateWithCommission(amount, percentage);
                            bankCard.deposit(amountWithCommission);
                        } catch (IllegalArgumentException ex) {
                            System.out.println(ex.getMessage());
                            continue;
                        }

                        simulateTask();
                        System.out.println("Your balance was successfully topped up by " + setBigDecimalScale(amountWithCommission) + "$ with " + percentage + "% commission.");
                        System.out.println("\nYour current balance: " + setBigDecimalScale(bankCard.getBalance()) + "$.");
                        break;
                    }
                }
                case 2 -> {
                    while (true) {
                        if (bankCard.getBalance().compareTo(BigDecimal.ZERO) == 0) {
                            System.out.println("\nWithdrawal isn't available while balance is 0.00$.");
                            break;
                        }

                        System.out.print("Please, enter an amount to withdraw: ");
                        BigDecimal amount = BigDecimal.valueOf(UserInput.readAmount());
                        BigDecimal amountWithCommission;

                        try {
                            amountWithCommission = calculateWithCommission(amount, percentage);
                            bankCard.withdraw(amount);
                        } catch (IllegalArgumentException | IllegalStateException ex) {
                            System.out.println(ex.getMessage());
                            continue;
                        }

                        simulateTask();
                        System.out.println("You successfully withdrew " + setBigDecimalScale(amountWithCommission) + "$ with " + percentage + "% commission from your account.");
                        System.out.println("Your current balance: " + setBigDecimalScale(bankCard.getBalance()) + "$.");
                        break;
                    }
                }
                case 3 -> {
                    simulateTask();
                    System.out.println("Your bank account balance: " + setBigDecimalScale(bankCard.getBalance()) + "$.");
                }
                case 4 -> {
                    simulateTask();
                    System.out.println(bankCard);
                }
                case 5 -> {
                    System.out.printf("%nThank you for using \"%s\" ATM.%n", bankName.getLabel());
                    return;
                }
                default -> {
                    System.out.println("Wrong option. Try again.");
                    continue;
                }
            }

            System.out.print("\nPress any button to proceed. ");
            UserInput.readInput();
        }
    }

    private boolean isPinCorrect(String enteredPin, String correctPin) {
        return enteredPin.equals(correctPin);
    }

    private BigDecimal calculateWithCommission(BigDecimal amount, BigDecimal percentage) {
        return amount.subtract(amount.multiply(percentage).divide(ONE_HUNDRED, 2, RoundingMode.HALF_EVEN));
    }

    private BigDecimal setBigDecimalScale(BigDecimal value) {
        return value.setScale(BIG_DECIMAL_SCALE, RoundingMode.HALF_EVEN);
    }

    private void simulateTask() {
        System.out.println("Please, wait...\n");
        try {
            int millis = ThreadLocalRandom.current().nextInt(ONE_SECOND_IN_MILLIS, TWO_SECONDS_IN_MILLIS);
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
