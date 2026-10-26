package com.bank.tanner.budget.Configuration;

import com.bank.tanner.budget.Repository.TransactionsRepository;
import com.bank.tanner.budget.Service.SupaBaseMgmt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SupaBaseMgmtConfig {

    @Bean
    public SupaBaseMgmt supaBaseMgmt(
            TransactionsRepository transactionsRepository) {
        return new SupaBaseMgmt(transactionsRepository);
    }
}