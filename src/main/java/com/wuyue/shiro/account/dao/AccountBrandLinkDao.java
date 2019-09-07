package com.wuyue.shiro.account.dao;

import com.wuyue.shiro.account.entity.AccountBrandLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountBrandLinkDao extends JpaRepository<AccountBrandLink, String> {

    List<AccountBrandLink> findByAccountId(String accountId);

}
