package com.bank.tanner.budget.Service;

import com.bank.tanner.budget.Model.Transactions;
import com.bank.tanner.budget.Repository.TransactionsRepository;

import java.time.LocalDate;
import java.util.List;

public class SupaBaseMgmt {

    TransactionsRepository transactionsRepository;
    LocalDate cutoffDate = LocalDate.now().minusDays(5);

    public SupaBaseMgmt(TransactionsRepository transactionsRepository){
        this.transactionsRepository = transactionsRepository;
    }
    //ensure no duplicate transactions
    public void ensureHistoricPendingMarkedRemoved(){
        List<Transactions> stalePendingTransactions =
                transactionsRepository.findByPendingTrueAndDateBefore(cutoffDate);

        for (Transactions staleTransaction : stalePendingTransactions){
            staleTransaction.setRemoved(true);
            transactionsRepository.save(staleTransaction);
        }
    }
}
