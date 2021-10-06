package com.example.waffledemo.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import waffle.spring.WindowsAuthenticationToken;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class CustomNegotiateSecurityFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        final HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getClass() == WindowsAuthenticationToken.class) {
            String principal = authentication.getName().toLowerCase();
            String domain;
            domain = principal.split("\\\\")[0];

            if (!domain.equals("desktop-3td884l")) {
                sendUnauthorized(httpServletResponse);
            }
            List<? extends GrantedAuthority> x = (List<? extends GrantedAuthority>) authentication.getAuthorities();
            System.out.println(x);
        }

        chain.doFilter(httpServletRequest, httpServletResponse);
    }

    private void sendUnauthorized(HttpServletResponse response) throws IOException {

        SecurityContextHolder.clearContext();
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

}
