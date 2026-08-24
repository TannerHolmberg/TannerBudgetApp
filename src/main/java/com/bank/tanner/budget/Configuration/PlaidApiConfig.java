package com.bank.tanner.budget.Configuration;

import com.bank.tanner.budget.Client.PlaidClient;
import com.bank.tanner.budget.Controller.PlaidController;
import com.bank.tanner.budget.Service.PlaidService;
import com.bank.tanner.budget.Service.UserTokensService;
import com.plaid.client.ApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class PlaidApiConfig {

    @Value("${PLAID_CLIENT_ID}")
    private String clientID;

    @Value("${plaid.production.secret}")
    private String secret;

    @Value("${user.id}")
    private String userId;

    @Bean
    public PlaidController plaidController(PlaidService plaidService){
        return new PlaidController(plaidService);
    }

    @Bean
    public PlaidService plaidService(PlaidClient plaidClient, UserTokensService userTokensService){
        return new PlaidService(plaidClient, userTokensService);
    }

    @Bean
    public PlaidClient plaidClient(ApiClient plaidApiClient){
        return new PlaidClient(plaidApiClient, userId);
    }

    @Bean
    public ApiClient plaidApiClient () {
        Map<String, String> apiKeys = Map.of(
                "clientId", clientID,
                "secret", secret
        );

        ApiClient plaidApiClient = new ApiClient(apiKeys);

        plaidApiClient.setPlaidAdapter(ApiClient.Production);

        return plaidApiClient;
    }
}
