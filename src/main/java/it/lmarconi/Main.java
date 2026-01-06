package it.lmarconi;

import it.lmarconi.model.CardTransaction;
import it.lmarconi.model.CardTransactionMonthlySum;
import it.lmarconi.service.CardTransactionService;
import it.lmarconi.util.ExcelUtils;

import java.text.MessageFormat;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<CardTransaction> transactions = ExcelUtils.extractTransactionsFromExcel("INSERT INPUT PATH HERE");
        CardTransactionService service = new CardTransactionService();
        List<CardTransactionMonthlySum> cardTransactionMonthlySumList = service.getMonthlyNetRemaining(transactions);
        for (CardTransactionMonthlySum monthlySum : cardTransactionMonthlySumList) {
            System.out.println(MessageFormat.format("Month: {0}, Net income: {1}", monthlySum.getMonth(), monthlySum.getMonthlyNetIncome()));
        }
        ExcelUtils.createAndPopulateExcelReport(cardTransactionMonthlySumList);
    }
}