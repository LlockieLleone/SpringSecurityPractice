package com.lingjun.springsecuritypractice.security.provider;

import com.lingjun.springsecuritypractice.security.service.SecurityService;
import com.lingjun.springsecuritypractice.security.service.SecurityUserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 自定义认证逻辑
 */
//@Component
public class SelfAuthenticationProvider implements AuthenticationProvider{
    @Resource
    private SecurityService securityService;

    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //获取用户名
        String username = String.valueOf(authentication.getPrincipal());
        //获取密码
        String password = String.valueOf(authentication.getCredentials());

        UserDetails userDetails= securityService.loadUserByUsername(username);

        if(bCryptPasswordEncoder.matches(password,userDetails.getPassword())){
            return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
        }

        throw new BadCredentialsException("Password is incorrect, please log in again!");

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }
}
