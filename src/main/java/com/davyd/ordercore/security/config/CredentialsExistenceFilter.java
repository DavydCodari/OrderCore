package com.davyd.ordercore.security.config;

import com.davyd.ordercore.auth.UserCredentialsRepository;
import com.davyd.ordercore.security.principal.OrderCorePrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class CredentialsExistenceFilter extends OncePerRequestFilter {
    private final UserCredentialsRepository credentialsRepository;

    public CredentialsExistenceFilter(UserCredentialsRepository credentialsRepository) {
        this.credentialsRepository = credentialsRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof OrderCorePrincipal principal
                && !credentialsRepository.existsById(principal.getUserId())) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        chain.doFilter(request, response);
    }
}
