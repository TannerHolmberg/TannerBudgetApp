package com.bank.tanner.budget.Client;

import com.plaid.client.ApiClient;
import com.plaid.client.model.*;
import com.plaid.client.request.PlaidApi;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import retrofit2.Response;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class PlaidClient {

    private static final Logger log = LogManager.getLogger(PlaidClient.class);
    static PlaidApi plaidApi;

    String userId;

    LinkTokenCreateRequestUser requestUser;
    LinkTokenCreateRequest request;

    public PlaidClient(ApiClient plaidApiClient, String userId){
        this.plaidApi = plaidApiClient.createService(PlaidApi.class);;
        this.userId = userId;
    }

    @PostConstruct
    public void init(){
        requestUser = new LinkTokenCreateRequestUser()
                        .clientUserId(userId);

         request = new LinkTokenCreateRequest()
                        .clientName("Tanner's Finance App")
                        .language("en")
                        .countryCodes(List.of(CountryCode.US))
                        .user(requestUser)
                        .products(List.of(Products.TRANSACTIONS));
    }

    public String createLinkToken() throws IOException {
        try {
            Response<LinkTokenCreateResponse> response =
                    plaidApi.linkTokenCreate(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body().getLinkToken();
            }
            if (response.errorBody() != null) {
                String errorBody = response.errorBody().string();
                System.out.println("PLAID ERROR BODY:" + errorBody);
            }
        }
        catch(IOException e){
            log.error("e: ", e);
        }
        return "Could not return link token";
    }

    public String exchangePublicToken(String publicToken) throws IOException {
        ItemPublicTokenExchangeRequest request =
                new ItemPublicTokenExchangeRequest()
                        .publicToken(publicToken);
        Response<ItemPublicTokenExchangeResponse> response =
                plaidApi.itemPublicTokenExchange(request).execute();
        if (response.isSuccessful() && response.body() != null) {
            return response.body().getAccessToken();
        }
        if (response.errorBody() != null) {
            String errorBody = response.errorBody().string();
            log.error("Plaid token exchange failed: {}", errorBody);
        }
        throw new IOException("Could not exchange public token");
    }

    public TransactionsSyncResponse getTransactions(String accessToken, String cursor) throws IOException {
        TransactionsSyncRequest request =
                new TransactionsSyncRequest()
                        .accessToken(accessToken)
                        .cursor(cursor)
                        .count(500);

        Response<TransactionsSyncResponse> response =
                plaidApi.transactionsSync(request).execute();

        if (!response.isSuccessful() || response.body() == null) {
            throw new IOException("Failed to get transactions");
        }

        return response.body();
    }

}
