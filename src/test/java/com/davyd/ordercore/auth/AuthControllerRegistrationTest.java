package com.davyd.ordercore.auth;

import com.davyd.ordercore.auth.dto.request.RegisterRequest;
import com.davyd.ordercore.common.exception.dto.ErrorResponse;
import com.davyd.ordercore.user.UserService;
import com.davyd.ordercore.user.dto.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.SecurityContextRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthControllerRegistrationTest {
    @Test
    void reportsCreatedAccountWhenAutomaticLoginFails() {
        AuthService service = mock(AuthService.class);
        AuthenticationManager manager = mock(AuthenticationManager.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(service.register("Alice", "alice@example.com", "password"))
                .thenReturn(mock(UserResponse.class));
        when(manager.authenticate(any())).thenThrow(new BadCredentialsException("failed"));
        when(request.getSession(false)).thenReturn(session);
        AuthController controller = new AuthController(service, manager,
                mock(SessionAuthenticationStrategy.class), mock(SecurityContextRepository.class),
                mock(UserService.class));

        var response = controller.register(new RegisterRequest("Alice", "alice@example.com", "password"),
                request, mock(HttpServletResponse.class));

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertEquals("Your account was created, but automatic login failed. Please log in.",
                ((ErrorResponse) response.getBody()).message());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(session).invalidate();
        verify(service).register("Alice", "alice@example.com", "password");
    }
}
