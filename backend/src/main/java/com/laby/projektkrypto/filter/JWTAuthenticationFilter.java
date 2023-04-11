package com.laby.projektkrypto.filter;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laby.projektkrypto.dto.UserLogin;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    final private AuthenticationManager authenticationManager;
    final private SecurityConstants securityConstants;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, SecurityConstants constants) {
        this.authenticationManager = authenticationManager;
        this.securityConstants = constants;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
        try {
            UserLogin credentials = new ObjectMapper().readValue(req.getInputStream(), UserLogin.class);

            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credentials.getUsername(),
                    credentials.getPassword()));
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) {
        String token = JWT.create()
                .withSubject(((User) authResult.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + securityConstants.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(securityConstants.SECRET));

        response.addHeader(HttpHeaders.AUTHORIZATION, securityConstants.TOKEN_PREFIX + token);
    }
}