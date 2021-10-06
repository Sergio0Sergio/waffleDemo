package com.example.waffledemo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import waffle.spring.NegotiateSecurityFilter;
import waffle.spring.NegotiateSecurityFilterEntryPoint;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter {

    private NegotiateSecurityFilter negotiateSecurityFilter;
    private NegotiateSecurityFilterEntryPoint negotiateSecurityFilterEntryPoint;
    private CustomPreAuthSecurityFilter customPreAuthSecurityFilter;
    private CustomNegotiateSecurityFilter customNegotiateSecurityFilter;

    @Autowired
    public WebSecurityConfig(NegotiateSecurityFilter negotiateSecurityFilter, NegotiateSecurityFilterEntryPoint negotiateSecurityFilterEntryPoint, CustomPreAuthSecurityFilter customPreAuthSecurityFilter, CustomNegotiateSecurityFilter customNegotiateSecurityFilter) {
        this.negotiateSecurityFilter = negotiateSecurityFilter;
        this.negotiateSecurityFilterEntryPoint = negotiateSecurityFilterEntryPoint;
        this.customPreAuthSecurityFilter = customPreAuthSecurityFilter;
        this.customNegotiateSecurityFilter = customNegotiateSecurityFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // A user needs to have the role user and has to be authenticated
        http.exceptionHandling()
                .authenticationEntryPoint(negotiateSecurityFilterEntryPoint).and()
                .addFilterBefore(customPreAuthSecurityFilter, BasicAuthenticationFilter.class)
                .addFilterAfter(negotiateSecurityFilter, BasicAuthenticationFilter.class)
                .addFilterAfter(customNegotiateSecurityFilter, BasicAuthenticationFilter.class)
                .authorizeRequests().anyRequest().fullyAuthenticated();
    }



}
