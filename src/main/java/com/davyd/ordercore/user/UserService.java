package com.davyd.ordercore.user;

import com.davyd.ordercore.auth.UserCredentials;
import com.davyd.ordercore.auth.UserCredentialsRepository;
import com.davyd.ordercore.common.util.Validation;
import com.davyd.ordercore.user.dto.response.UserResponse;
import com.davyd.ordercore.user.exception.EmailAlreadyExistsException;
import com.davyd.ordercore.user.exception.UserNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final UserCredentialsRepository userCredentialsRepository;
    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserCredentialsRepository userCredentialsRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userCredentialsRepository = userCredentialsRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public UserResponse getUser(long currentUserId) {
        User user = userRepository.findById(currentUserId)
                .orElseThrow(
                        () -> new UserNotFoundException(currentUserId)
                );

        return userMapper.toResponse(user);
    }

    public User getUserEntity(long id) {
        return userRepository.findById(id)
                .orElseThrow(
                        () -> new UserNotFoundException(id)
                );
    }

    public User getUserEntity(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new UserNotFoundException(email)
                );
    }

    public UserResponse getUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException(email));


        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponse createUser(String name, String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }

        try {
            return userMapper.toResponse(userRepository.saveAndFlush(new User(name, email, UserRole.CUSTOMER)));
        } catch (DataIntegrityViolationException e){
            if (Validation.isConstraintViolation(e, "uq_users_email")){
                throw new EmailAlreadyExistsException(email);
            }

            throw e;
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public UserResponse changeUserName(long currentUserId, String newName){
        User user = getUserEntity(currentUserId);

        user.changeName(newName);
        return userMapper.toResponse(user);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public UserResponse changeUserEmail(long currentUserId, String newEmail){
        User user = getUserEntity(currentUserId);
        if (Objects.equals(newEmail, user.getEmail())){
            return userMapper.toResponse(user);
        }
        if (userRepository.existsByEmail(newEmail)) {
            throw new EmailAlreadyExistsException(newEmail);
        }

        try {
            user.changeEmail(newEmail);
            userRepository.flush();
            return userMapper.toResponse(user);
        } catch (DataIntegrityViolationException e){
            if (Validation.isConstraintViolation(e, "uq_users_email")){
                throw new EmailAlreadyExistsException(newEmail);
            }

            throw e;
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public UserResponse changePassword(long currentUserId, String currentPassword, String newPassword) {
        UserCredentials userCredentials = userCredentialsRepository.findById(currentUserId)
                .orElseThrow(() -> new UserNotFoundException(currentUserId));

        if (!passwordEncoder.matches(currentPassword, userCredentials.getHashedPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        userCredentials.changeHashedPassword(passwordEncoder.encode(newPassword));
        return userMapper.toResponse(userCredentials.getUser());
    }
}
