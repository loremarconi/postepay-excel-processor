package it.lmarconi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Month;

@Getter
@Setter
@NoArgsConstructor
public class CardTransactionMonthlySum {

    private Month month;
    private BigDecimal monthlyNetIncome;

}
