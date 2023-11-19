package org.example.app.bank;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class BankCard {
    private final CardIssuer issuer;
    private final CardNetwork network;
    private final String number;
    private final String pin;
    private final String name;
    private final String cardholderName;

    private BigDecimal balance;

    private BankCard(BankCardBuilder builder) {
        this.issuer = builder.issuer;
        this.network = builder.network;
        this.number = builder.number;
        this.pin = builder.pin;
        this.name = builder.name;
        this.cardholderName = builder.cardholderName;
        this.balance = builder.balance;
    }

    @Getter
    @AllArgsConstructor
    public enum CardIssuer {
        AMERICAN_EXPRESS("American Express"),
        CHASE("Chase"),
        CITI("Citi"),
        CAPITAL_ONE("Capital One"),
        BANK_OF_AMERICA("Bank of America"),
        DISCOVER("Discover"),
        WELLS_FARGO("Wells Fargo");

        private final String label;
    }

    @Getter
    @AllArgsConstructor
    public enum CardNetwork {
        VISA("Visa"),
        MASTERCARD("MasterCard"),
        AMERICAN_EXPRESS("American Express"),
        DINERS_CLUB("Diners Club"),
        DISCOVER("Discover");

        private final String label;
    }

    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("The amount to top up must be greater than 0.");

        balance = balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("The withdrawal amount must be greater than 0.");
        if (amount.compareTo(balance) > 0)
            throw new IllegalStateException("Insufficient funds on your bank account.");

        balance = balance.subtract(amount);
    }

    @Override
    public String toString() {
        return "Card issuer: " + issuer.label +
                "\nCard network: " + network.label +
                "\nCard number: " + maskCardNumber(number) +
                "\nCard name: " + name +
                "\nCardholder name: " + cardholderName;
    }

    private String maskCardNumber(String cardNumber) {
        int asteriskEndIndex = cardNumber.length() - 4;
        return "*".repeat(asteriskEndIndex) + cardNumber.substring(asteriskEndIndex);
    }

    public static class BankCardBuilder {
        private CardIssuer issuer;
        private CardNetwork network;
        private String number;
        private String pin;

        private String name = "Undefined";
        private String cardholderName = "Undefined";
        private BigDecimal balance = BigDecimal.ZERO;

        public BankCardBuilder(CardIssuer issuer, CardNetwork network, String number, String pin) {
            setIssuer(issuer);
            setNetwork(network);
            setNumber(number);
            setPin(pin);
        }

        private void setIssuer(CardIssuer issuer) {
            if (issuer == null)
                throw new IllegalArgumentException("Card Issuer cannot be null");

            this.issuer = issuer;
        }

        private void setNetwork(CardNetwork network) {
            if (network == null)
                throw new IllegalArgumentException("Card Network cannot be null");

            this.network = network;
        }

        private void setNumber(String number) {
            if (number == null)
                throw new IllegalArgumentException("Card Number cannot be null");

            String numberRegex;
            switch (network) {
                case VISA -> numberRegex = "^4[0-9]{12}(?:[0-9]{3})?$";
                case MASTERCARD -> numberRegex = "^(5[1-5][0-9]{14}|2(22[1-9][0-9]{12}|2[3-9][0-9]{13}|[3-6][0-9]{14}|7[0-1][0-9]{13}|720[0-9]{12}))$";
                case AMERICAN_EXPRESS -> numberRegex = "^3[47][0-9]{13}$";
                case DINERS_CLUB -> numberRegex = "^3(?:0[0-5]|[68][0-9])[0-9]{11}$";
                case DISCOVER -> numberRegex = "^65[4-9][0-9]{13}|64[4-9][0-9]{13}|6011[0-9]{12}|(622(?:12[6-9]|1[3-9][0-9]|[2-8][0-9][0-9]|9[01][0-9]|92[0-5])[0-9]{10})$";
                default -> numberRegex = "^[0-9]{4}[0-9]{4}[0-9]{4}[0-9]{4}$";
            }

            number = number.replaceAll("[_, -]", "");
            if (!number.matches(numberRegex))
                throw new IllegalArgumentException("Invalid card number");

            this.number = number;
        }

        private void setPin(String pin) {
            if (pin == null)
                throw new IllegalArgumentException("Card PIN cannot be null");

            String pinRegex = "^[0-9]{4}$";
            if (!pin.matches(pinRegex))
                throw new IllegalArgumentException("Invalid card PIN");

            this.pin = pin;
        }

        public BankCardBuilder cardName(String name) {
            if (name == null)
                throw new IllegalArgumentException("Card Name cannot be null");

            String regex = "^[A-Z][a-z]{2,16}$";
            if (!name.matches(regex))
                throw new IllegalArgumentException("Invalid card name");

            this.name = name;
            return this;
        }

        public BankCardBuilder cardholderName(String cardholderName) {
            if (cardholderName == null)
                throw new IllegalArgumentException("Cardholder’s Name cannot be null");

            String regex = "^[A-Z][a-z]{2,18} [A-Z][a-z]{2,18}$";
            if (!cardholderName.matches(regex))
                throw new IllegalArgumentException("Invalid cardholder's name");

            this.cardholderName = cardholderName;
            return this;
        }

        public BankCardBuilder initialBalance(BigDecimal balance) {
            if (balance == null)
                throw new IllegalArgumentException("Initial Balance cannot be null");

            if (balance.compareTo(BigDecimal.ZERO) < 0)
                throw new IllegalArgumentException("Card balance cannot be less than 0");

            this.balance = balance;
            return this;
        }

        public BankCard build() {
            return new BankCard(this);
        }
    }
}
