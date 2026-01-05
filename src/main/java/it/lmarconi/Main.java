package it.lmarconi;

import it.lmarconi.model.CardTransaction;
import it.lmarconi.util.ExcelUtils;

import java.text.MessageFormat;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<CardTransaction> transactions = ExcelUtils.extractTransactionsFromExcel("C:/Users/Lorenzo/Documents/test-codice/ListaMovimenti.xlsx");
        //Printing excel results for initial tests about field formatting
        for (CardTransaction transaction : transactions) {
            System.out.println(MessageFormat.format("Transaction Date: {0}, Amount: {1}, Description: {2}",
                    transaction.getTransactionDate(), transaction.getAmount(), transaction.getDescription()));
        }
        System.out.println("Transactions: " + transactions.size());
    }
}