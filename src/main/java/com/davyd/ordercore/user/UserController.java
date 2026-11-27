package com.davyd.ordercore.user;

import com.davyd.ordercore.security.principal.OrderCorePrincipal;
import com.davyd.ordercore.user.dto.request.ChangeEmailRequest;
import com.davyd.ordercore.user.dto.request.ChangeNameRequest;
import com.davyd.ordercore.user.dto.request.ChangePasswordRequest;
import com.davyd.ordercore.user.dto.request.DeleteAccountRequest;
import com.davyd.ordercore.user.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import com.davyd.ordercore.auth.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@PreAuthorize("isAuthenticated()")
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @Operation(summary = "Close the current account", description = "Requires the current password. Deletes login credentials, the shopping cart and its items, and ends the session. User, orders and payments are retained.")
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteAccount(
            @Parameter(hidden = true) @AuthenticationPrincipal OrderCorePrincipal principal,
            @Valid @RequestBody DeleteAccountRequest body,
            HttpServletRequest request, HttpServletResponse response) {
        authService.deleteCredentials(principal.getUserId(), body.currentPassword());
        new SecurityContextLogoutHandler().logout(request, response,
                SecurityContextHolder.getContext().getAuthentication());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Change the current user's name")
    @PatchMapping("/me/name")
    public ResponseEntity<UserResponse> changeName(
            @Parameter(hidden = true) @AuthenticationPrincipal OrderCorePrincipal principal,
            @Valid @RequestBody ChangeNameRequest request
    ) {
        UserResponse userResponse = userService.changeUserName(principal.getUserId(), request.name());
        return ResponseEntity.ok(userResponse);
    }

    @Operation(summary = "Change the current user's email")
    @PatchMapping("/me/email")
    public ResponseEntity<UserResponse> changeEmail(
            @Parameter(hidden = true) @AuthenticationPrincipal OrderCorePrincipal principal,
            @Valid @RequestBody ChangeEmailRequest request
    ) {
        UserResponse userResponse = userService.changeUserEmail(principal.getUserId(), request.email());
        return ResponseEntity.ok(userResponse);
    }

    @Operation(summary = "Change the current user's password")
    @PatchMapping("/me/password")
    public ResponseEntity<UserResponse> changePassword(
            @Parameter(hidden = true) @AuthenticationPrincipal OrderCorePrincipal principal,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        UserResponse userResponse = userService.changePassword(
                principal.getUserId(), request.currentPassword(), request.newPassword());
        return ResponseEntity.ok(userResponse);
    }
}
