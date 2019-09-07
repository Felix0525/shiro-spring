package com.wuyue.shiro.merchant.service;

import com.wuyue.shiro.merchant.entity.Merchant;

import java.util.Optional;

public interface MerchantService {

    Optional<Merchant> findById(String id);

}
