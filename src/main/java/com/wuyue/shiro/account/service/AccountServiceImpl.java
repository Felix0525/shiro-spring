package com.wuyue.shiro.account.service;

import com.wuyue.shiro.account.dao.AccountBrandLinkDao;
import com.wuyue.shiro.account.dao.AccountDao;
import com.wuyue.shiro.account.dao.AccountMerchantLinkDao;
import com.wuyue.shiro.account.entity.Account;
import com.wuyue.shiro.account.entity.AccountBrandLink;
import com.wuyue.shiro.account.entity.AccountMerchantLink;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private AccountMerchantLinkDao accountMerchantLinkDao;

    @Autowired
    private AccountBrandLinkDao accountBrandLinkDao;

    @Override
    public Optional<Account> findByUsername(String username) {
        return accountDao.findByUsername(username);
    }

    @Override
    public List<AccountMerchantLink> findMerchantLinks(String accountId) {
        if (StringUtils.isBlank(accountId)) {
            return Collections.emptyList();
        }
        return accountMerchantLinkDao.findByAccountId(accountId);
    }

    @Override
    public List<AccountBrandLink> findBrandLinks(String accountId) {
        if (StringUtils.isBlank(accountId)) {
            return Collections.emptyList();
        }
        return accountBrandLinkDao.findByAccountId(accountId);
    }

}
