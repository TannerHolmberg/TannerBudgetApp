package com.bank.tanner.budget.Configuration;

import com.bank.tanner.budget.Controller.UserTokensController;
import com.bank.tanner.budget.Repository.UserTokensRepository;
import com.bank.tanner.budget.Service.UserTokensService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserTokensConfig {

    @Value("${user.id}")
    Long userId;

    @Bean
    UserTokensService userTokensService(UserTokensRepository userTokensRepository){
        return new UserTokensService(userTokensRepository, userId);
    }

    @Bean
    UserTokensController userTokensController(UserTokensService userTokensService){
        return new UserTokensController(userTokensService);
    }
}
