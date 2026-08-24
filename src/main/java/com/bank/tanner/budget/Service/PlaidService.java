package com.bank.tanner.budget.Service;

import com.bank.tanner.budget.Client.PlaidClient;

import java.io.IOException;

public class PlaidService {

    PlaidClient plaidClient;
    UserTokensService userTokensService;



    public PlaidService(PlaidClient plaidClient, UserTokensService userTokensService){
        this.plaidClient = plaidClient;
        this.userTokensService = userTokensService;
    }

    public String createLinkToken() throws IOException {
        return plaidClient.createLinkToken();
    }

    public boolean exchangePublicToken(String publicToken) throws IOException {
        String accessToken = plaidClient.exchangePublicToken(publicToken);

        if(accessToken == null){
            return false;
        }

        userTokensService.saveTokenIfNeeded(accessToken);

        return true;
    }
}
