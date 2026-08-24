package com.bank.tanner.budget.Repository;

import com.bank.tanner.budget.Model.UserTokens;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTokensRepository extends JpaRepository<UserTokens, Long> {
}
