package org.example.ordercore.user;

import org.example.ordercore.user.dto.response.UserResponse;
import org.example.ordercore.user.exception.UserNotFoundException;
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
}