package com.wuyue.shiro.merchant.controller;

import com.wuyue.shiro.DataResp;
import com.wuyue.shiro.merchant.entity.Merchant;
import com.wuyue.shiro.merchant.service.MerchantService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/merchant")
@RestController
public class MerchantController {

    @Autowired
    private MerchantService merchantService;

    @RequiresAuthentication
    @GetMapping("/{id}")
    public DataResp<Merchant> findById(@PathVariable("id") String id) {
        return DataResp.build(merchantService.findById(id));
    }

}
