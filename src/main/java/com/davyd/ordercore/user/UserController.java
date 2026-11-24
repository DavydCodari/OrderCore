package com.davyd.ordercore.user;

import com.davyd.ordercore.security.principal.OrderCorePrincipal;
import com.davyd.ordercore.user.dto.request.ChangeEmailRequest;
import com.davyd.ordercore.user.dto.request.ChangeNameRequest;
import com.davyd.ordercore.user.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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
}
