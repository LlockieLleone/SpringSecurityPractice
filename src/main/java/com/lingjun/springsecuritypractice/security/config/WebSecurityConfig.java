package com.lingjun.springsecuritypractice.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity httpSecurity) throws Exception {

        String[] permitUrls = new String[]{"/auth/login", "/auth/register"};

        //httpSecurity.authorizeRequests().anyRequest().permitAll();

        httpSecurity.csrf().disable();

//        httpSecurity.authorizeHttpRequests()
//                .requestMatchers(permitUrls).permitAll().requestMatchers()


        return httpSecurity.build();
    }

}
