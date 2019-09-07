package com.wuyue.shiro.merchant.service;

import com.wuyue.shiro.merchant.dao.MerchantDao;
import com.wuyue.shiro.merchant.entity.Merchant;
import com.wuyue.shiro.shiro.Permitable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private MerchantDao merchantDao;

    @Permitable(pre = "#id")
    @Override
    public Optional<Merchant> findById(String id) {
        if (StringUtils.isBlank(id)) {
            return Optional.empty();
        }
        return merchantDao.findById(id);
    }

}
