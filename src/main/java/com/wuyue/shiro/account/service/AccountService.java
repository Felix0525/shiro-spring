package com.wuyue.shiro.account.service;

import com.wuyue.shiro.account.entity.Account;
import com.wuyue.shiro.account.entity.AccountBrandLink;
import com.wuyue.shiro.account.entity.AccountMerchantLink;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    Optional<Account> findByUsername(String username);

    List<AccountMerchantLink> findMerchantLinks(String accountId);

    List<AccountBrandLink> findBrandLinks(String accountId);

}
