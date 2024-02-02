package ru.kata.spring.boot_security.demo.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceDetailsImpl;

import java.util.Collection;

@Component
public class AuthProvider implements AuthenticationProvider {

    private final UserServiceDetailsImpl userService;


    @Autowired
    public AuthProvider(UserServiceDetailsImpl userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        PasswordEncoder passwordEncoder = getPasswordEncoder();

        UserDetails userDetails = userService.loadUserByUsername(username);

        if (userDetails != null) {
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("Wrong password");
            }

            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

            return new UsernamePasswordAuthenticationToken(userDetails, password, authorities);
        } else {
            throw new BadCredentialsException("Username not found");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }

    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
