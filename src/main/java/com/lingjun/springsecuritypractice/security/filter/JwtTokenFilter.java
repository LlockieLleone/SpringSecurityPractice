package com.lingjun.springsecuritypractice.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lingjun.springsecuritypractice.model.common.Response;
import com.lingjun.springsecuritypractice.security.model.LoginDTO;
import com.lingjun.springsecuritypractice.security.model.SecurityUser;
import com.lingjun.springsecuritypractice.security.utils.JwtTokenUtil;
import com.lingjun.springsecuritypractice.security.utils.ServletUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

public class JwtTokenFilter extends AbstractAuthenticationProcessingFilter {

    private JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    //constructor
    public JwtTokenFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher(defaultFilterProcessesUrl));
        setAuthenticationManager(authenticationManager);
    }

    //json body login
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException{

        LoginDTO user = new ObjectMapper().readValue(request.getInputStream(), LoginDTO.class);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

        return getAuthenticationManager().authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth){

        SecurityUser principal = (SecurityUser) auth.getPrincipal();
        String token = jwtTokenUtil.generateToken(principal);

        try{
            ServletUtils.render(request, response, Response.success(token, "token generated successfully"));
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        String result="";
        // 账号过期
        if (failed instanceof AccountExpiredException) {
            result="Account Expired";
        }
        // 密码错误
        else if (failed instanceof BadCredentialsException) {
            result="Wrong Password";
        }
        // 密码过期
        else if (failed instanceof CredentialsExpiredException) {
            result="Password Expired";
        }
        // 账号不可用
        else if (failed instanceof DisabledException) {
            result="Account Disabled";
        }
        //账号锁定
        else if (failed instanceof LockedException) {
            result="Account Locked";
        }
        // 用户不存在
        else if (failed instanceof UsernameNotFoundException) {
            result="User Not Exist";
        }
        // 其他错误
        else{
            result="Unknown Error";
        }
        ServletUtils.render(request,response,Response.fail(result));
    }
}
