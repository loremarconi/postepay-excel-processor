package it.lmarconi.mapper;

import it.lmarconi.model.CardTransaction;
import it.lmarconi.model.CardTransactionMonthly;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CardTransactionMonthlyMapper {

    public static List<CardTransactionMonthly> mapToCardTransactionMonthly(Map<LocalDate, List<CardTransaction>> transactionsByMonth) {
        List<CardTransactionMonthly> cardTransactionMonthlyList = new ArrayList<>();

        for (Map.Entry<LocalDate, List<CardTransaction>> transaction : transactionsByMonth.entrySet()) {
            CardTransactionMonthly cardTransactionMonthly = new CardTransactionMonthly();
            cardTransactionMonthly.setMonth(Month.of(transaction.getKey().getMonthValue()));
            cardTransactionMonthly.setTransactions(transaction.getValue());
            cardTransactionMonthlyList.add(cardTransactionMonthly);
        }
        return cardTransactionMonthlyList;
    }

}
