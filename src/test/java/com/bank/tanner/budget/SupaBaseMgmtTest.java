package com.bank.tanner.budget;

import com.bank.tanner.budget.Service.SupaBaseMgmt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SupaBaseMgmtTest {

    @Autowired
    private SupaBaseMgmt supaBaseMgmt;

    @Test
    void markHistoricPendingTransactionsRemoved() {
        supaBaseMgmt.ensureHistoricPendingMarkedRemoved();
    }
}