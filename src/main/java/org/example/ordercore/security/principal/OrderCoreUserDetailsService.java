package org.example.ordercore.security.principal;

import org.example.ordercore.auth.UserCredentials;
import org.example.ordercore.auth.UserCredentialsRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderCoreUserDetailsService implements UserDetailsService {
    private final UserCredentialsRepository userCredentialsRepository;

    public OrderCoreUserDetailsService(UserCredentialsRepository userCredentialsRepository){
        this.userCredentialsRepository = userCredentialsRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserCredentials userCredentials = userCredentialsRepository.findByUser_Email(email)
                .orElseThrow(() -> new UsernameNotFoundException("User under email " + email +
                        " not found"));
        return new OrderCorePrincipal(
                userCredentials.getUser().getId(),
                email,
                userCredentials.getHashedPassword(),
                userCredentials.getUser().getRole()
        );
    }
}

