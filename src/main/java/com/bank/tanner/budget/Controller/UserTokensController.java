package com.bank.tanner.budget.Controller;

import com.bank.tanner.budget.Service.UserTokensService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-tokens")
public class UserTokensController {

    UserTokensService userTokensService;

    public UserTokensController(UserTokensService userTokensService){
        this.userTokensService = userTokensService;
    }

    @GetMapping("/does-token-exist")
    public boolean checkIfTokenExists(){
        return userTokensService.checkIfAuthTokenExists();
    }
}
