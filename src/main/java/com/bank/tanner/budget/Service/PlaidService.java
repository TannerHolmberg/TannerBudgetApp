package com.bank.tanner.budget.Service;

import com.bank.tanner.budget.Client.PlaidClient;
import com.bank.tanner.budget.Model.Gauge;
import com.bank.tanner.budget.Model.GaugeTransactionResponse;
import com.bank.tanner.budget.Model.Transactions;
import com.bank.tanner.budget.Repository.GaugeRepository;
import com.bank.tanner.budget.Repository.TransactionsRepository;
import com.plaid.client.model.RemovedTransaction;
import com.plaid.client.model.Transaction;
import com.plaid.client.model.TransactionsSyncResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class PlaidService {

    PlaidClient plaidClient;
    UserTokensService userTokensService;
    TransactionsRepository transactionsRepository;
    String userId;
    GaugeRepository gaugeRepository;
    Double moneyOut = 0.0;
    Double moneyIn = 0.0;


    public PlaidService(PlaidClient plaidClient, UserTokensService userTokensService,
                        TransactionsRepository transactionsRepository, String userId,
                        GaugeRepository gaugeRepository){
        this.plaidClient = plaidClient;
        this.userTokensService = userTokensService;
        this.transactionsRepository = transactionsRepository;
        this.userId = userId;
        this.gaugeRepository = gaugeRepository;
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

    public GaugeTransactionResponse getTransactionData() throws IOException {

        String accessToken = userTokensService.getAccessToken();
        String cursor = userTokensService.getCursor();

        if (accessToken == null) {
            throw new IllegalStateException("Access token does not exist");
        }

        TransactionsSyncResponse syncResponse =
                plaidClient.getTransactions(accessToken, cursor);

        String nextCursor = syncResponse.getNextCursor();
        userTokensService.setCursor(nextCursor);

        List<Transaction> plaidTransactions =
                syncResponse.getAdded();

        List<RemovedTransaction> removedTransactions =
                syncResponse.getRemoved();

        markRemovedTransactions(removedTransactions);

        saveTransactions(plaidTransactions);
        moneyIn = 0.0;
        moneyOut = 0.0;

        GaugeTransactionResponse categorizedTransactions = categorizeTransactions();

        return categorizedTransactions;
    }

    public GaugeTransactionResponse categorizeTransactions(){
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate startOfNextMonth = startOfMonth.plusMonths(1);

        List<Transactions> transactions =
                transactionsRepository.findByDateBetweenAndRemovedFalse(
                        startOfMonth,
                        startOfNextMonth.minusDays(1)
                );

        generateMoneyInAndOut(transactions);

        List<Gauge> gauges = gaugeRepository.findAll();

        Map<String, String> categoryMap = generateQuickMap(gauges);
        List<Transactions> uncategorizedTransactions = new ArrayList<>();
        Map<String, List<Transactions>> gaugeFilteredTxn =  new HashMap<>();
        Map<String, Double> gaugeMap = new HashMap<>();

        for(Gauge gauge : gauges){
            gaugeMap.put(gauge.getGaugeName(), 0D);
            gaugeFilteredTxn.put(gauge.getGaugeName(), new ArrayList<>());
        }

        for(Transactions transaction : transactions){
            String category = transaction.getCategory();
            String gauge = categoryMap.get(category);

            if(gauge != null) {
                Double currVal = gaugeMap.get(gauge);
                currVal += transaction.getAmount();
                currVal = Math.round(currVal * 100.0) / 100.0;
                gaugeMap.put(gauge, currVal);
                gaugeFilteredTxn.get(gauge).add(transaction);
            } else{
                uncategorizedTransactions.add(transaction);
            }
        }

        return new GaugeTransactionResponse(gaugeMap, uncategorizedTransactions, gaugeFilteredTxn, moneyIn, moneyOut);
    }

    public void saveTransactions(List<com.plaid.client.model.Transaction> plaidTransactions) {

        for (Transaction plaidTransaction : plaidTransactions) {

            String transactionId = plaidTransaction.getTransactionId();
            if (!transactionsRepository.existsById(transactionId)) {

                assert plaidTransaction.getPersonalFinanceCategory() != null;
                Transactions transaction = new Transactions(
                        plaidTransaction.getTransactionId(),
                        userId,
                        plaidTransaction.getAccountId(),
                        plaidTransaction.getMerchantName(),
                        plaidTransaction.getName(),
                        plaidTransaction.getAmount(),
                        plaidTransaction.getDate(),
                        plaidTransaction.getPersonalFinanceCategory().getDetailed(),
                        plaidTransaction.getPending()
                );

                transactionsRepository.save(transaction);
            }
        }
    }

    public void markRemovedTransactions(List<RemovedTransaction> removedTransactions) {
        for (RemovedTransaction removedTransaction : removedTransactions){
            if (transactionsRepository.existsById(removedTransaction.getTransactionId())){
                Optional<Transactions> t = transactionsRepository.findById(removedTransaction.getTransactionId());
                if(t.isPresent()){
                    t.get().setRemoved(true);
                    transactionsRepository.save(t.get());
                }
            }
        }
    }

    public Map<String, String> generateQuickMap(List<Gauge> gauges){
        Map<String, String> quickMap = new HashMap<>();
        for(Gauge gauge : gauges){
            List<String> gaugeCategories = gauge.getCategoryList();
            for(String category : gaugeCategories){
                quickMap.put(category, gauge.getGaugeName());
            }
        }
        return quickMap;
    }

    private void generateMoneyInAndOut(List<Transactions> transactions){
        for(Transactions t : transactions){
            if(t.getAmount() <= 0){
                moneyIn = moneyIn + (-1.0 * t.getAmount());
            }else{
                moneyOut = moneyOut + t.getAmount();
            }
        }
    }
}
