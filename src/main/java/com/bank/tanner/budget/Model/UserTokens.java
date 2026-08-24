package com.bank.tanner.budget.Model;

import jakarta.persistence.*;

import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_tokens")
public class UserTokens {
    @Id
    private Long id;

    private LocalDateTime createdAt;

    private String token;

    UserTokens(){}

    public UserTokens(Long id, LocalDateTime createdAt, String token){
        this.id = id;
        this.createdAt = createdAt;
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setExpiration(LocalDateTime date){
        createdAt = date;
    }

    public void setToken(String token){
        this.token = token;
    }

    public LocalDateTime getExpiration(){
        return createdAt;
    }

    public String getToken(){
        return token;
    }

}
