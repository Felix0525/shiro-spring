package com.wuyue.shiro.auth.controller;

import com.wuyue.shiro.DataResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.wuyue.shiro.StatusCode.AUTHENTICATION_ERROR;

@Slf4j
@RequestMapping("auth")
@RestController
public class AuthController {

    @PostMapping("/login/{username}/{password}")
    public DataResp<String> login(@PathVariable("username") String username,
                                  @PathVariable("password") String password) {
        // facade for shiro
        Subject subject = SecurityUtils.getSubject();

        // If current principal is authenticated, logout first
        if (subject.isAuthenticated()) {
            subject.logout();
        }

        // Collect user info
        UsernamePasswordToken token = new UsernamePasswordToken(username, password.toCharArray());

        // Delegate login to shiro
        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            // Incorrect username or password
            return DataResp.build(AUTHENTICATION_ERROR.getCode(), AUTHENTICATION_ERROR.getMsg());
        }

        Map<String, Object> principal = (Map<String, Object>) subject.getPrincipal();
        log.info("Login success, current principal is : {}", principal);

        // return the session id
        return DataResp.build((String) subject.getSession(false).getId());
    }

    @RequiresAuthentication
    @GetMapping("/session")
    public DataResp<Map<String, Object>> getSession() {
        Map<String, Object> principal = (Map<String, Object>) SecurityUtils.getSubject().getPrincipal();
        return DataResp.build(principal);
    }

    @RequiresAuthentication
    @GetMapping("/logout")
    public DataResp<Void> logout() {
        SecurityUtils.getSubject().logout();
        return DataResp.build();
    }

}
