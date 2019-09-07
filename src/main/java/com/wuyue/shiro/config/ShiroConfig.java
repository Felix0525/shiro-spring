package com.wuyue.shiro.config;

import com.wuyue.shiro.account.service.AccountService;
import com.wuyue.shiro.shiro.DBRealm;
import com.wuyue.shiro.shiro.PermitAdvisor;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.ServletContainerSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.spel.standard.SpelExpressionParser;

@Configuration
public class ShiroConfig {

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        return shiroFilterFactoryBean;
    }

    @Bean
    public ServletContainerSessionManager servletContainerSessionManager() {
        return new ServletContainerSessionManager();
    }

    /**
     * 用于安全访问用户数据的域对象
     */
    @Bean
    public Realm realm(AccountService accountService) {
        DBRealm dbRealm = new DBRealm(accountService);
        dbRealm.setCredentialsMatcher(new SimpleCredentialsMatcher());
        return dbRealm;
    }

    /**
     * 构造shiro核心处理类
     *
     * @param sessionManager 会话管理器
     * @param realm 数据访问域
     * @return
     */
    @Bean
    public SecurityManager securityManager(SessionManager sessionManager,
                                           Realm realm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm);
        securityManager.setSessionManager(sessionManager);
        return securityManager;
    }

    /**
     * 使shiro注解生效
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        return new AuthorizationAttributeSourceAdvisor();
    }

    /**
     * 声明自定义切面，使用注解和SpEL表达式实现对资源细粒度的权限校验
     */
    @Bean
    public PermitAdvisor permitAdvisor() {
        return new PermitAdvisor(new SpelExpressionParser());
    }

}
