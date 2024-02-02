package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserDao;
import ru.kata.spring.boot_security.demo.security.SecurityUserDetails;

@Service
public class UserServiceDetailsImpl implements UserDetailsService {
    private final UserDao userDao;

    public UserServiceDetailsImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userDaoByEmail = userDao.findByEmail(username);
        User userDaoByUserName = userDao.findByUserName(username);

        if (userDaoByUserName != null) {
            return new SecurityUserDetails(userDaoByUserName);
        } else if (userDaoByEmail != null) {
            return new SecurityUserDetails(userDaoByEmail);
        } else {
            throw new UsernameNotFoundException("User not found!");
        }
    }
}
