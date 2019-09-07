package com.wuyue.shiro.account.dao;

import com.wuyue.shiro.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountDao extends JpaRepository<Account, String> {

    Optional<Account> findByUsername(String username);

}
