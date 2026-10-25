package com.bank.tanner.budget.Controller;


import com.bank.tanner.budget.Model.GaugeTransactionResponse;
import com.bank.tanner.budget.Model.Transactions;
import com.bank.tanner.budget.Service.PlaidService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/plaid")
@CrossOrigin(origins = "http://localhost:5174")
public class PlaidController {

    private final PlaidService plaidService;

    public PlaidController(PlaidService plaidService){
        this.plaidService = plaidService;
    }

    @PostMapping("/link-token")
    public Map<String, String> createLinkToken() throws IOException {
        String linkToken = plaidService.createLinkToken();

        return Map.of("link_token", linkToken);
    }

    @PostMapping("/exchange-token")
    public boolean createAuthToken(@RequestParam String publicToken) throws IOException {
        return plaidService.exchangePublicToken(publicToken);
    }

    @GetMapping("/get-transaction-data")
    public ResponseEntity<GaugeTransactionResponse> getTransactionData() throws IOException {
        GaugeTransactionResponse response =
                plaidService.getTransactionData();

        return ResponseEntity.ok(response);
    }

}
