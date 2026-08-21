package com.bank.tanner.budget.Service;

import com.bank.tanner.budget.Client.PlaidClient;

import java.io.IOException;

public class PlaidService {

    PlaidClient plaidClient;

    public PlaidService(PlaidClient plaidClient){
        this.plaidClient = plaidClient;
    }

    public String createLinkToken() throws IOException {
        return plaidClient.createLinkToken();
    }
}
