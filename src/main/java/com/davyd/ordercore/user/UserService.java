package com.davyd.ordercore.user;

import com.davyd.ordercore.common.util.Validation;
import com.davyd.ordercore.user.dto.response.UserResponse;
import com.davyd.ordercore.user.exception.EmailAlreadyExistsException;
import com.davyd.ordercore.user.exception.UserNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
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
    public UserResponse changeUserName(long userId, String newName){
        User user = getUserEntity(userId);

        user.changeName(newName);
        return userMapper.toResponse(user);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public UserResponse changeUserEmail(long userId, String newEmail){
        User user = getUserEntity(userId);
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
}