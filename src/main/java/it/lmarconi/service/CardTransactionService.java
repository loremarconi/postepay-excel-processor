package it.lmarconi.service;

import it.lmarconi.mapper.CardTransactionMonthlyMapper;
import it.lmarconi.model.CardTransaction;
import it.lmarconi.model.CardTransactionMonthly;
import it.lmarconi.model.CardTransactionMonthlySum;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class CardTransactionService {

    public List<CardTransactionMonthlySum> getMonthlyNetRemaining(List<CardTransaction> transactions) {
        List<CardTransactionMonthly> cardTransactionMonthlyList = splitTransactionsByMonth(transactions);

        List<CardTransactionMonthlySum> cardTransactionMonthlySumList = new ArrayList<>();
        for (CardTransactionMonthly cardTransactionMonthly : cardTransactionMonthlyList) {
            CardTransactionMonthlySum monthlySum = new CardTransactionMonthlySum();
            monthlySum.setMonth(cardTransactionMonthly.getMonth());
            monthlySum.setMonthlyNetIncome(sumAllMonthlyTransactions(cardTransactionMonthly.getTransactions()));
            cardTransactionMonthlySumList.add(monthlySum);
        }
        return cardTransactionMonthlySumList;
    }

    public List<CardTransactionMonthly> splitTransactionsByMonth(List<CardTransaction> transactions) {
        log.info("Splitting {} card transactions by month", transactions.size());
        Map<LocalDate, List<CardTransaction>> splitTransactions = transactions.stream()
                .collect(Collectors.groupingBy(t -> t.getTransactionDate().with(TemporalAdjusters.firstDayOfMonth())));
        log.info("Retrieved transactions for {} months", splitTransactions.size());
        return CardTransactionMonthlyMapper.mapToCardTransactionMonthly(splitTransactions);
    }

    private BigDecimal sumAllMonthlyTransactions(List<CardTransaction> transactions) {
        return transactions.stream()
                .map(CardTransaction::getAmount)
                .reduce(BigDecimal::add)
                .orElse(null);
    }
}
