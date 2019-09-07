package com.wuyue.shiro.account.dao;

import com.wuyue.shiro.account.entity.AccountMerchantLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountMerchantLinkDao extends JpaRepository<AccountMerchantLink, String> {

    List<AccountMerchantLink> findByAccountId(String accountId);

}
