package com.davyd.ordercore.user;

import com.davyd.ordercore.auth.AuthService;
import com.davyd.ordercore.security.principal.OrderCorePrincipal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerDeleteTest {
    @AfterEach void clearContext() { SecurityContextHolder.clearContext(); }

    @Test void deletesOnlyCurrentUsersCredentialsAndLogsOut() {
        AuthService authService = mock(AuthService.class);
        var principal = new OrderCorePrincipal(42, "a@example.com", null, UserRole.CUSTOMER);
        SecurityContextHolder.getContext().setAuthentication(
                UsernamePasswordAuthenticationToken.authenticated(principal, null, principal.getAuthorities()));
        var request = new MockHttpServletRequest();
        var session = (MockHttpSession) request.getSession();
        var controller = new UserController(mock(UserService.class), authService);

        var response = controller.deleteAccount(principal, request, new MockHttpServletResponse());

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(authService).deleteCredentials(42);
        assertTrue(session.isInvalid());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
