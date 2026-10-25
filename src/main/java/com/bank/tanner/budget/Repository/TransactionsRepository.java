package com.bank.tanner.budget.Repository;

import com.bank.tanner.budget.Model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TransactionsRepository extends JpaRepository<Transactions, String> {
    List<Transactions>  findByDateBetween(
            LocalDate startDate,
            LocalDate endDate
    );
}
