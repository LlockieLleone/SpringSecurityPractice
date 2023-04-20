package com.lingjun.springsecuritypractice.security.utils;

import com.lingjun.springsecuritypractice.security.model.JwtParametric;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.StringUtils;

import java.util.*;

@Component
public class JwtTokenUtil {

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtParametric.CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(JwtParametric.CLAIM_KEY_CREATED, new Date());
        claims.put(JwtParametric.CLAIM_KEY_ROLE, userDetails.getAuthorities());
        return generateToken(claims);
    }

    private String generateToken(Map<String, Object> claims) {

        return Jwts.builder().setClaims(claims)
                .setExpiration(generateExpriationDate())
                .setId(UUID.randomUUID().toString())
                .setSubject(JwtParametric.subject)
                .signWith(SignatureAlgorithm.HS512, JwtParametric.secret)
                .compact();

    }

    private Date generateExpriationDate() {
        return new Date(System.currentTimeMillis() + JwtParametric.expirationTime * 1000);
    }

    public Boolean validateTokenIsExpired(String token){
        return validateExpirationDate(token);
    }

    private Boolean validateExpirationDate(String token){
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Date getExpirationDateFromToken(String token){
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    public String getUsernameFromToken(String token){
        String username;
        try {
            final Claims claims = getClaimsFromToken(token);
            username = (String) claims.get(JwtParametric.CLAIM_KEY_USERNAME);
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    public Set<GrantedAuthority> getAuthoritiesFromToken(String token){
        Set<GrantedAuthority> authoritiesSet = new HashSet();
        try {
            final Claims claims = getClaimsFromToken(token);
            String roles = (String) claims.get(JwtParametric.CLAIM_KEY_ROLE);
            if(StringUtils.hasLength(roles)){
                roles = roles.substring(1, roles.length() - 1);
                String[] roleArray = roles.split(",");
                for(String role : roleArray){
                    GrantedAuthority authority = new SimpleGrantedAuthority(role);
                    authoritiesSet.add(authority);
                }
            }
        } catch (Exception e) {
            authoritiesSet = null;
        }
        return authoritiesSet;
    }

    private Claims getClaimsFromToken(String token){
        Claims claims;
        try {
            claims = Jwts.parser()
                    .requireSubject(JwtParametric.subject)
                    .setSigningKey(JwtParametric.secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            claims = null;
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }



}
