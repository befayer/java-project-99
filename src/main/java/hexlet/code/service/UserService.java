package hexlet.code.service;

import hexlet.code.dto.UserRequestDto;
import hexlet.code.dto.UserResponseDto;
import hexlet.code.mapper.UserMapper;
import hexlet.code.entity.User;
import hexlet.code.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing users.
 */
@RequiredArgsConstructor
@Service
public final class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Retrieves all users.
     *
     * @return List of UserResponseDto representing all users.
     */
    public List<UserResponseDto> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a user by ID.
     *
     * @param id The ID of the user.
     * @return UserResponseDto representing the user.
     */
    public UserResponseDto findById(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        return userMapper.toDto(user);
    }

    /**
     * Saves a new user.
     *
     * @param userRequestDto The data for creating a new user.
     * @return UserResponseDto representing the saved user.
     */
    public UserResponseDto save(UserRequestDto userRequestDto) {
        String encodedPassword = passwordEncoder.encode(userRequestDto.getPassword());
        userRequestDto.setPassword(encodedPassword);
        User user = userMapper.toEntity(userRequestDto);
        user.setCreatedAt(Instant.now());
        User resultUser = userRepository.save(user);
        return userMapper.toDto(resultUser);
    }

    /**
     * Deletes a user by ID.
     *
     * @param id The ID of the user to delete.
     */
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Updates a user by ID.
     *
     * @param id              The ID of the user to update.
     * @param userRequestDto The data for updating the user.
     * @return UserResponseDto representing the updated user.
     */
    public UserResponseDto updateById(Long id, UserRequestDto userRequestDto) {
        if (userRequestDto.getPassword() != null) {
            String encodedPassword = passwordEncoder.encode(userRequestDto.getPassword());
            userRequestDto.setPassword(encodedPassword);
        }
        User user = userRepository.findById(id).orElseThrow();
        User updated = userMapper.partialUpdate(userRequestDto, user);
        User saved = userRepository.save(updated);
        return userMapper.toDto(saved);
    }
}
