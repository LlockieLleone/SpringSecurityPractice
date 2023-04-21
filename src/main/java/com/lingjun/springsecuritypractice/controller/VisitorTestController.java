package com.lingjun.springsecuritypractice.controller;

import com.lingjun.springsecuritypractice.security.model.SecurityUser;
import com.lingjun.springsecuritypractice.security.service.SecurityUserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/visitor")
public class VisitorTestController {

    @Resource
    private SecurityUserService securityUserService;

    @RequestMapping("/test")
    public String hello() {
        return "visitor test success!";
    }

}
