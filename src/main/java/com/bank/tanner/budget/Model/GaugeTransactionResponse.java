package com.bank.tanner.budget.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
public class GaugeTransactionResponse {
    private Map<String, Double> gaugeMap;
    private List<Transactions> uncategorizedTransactions;
    private Map<String, List<Transactions>> gaugeFilteredTxn;
    private Double moneyIn;
    private Double moneyOut;
}
