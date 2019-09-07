package com.wuyue.shiro.merchant.dao;

import com.wuyue.shiro.merchant.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MerchantDao extends JpaRepository<Merchant, String> {
}
