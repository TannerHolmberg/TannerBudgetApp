package com.bank.tanner.budget.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "transactions")
public class Transactions {

    @Id
    private String transactionId;

    private String userId;

    private String accountId;

    private String merchantName;

    private String name;

    private Double amount;

    private LocalDate date;

    private String category;

    private boolean pending;

    private boolean removed;

    public Transactions() {
    }

    public Transactions(
            String transactionId,
            String userId,
            String accountId,
            String merchantName,
            String name,
            Double amount,
            LocalDate date,
            String category,
            boolean pending
    ) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.accountId = accountId;
        this.merchantName = merchantName;
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.pending = pending;
        this.removed = false;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getUserId() {
        return userId;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public String getName() {
        return name;
    }

    public Double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public boolean isPending() {
        return pending;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public boolean isRemoved() { return removed; }

    public void setRemoved(boolean removed) { this.removed = removed; }
}