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
    PlaidApi plaidApi;

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
                        .products(List.of(
                                Products.AUTH,
                                Products.TRANSACTIONS));
    }

    public String createLinkToken() throws IOException {
        try {
            Response<LinkTokenCreateResponse> response =
                    plaidApi.linkTokenCreate(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body().getLinkToken();
            }
        }
        catch(IOException e){
            log.error("e: ", e);
        }
        return "Could not return link token";
    }
}
