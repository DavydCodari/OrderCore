package org.example.ordercore.user;

import org.example.ordercore.common.util.Validation;
import org.example.ordercore.user.dto.response.UserResponse;
import org.example.ordercore.user.exception.EmailAlreadyExistsException;
import org.example.ordercore.user.exception.UserNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            if (Validation.isConstraintViolation(e, "uk_users_email")){
                throw new EmailAlreadyExistsException(email);
            }

            throw e;
        }
    }
}