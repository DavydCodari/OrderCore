package com.davyd.ordercore.security;

import com.davyd.ordercore.auth.UserCredentialsRepository;
import com.davyd.ordercore.security.config.CredentialsExistenceFilter;
import com.davyd.ordercore.security.principal.OrderCorePrincipal;
import com.davyd.ordercore.user.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CredentialsExistenceFilterTest {
    @AfterEach void clearContext() { SecurityContextHolder.clearContext(); }

    @Test void deletedCredentialsInvalidateExistingSession() throws Exception {
        UserCredentialsRepository repository = mock(UserCredentialsRepository.class);
        when(repository.existsById(42L)).thenReturn(false);
        var principal = new OrderCorePrincipal(42, "a@example.com", null, UserRole.CUSTOMER);
        SecurityContextHolder.getContext().setAuthentication(
                UsernamePasswordAuthenticationToken.authenticated(principal, null, principal.getAuthorities()));
        var request = new MockHttpServletRequest();
        var session = request.getSession();

        new CredentialsExistenceFilter(repository).doFilter(request, new MockHttpServletResponse(), new MockFilterChain());

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        assertTrue(((org.springframework.mock.web.MockHttpSession) session).isInvalid());
    }

    @Test void existingCredentialsKeepAuthentication() throws Exception {
        UserCredentialsRepository repository = mock(UserCredentialsRepository.class);
        when(repository.existsById(42L)).thenReturn(true);
        var principal = new OrderCorePrincipal(42, "a@example.com", null, UserRole.CUSTOMER);
        var authentication = UsernamePasswordAuthenticationToken.authenticated(principal, null, principal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        new CredentialsExistenceFilter(repository).doFilter(new MockHttpServletRequest(), new MockHttpServletResponse(), new MockFilterChain());

        assertSame(authentication, SecurityContextHolder.getContext().getAuthentication());
    }
}
