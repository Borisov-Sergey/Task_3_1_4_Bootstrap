package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserDao;
import ru.kata.spring.boot_security.demo.security.AuthProvider;

@Service
public class RegistrationServiceImpl {

    private final UserDao userDao;
    private final AuthProvider authProvider;

    @Autowired
    public RegistrationServiceImpl(UserDao userDao, AuthProvider authProvider) {
        this.userDao = userDao;
        this.authProvider = authProvider;
    }

    @Transactional
    public void register(User user) {
        user.setPassword(authProvider.getPasswordEncoder().encode(user.getPassword()));
        userDao.save(user);
    }

}
