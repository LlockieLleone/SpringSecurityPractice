package com.lingjun.springsecuritypractice.security.config;

import com.lingjun.springsecuritypractice.security.filter.JwtTokenFilter;
import com.lingjun.springsecuritypractice.security.filter.SecurityAuthTokenFilter;
import com.lingjun.springsecuritypractice.security.handler.AccessDeny;
import com.lingjun.springsecuritypractice.security.handler.AnonymousAuthenticationEntryPoint;
import com.lingjun.springsecuritypractice.security.handler.AuthenticationLogout;
import com.lingjun.springsecuritypractice.security.provider.SelfAuthenticationProvider;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private AnonymousAuthenticationEntryPoint anonymousAuthenticationEntryPoint;

    private AuthenticationLogout authenticationLogout;

    private AccessDeny accessDeny;

    private SelfAuthenticationProvider selfAuthenticationProvider;

    @Autowired
    public void setAnonymousAuthenticationEntryPoint(AnonymousAuthenticationEntryPoint anonymousAuthenticationEntryPoint) {
        this.anonymousAuthenticationEntryPoint = anonymousAuthenticationEntryPoint;
    }

    @Autowired
    public void setAuthenticationLogout(AuthenticationLogout authenticationLogout) {
        this.authenticationLogout = authenticationLogout;
    }

    @Autowired
    public void setAccessDeny(AccessDeny accessDeny) {
        this.accessDeny = accessDeny;
    }

    @Autowired
    public void setSelfAuthenticationProvider(SelfAuthenticationProvider selfAuthenticationProvider) {
        this.selfAuthenticationProvider = selfAuthenticationProvider;
    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    //使用自定义的AuthenticationProvider
    @Bean
    public AuthenticationManager authManager(HttpSecurity httpSecurity) throws Exception {
//        AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
//        authenticationManagerBuilder.authenticationProvider(selfAuthenticationProvider);
//        return authenticationManagerBuilder.build();
        return new ProviderManager(selfAuthenticationProvider);
    }

    //SecurityFilterChain 用于配置HttpSecurity
    @Bean
    public SecurityFilterChain filterChain (HttpSecurity httpSecurity) throws Exception {

        String[] permitUrls = new String[]{"/login", "/register"};

        httpSecurity.csrf().disable();

        httpSecurity.authorizeHttpRequests()
                .requestMatchers("/anonymous/**").anonymous()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/visitor/**").hasRole("VISITOR")
                .requestMatchers(permitUrls).permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeny)
                .authenticationEntryPoint(anonymousAuthenticationEntryPoint)
                .and()
                .logout()
                .logoutSuccessHandler(authenticationLogout)
                .and()
                .addFilterBefore(new JwtTokenFilter("/login", authManager(httpSecurity)), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new SecurityAuthTokenFilter(authManager(httpSecurity)), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return httpSecurity.build();
    }

    //WebSecurityCustomizer 用于配置WebSecurity
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        //指定不经过spring security过滤器的url
        String[] ignoreUrls = new String[]{""};
        return (web) -> web.ignoring().requestMatchers(ignoreUrls);
    }


}
