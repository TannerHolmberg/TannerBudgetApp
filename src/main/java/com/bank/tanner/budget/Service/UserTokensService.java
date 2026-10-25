package com.bank.tanner.budget.Service;

import com.bank.tanner.budget.Model.UserTokens;
import com.bank.tanner.budget.Repository.UserTokensRepository;

import java.time.LocalDateTime;

public class UserTokensService {

    private final UserTokensRepository userTokensRepository;

     private final Long userId;

    public UserTokensService(UserTokensRepository userTokensRepository, Long userId){
        this.userTokensRepository = userTokensRepository;
        this.userId = userId;
    }

    public boolean checkIfAuthTokenExists(){
        UserTokens existingToken = userTokensRepository
                .findById(userId)
                .orElse(null);

        if (existingToken == null) {
            return false;
        }else{
            return true;
        }

    }

    public String getAccessToken(){
        UserTokens accessToken = userTokensRepository
                .findById(userId)
                .orElse(null);

        assert accessToken != null;
        if(accessToken.getToken() == null){
            return null;
        }

        return accessToken.getToken();
    }

    public boolean saveTokenIfNeeded(String accessToken){
        UserTokens existingToken = userTokensRepository
                .findById(userId)
                .orElse(null);

        if (existingToken == null) {
            UserTokens newToken =
                    new UserTokens(userId, LocalDateTime.now(), accessToken, "");
            userTokensRepository.save(newToken);
            return true;
        }

        if (existingToken.getToken().equals(accessToken)) {
            return false;
        }

        existingToken.setToken(accessToken);
        userTokensRepository.save(existingToken);
        return true;
    }

    public String getCursor(){
        UserTokens info = userTokensRepository
                .findById(userId)
                .orElseThrow(() -> new IllegalStateException(
                        "User token does not exist"
                ));

        if (info.getCursor() == null || info.getCursor().isEmpty()) {
            return null;
        }

        return info.getCursor();
    }

    public void setCursor(String cursor){
        UserTokens info = userTokensRepository
                .findById(userId)
                .orElseThrow(() -> new IllegalStateException("User token does not exist"));

        info.setCursor(cursor);

        userTokensRepository.save(info);
    }
}
