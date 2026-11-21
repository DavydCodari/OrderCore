package org.example.ordercore.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.ordercore.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Authentication",
        description = "Session-based authentication, registration and current user profile"
)
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final SessionAuthenticationStrategy sessionAuthenticationStrategy;
    private final SecurityContextRepository securityContextRepository;
    private final UserService userService;

    public AuthController(
            AuthService authService,
            AuthenticationManager authenticationManager,
            SessionAuthenticationStrategy sessionAuthenticationStrategy,
            SecurityContextRepository securityContextRepository,
            UserService userService
    ) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.sessionAuthenticationStrategy = sessionAuthenticationStrategy;
        this.securityContextRepository = securityContextRepository;
        this.userService = userService;
    }

    @Operation(
            summary = "Get a CSRF token",
            description = "Returns a CSRF token and its header and parameter names. "
                    + "Keep the session cookie and send the token in the indicated header for state-changing requests. "
                    + "Fetch a new token after login or registration.",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "CSRF token retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CsrfToken.class))
            )
    )
    @GetMapping("/csrf")
    public ResponseEntity<CsrfToken> csrf(@Parameter(hidden = true) CsrfToken csrfToken){
        return ResponseEntity.ok(csrfToken);
    }


}