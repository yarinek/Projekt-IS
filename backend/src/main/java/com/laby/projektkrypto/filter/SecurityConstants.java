package com.laby.projektkrypto.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:jwt.properties")
public class SecurityConstants {
    final String SECRET;
    final String TOKEN_PREFIX;
    final Integer EXPIRATION_TIME;

    public SecurityConstants(@Value("${jwt.secret}") String secret, @Value("${jwt.prefix}") String prefix,
            @Value("${jwt.expiration}") Integer expiration) {
        this.SECRET = secret;
        this.TOKEN_PREFIX = prefix;
        this.EXPIRATION_TIME = expiration;
    }
}
