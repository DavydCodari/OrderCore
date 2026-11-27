package com.davyd.ordercore.user;

import com.davyd.ordercore.auth.AuthService;
import com.davyd.ordercore.security.principal.OrderCorePrincipal;
import com.davyd.ordercore.user.dto.request.DeleteAccountRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
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

        var response = controller.deleteAccount(principal, new DeleteAccountRequest("password"), request, new MockHttpServletResponse());

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(authService).deleteCredentials(42, "password");
        assertTrue(session.isInvalid());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test void incorrectPasswordDoesNotLogOut() {
        AuthService authService = mock(AuthService.class);
        doThrow(new BadCredentialsException("Invalid credentials")).when(authService).deleteCredentials(42, "wrong");
        var principal = new OrderCorePrincipal(42, "a@example.com", null, UserRole.CUSTOMER);
        var request = new MockHttpServletRequest();
        var session = (MockHttpSession) request.getSession();
        var controller = new UserController(mock(UserService.class), authService);

        assertThrows(BadCredentialsException.class, () -> controller.deleteAccount(
                principal, new DeleteAccountRequest("wrong"), request, new MockHttpServletResponse()));
        assertFalse(session.isInvalid());
    }
}
