package ru.kata.spring.boot_security.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.security.SecurityUserDetails;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/home")
public class MainController {

    private final UserService userService;

    @Autowired
    public MainController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String home() {
        return "index";
    }


    @GetMapping(value = "/admin")
    public String allUser(Model model) {
        model.addAttribute("user", userService.getAll());
        return "adminPage";
    }

    @GetMapping(value = "/admin/update")
    public String getUpdate(@RequestParam(value = "id", required = false) Long id, Model model) {
        User user = userService.getById(id);
        model.addAttribute("user", user);
        return "updateAccount";
    }

    @PostMapping(value = "/admin/update")
    public String updateUser(@ModelAttribute User user) {
        userService.update(user);
        return "redirect:/home/admin";
    }

    @GetMapping(value = "/admin/delete")
    public String deleteUser(@RequestParam(value = "id", required = false) Long id) {
        userService.removeById(id);
        return "redirect:/home/admin";
    }

    @GetMapping(value = "/user")
    public String userAccount(@AuthenticationPrincipal SecurityUserDetails userDetails, Model model) {
        model.addAttribute("user", userDetails.getUser());
        return "userPage";
    }

    @GetMapping(value = "/admin/edit")
    public String editRule(@RequestParam(value = "id", required = false) Long id, Model model) {
        model.addAttribute("user", userService.getById(id));
        model.addAttribute("roles", Role.values());
        return "addRole";
    }

    @PostMapping(value = "/admin/edit")
    public String saveRule(@RequestParam String userName,
                           @RequestParam Map<String, String> form,
                           @RequestParam(value = "id", required = false) Long id) {
        User user = userService.getById(id);
        user.setUserName(userName);

        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        user.getRoles().clear();

        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }

        userService.save(user);

        return "redirect:/home/admin";
    }

}
