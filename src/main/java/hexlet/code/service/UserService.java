package hexlet.code.service;

import hexlet.code.entity.User;
import hexlet.code.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public User findById(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        return user;
    }

    public List<User> findAll() {
        List<User> users = userRepository.findAll();
        return users;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

}
