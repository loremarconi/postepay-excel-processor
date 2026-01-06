package it.lmarconi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Month;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CardTransactionMonthly {

    private Month month;
    private List<CardTransaction> transactions;

}
