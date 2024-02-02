package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserDao;
import ru.kata.spring.boot_security.demo.security.AuthProvider;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final AuthProvider authProvider;

    @Autowired
    public UserServiceImpl(UserDao userDao, AuthProvider authProvider) {
        this.userDao = userDao;
        this.authProvider = authProvider;
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public void save(User user) {
        userDao.save(user);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<User> getAll() {
        return userDao.findAll();
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public void update(User user) {
        if (ObjectUtils.isEmpty(user.getPassword())) {
            userDao.findById(user.getId()).map(User::getPassword).ifPresent(user::setPassword);
            userDao.save(user);
            return;
        }
        user.setPassword(authProvider.getPasswordEncoder().encode(user.getPassword()));
        userDao.save(user);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public void removeById(Long id) {
        User user = userDao.getById(id);
        userDao.delete(user);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public User getById(Long id) {
        return userDao.getById(id);
    }

    @Override
    public User findByUserName(String username) {
        return userDao.findByUserName(username);
    }

    @Override
    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

}
