package com.davyd.ordercore.auth;

import com.davyd.ordercore.cart.ShoppingCartService;
import com.davyd.ordercore.user.User;
import com.davyd.ordercore.user.UserService;
import com.davyd.ordercore.user.dto.response.UserResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserCredentialsRepository userCredentialsRepository;
    private final ShoppingCartService shoppingCartService;
    private final UserService userService;

    public AuthService(PasswordEncoder passwordEncoder,
                       UserCredentialsRepository userCredentialsRepository,
                       ShoppingCartService shoppingCartService,
                       UserService userService){
        this.passwordEncoder = passwordEncoder;
        this.userCredentialsRepository = userCredentialsRepository;
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
    }

    @Transactional
    public UserResponse register(String name, String email, String password){
        String passwordHash = passwordEncoder.encode(password);

        UserResponse userResponse = userService.createUser(name, email);

        User user = userService.getUserEntity(userResponse.id());

        UserCredentials userCredentials = new UserCredentials(user, passwordHash);

        shoppingCartService.createShoppingCart(user.getId());

        userCredentialsRepository.save(userCredentials);

        return userResponse;
    }

    @Transactional
    public void deleteCredentials(long userId, String currentPassword) {
        UserCredentials credentials = userCredentialsRepository.findForAccountClosure(userId)
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
        if (!passwordEncoder.matches(currentPassword, credentials.getHashedPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }
        userCredentialsRepository.delete(credentials);
    }
}
