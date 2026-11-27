package com.davyd.ordercore.auth;

import com.davyd.ordercore.cart.ShoppingCartService;
import com.davyd.ordercore.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountClosureTest {
    @Test void deletesCredentialsOnlyAfterPasswordMatches() {
        var repository = mock(UserCredentialsRepository.class);
        var encoder = mock(PasswordEncoder.class);
        var credentials = mock(UserCredentials.class);
        when(repository.findForAccountClosure(42L)).thenReturn(Optional.of(credentials));
        when(credentials.getHashedPassword()).thenReturn("hash");
        when(encoder.matches("correct", "hash")).thenReturn(true);
        var cartService = mock(ShoppingCartService.class);
        var service = new AuthService(encoder, repository, cartService, mock(UserService.class));

        assertThrows(BadCredentialsException.class, () -> service.deleteCredentials(42, "wrong"));
        verify(repository, never()).delete(any());
        verifyNoInteractions(cartService);
        service.deleteCredentials(42, "correct");
        verify(repository).delete(credentials);
        verify(cartService).deleteCartForAccountClosure(42);
    }

    @Test void missingCredentialsAreRejected() {
        var repository = mock(UserCredentialsRepository.class);
        var service = new AuthService(mock(PasswordEncoder.class), repository,
                mock(ShoppingCartService.class), mock(UserService.class));
        assertThrows(BadCredentialsException.class, () -> service.deleteCredentials(42, "password"));
        verify(repository, never()).delete(any());
    }
}
