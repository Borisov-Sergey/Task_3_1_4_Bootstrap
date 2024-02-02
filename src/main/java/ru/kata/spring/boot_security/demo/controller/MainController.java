package ru.kata.spring.boot_security.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.security.SecurityUserDetails;
import ru.kata.spring.boot_security.demo.service.RegistrationServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class MainController {

    private final UserService userService;
    private final RegistrationServiceImpl registrationService;

    @Autowired
    public MainController(UserService userService, RegistrationServiceImpl registrationService) {
        this.userService = userService;
        this.registrationService = registrationService;
    }


    @GetMapping(value = "admin")
    public String allUser(@AuthenticationPrincipal SecurityUserDetails userDetails, Model model) {
        model.addAttribute("users", userService.getAll());
        model.addAttribute("userActive", userDetails.getUser());
        model.addAttribute("newUser", new User());
        model.addAttribute("roles", Role.values());
        if (userDetails.getUser().getRoles().contains(Role.ADMIN)) {
            model.addAttribute("roleBoolean", true);
        }
        return "adminPage";
    }


    @PostMapping(value = "admin/update")
    public String updateUser(@ModelAttribute User user) {
        userService.update(user);
        return "redirect:/admin";
    }

    @PostMapping(value = "admin/delete")
    public String deleteUser(@RequestParam(value = "id", required = false) Long id) {
        userService.removeById(id);
        return "redirect:/admin";
    }

    @GetMapping(value = "user")
    public String userAccount(@AuthenticationPrincipal SecurityUserDetails userDetails, Model model) {
        model.addAttribute("user", userDetails.getUser());
        if (userDetails.getUser().getRoles().contains(Role.ADMIN)) {
            model.addAttribute("roleBoolean", true);
        }
        return "userPage";
    }


    @PostMapping(value = "admin/new")
    public String newUser(@ModelAttribute("user") User user) {
        registrationService.register(user);
        return "redirect:/admin";
    }

}
