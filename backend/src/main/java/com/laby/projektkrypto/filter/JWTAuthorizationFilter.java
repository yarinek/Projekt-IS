package com.laby.projektkrypto.filter;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    private final SecurityConstants securityConstants;
    private final UserDetailsService userDetailsService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, SecurityConstants constants,
            UserDetailsService userDetailsService) {
        super(authenticationManager);
        this.securityConstants = constants;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(token != null && token.startsWith(securityConstants.TOKEN_PREFIX)) {
            var authToken = getAuthenticationToken(token);

            authToken.ifPresent(usernamePasswordAuthenticationToken ->
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken));
        }

        chain.doFilter(request, response);
    }

    private Optional<UsernamePasswordAuthenticationToken> getAuthenticationToken(String token) {
        String userName = JWT.require(Algorithm.HMAC512(securityConstants.SECRET))
                .build()
                .verify(token.replace(securityConstants.TOKEN_PREFIX, ""))
                .getSubject();

        if(userName != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
            return Optional.of(new UsernamePasswordAuthenticationToken(userName, null, userDetails.getAuthorities()));
        }
        else
            return Optional.empty();
    }
}