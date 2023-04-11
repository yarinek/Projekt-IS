package com.laby.projektkrypto.config;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

import com.laby.projektkrypto.filter.JWTAuthenticationFilter;
import com.laby.projektkrypto.filter.JWTAuthorizationFilter;
import com.laby.projektkrypto.filter.SecurityConstants;
import com.laby.projektkrypto.service.UserDetailsServiceImpl;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    private final SecurityConstants securityConstants;
    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(SecurityConstants securityConstants, UserDetailsServiceImpl userDetailsService) {
        this.securityConstants = securityConstants;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
                .antMatchers("/ws/**").anonymous()
                .antMatchers("/api/rest/register").anonymous()
                .antMatchers("/api/rest/db/**").hasAuthority(UserRole.ROLE_ADMIN.name())
                .antMatchers("/api/rest/events/**").authenticated()
                .antMatchers("/**").denyAll()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), securityConstants))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), securityConstants, userDetailsService))
                .exceptionHandling()
                .authenticationEntryPoint((request, response, exception) ->
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized"));
    }

    @Bean
    public Argon2PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService);

        return authenticationProvider;
    }
}