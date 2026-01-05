package it.lmarconi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class CardTransaction {

    private LocalDate transactionDate; //Data transazione, quella che interessa a noi per i calcoli sulle spese
    private LocalDate effectiveCreditDate; //Data accredito
    private BigDecimal amount;
    private String description;

}
