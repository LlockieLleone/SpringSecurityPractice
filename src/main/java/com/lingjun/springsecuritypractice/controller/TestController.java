package com.lingjun.springsecuritypractice.controller;

import com.lingjun.springsecuritypractice.security.model.SecurityUser;
import com.lingjun.springsecuritypractice.security.service.SecurityUserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private SecurityUserService securityUserService;

    @RequestMapping("/test")
    public String hello() {
        return "test success!";
    }

    @RequestMapping("/changepassword")
    public String changepassword(@RequestBody SecurityUser securityUser) {

        return securityUserService.changePassword(securityUser.getUsername(), securityUser.getPassword());

    }

    @RequestMapping("/matchpassword")
    public String matchpassword(@RequestBody SecurityUser securityUser) {

        return securityUserService.matchPassword(securityUser.getUsername(), securityUser.getPassword());

    }

}
