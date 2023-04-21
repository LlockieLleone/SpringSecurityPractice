package com.lingjun.springsecuritypractice.controller;

import com.lingjun.springsecuritypractice.security.model.SecurityUser;
import com.lingjun.springsecuritypractice.security.service.SecurityUserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/anonymous")
public class AnonymousTestController {

    @Resource
    private SecurityUserService securityUserService;

    @RequestMapping("/test")
    public String hello() {
        return "anonymous test success!";
    }


}
