package com.lingjun.springsecuritypractice.security.service;

import com.lingjun.springsecuritypractice.security.model.SecurityUser;
import com.lingjun.springsecuritypractice.security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SecurityService implements UserDetailsService {

    private SecurityUserService securityUserService;

    @Autowired
    private void setSecurityUserService(SecurityUserService securityUserService) {
        this.securityUserService = securityUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = securityUserService.findUserByUsername(username);

        if(Objects.isNull(user)) {
            throw new UsernameNotFoundException("User not found");
        }

        SecurityUser securityUser = new SecurityUser();

        securityUser.setUsername(user.getUsername());
        securityUser.setPassword(user.getPassword());
        securityUser.setAuthorities(getAuthorities(user));
        securityUser.setEmail(user.getEmail());

        return securityUser;
    }

    private Set<GrantedAuthority> getAuthorities(User user) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for(String role : user.getRoles()){
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.trim()));
        }
        return authorities;
    }
}
