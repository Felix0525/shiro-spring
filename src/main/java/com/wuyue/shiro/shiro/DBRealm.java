package com.wuyue.shiro.shiro;

import com.wuyue.shiro.account.entity.Account;
import com.wuyue.shiro.account.entity.AccountBrandLink;
import com.wuyue.shiro.account.entity.AccountMerchantLink;
import com.wuyue.shiro.account.service.AccountService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.*;

import static java.util.stream.Collectors.toSet;

public class DBRealm extends AuthorizingRealm {

    private AccountService accountService;

    public DBRealm(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * 认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        Optional<Account> optAccount = accountService.findByUsername(upToken.getUsername());
        Account account = optAccount.orElseThrow(UnknownAccountException::new);
        Map<String, Object> principal = new HashMap<>();
        principal.put("accountId", account.getId());
        principal.put("name", account.getUsername());
        return new SimpleAuthenticationInfo(principal, account.getPwd().toCharArray(), DBRealm.class.getName());
    }

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Map<String, Object> principal = (Map<String, Object>) principals.getPrimaryPrincipal();
        String accountId = (String) principal.get("accountId");

        // 拥有的商家资源权限
        List<AccountMerchantLink> merchantLinks = accountService.findMerchantLinks(accountId);
        Set<String> merchantPermissions = merchantLinks.stream().map(AccountMerchantLink::getMerchantId).collect(toSet());
        SimpleAuthorizationInfo authzInfo = new SimpleAuthorizationInfo();
        authzInfo.addStringPermissions(merchantPermissions);

        // 拥有的品牌资源权限
        List<AccountBrandLink> brandLinks = accountService.findBrandLinks(accountId);
        Set<String> brandPermissions = brandLinks.stream().map(link -> link.getMerchantId() + ":" + link.getBrandId()).collect(toSet());
        authzInfo.addStringPermissions(brandPermissions);

        return authzInfo;
    }

}
