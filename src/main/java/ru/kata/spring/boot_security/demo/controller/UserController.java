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
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final RegistrationServiceImpl registrationService;
    private final RoleService roleService;

    @Autowired

    public UserController(UserService userService, RegistrationServiceImpl registrationService, RoleService roleService) {
        this.userService = userService;
        this.registrationService = registrationService;
        this.roleService = roleService;
    }


    @GetMapping(value = "/admin")
    public String getTableUser(@AuthenticationPrincipal SecurityUserDetails userDetails, Model model) {
        model.addAttribute("users", userService.getAll());
        model.addAttribute("userActive", userDetails.getUser());
        model.addAttribute("newUser", new User());
        model.addAttribute("allRoles", roleService.getAllRoles());
        if (userDetails.getUser().getRoles().toString().contains("ADMIN")) {
            model.addAttribute("roleBoolean", true);
        }
        return "adminPage";
    }


    @PostMapping(value = "/update")
    public String postUpdateUser(@ModelAttribute User user, @RequestParam(value = "rls") String[] roles) {
        Set<Role> resultRole = new HashSet<>();
        for (String role : roles) {
            resultRole.add(roleService.findByName(role));
        }
        user.setRoles(resultRole);
        userService.update(user);
        return "redirect:/user/admin";
    }

    @PostMapping(value = "/delete")
    public String postDeleteUser(@RequestParam(value = "id", required = false) Long id) {
        userService.removeById(id);
        return "redirect:/user/admin";
    }

    @GetMapping
    public String getUserAccount(@AuthenticationPrincipal SecurityUserDetails userDetails, Model model) {
        model.addAttribute("user", userDetails.getUser());
        if (userDetails.getUser().getRoles().toString().contains("ADMIN")) {
            model.addAttribute("roleBoolean", true);
        }
        return "userPage";
    }


    @PostMapping(value = "/save")
    public String postSaveNewUser(@ModelAttribute("user") User user, @RequestParam(value = "rlss") String[] roles) {

        Set<Role> resultRole = new HashSet<>();
        for (String role : roles) {
            resultRole.add(roleService.findByName(role));
        }

        user.setRoles(resultRole);
        registrationService.register(user);
        return "redirect:/user/admin";
    }

}
