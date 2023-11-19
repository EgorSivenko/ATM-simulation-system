package org.example.app;

import org.example.app.bank.ATM;
import org.example.app.bank.BankCard;
import org.example.app.bank.BankCard.CardIssuer;
import org.example.app.bank.BankCard.CardNetwork;

import java.math.BigDecimal;

public class App {
    public static void main(String[] args) {
        BankCard bankCard = new BankCard.BankCardBuilder(CardIssuer.AMERICAN_EXPRESS, CardNetwork.VISA, "4375 6759 2480 6971", "7594")
                .cardholderName("John Williams")
                .initialBalance(BigDecimal.valueOf(10_000))
                .cardName("Ultimate")
                .build();

        ATM atm = new ATM(CardIssuer.BANK_OF_AMERICA);
        atm.run(bankCard);
    }
}
