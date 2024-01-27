package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void save(User user);

    List<User> getAll();

    void update(User user);

    void removeById(Long id);

    User getById(Long id);

    User findByUserName(String username);
}
