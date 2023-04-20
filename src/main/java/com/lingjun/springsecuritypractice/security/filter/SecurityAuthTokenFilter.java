package com.lingjun.springsecuritypractice.security.filter;

import com.lingjun.springsecuritypractice.model.common.Response;
import com.lingjun.springsecuritypractice.security.model.JwtParametric;
import com.lingjun.springsecuritypractice.security.model.SecurityUser;
import com.lingjun.springsecuritypractice.security.utils.JwtTokenUtil;
import com.lingjun.springsecuritypractice.security.utils.ServletUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.security.Security;
import java.util.HashSet;
import java.util.Set;

public class SecurityAuthTokenFilter extends BasicAuthenticationFilter {

    @Resource JwtTokenUtil jwtTokenUtil;

    public SecurityAuthTokenFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = request.getHeader(JwtParametric.headerString);
        Set<GrantedAuthority> authorities = jwtTokenUtil.getAuthoritiesFromToken(token);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        if(username != null && authorities != null){
            SecurityUser securityUser = new SecurityUser().setUsername(username).setAuthorities(authorities);
        }else{
            ServletUtils.render(request,response, Response.fail("Token expired or invalid"));
            return;
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            // If not expired, keep login status
            if(!jwtTokenUtil.validateTokenIsExpired(token)){
                // Store user information in authentication for easy verification in the future
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
                //SecurityContextHolder authorization verification context
                SecurityContext context = SecurityContextHolder.getContext();
                //Indicates that the user has passed authentication
                context.setAuthentication(authentication);
            }
        }

        // Continue to the next filter
        filterChain.doFilter(request, response);

    }
}
