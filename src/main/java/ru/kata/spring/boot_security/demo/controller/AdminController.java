package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String toAdmin() {
        return "admin";
    }

    @GetMapping("/users")
    public String findAllUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("allRoles", roleService.findAll());
        return "users";
    }

    @GetMapping("/users/{id}")
    public String findUserById(Model model, @PathVariable Long id) {
        model.addAttribute("user", userService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
        model.addAttribute("allRoles", roleService.findAll());
        return "user";
    }

    @PostMapping("/users")
    public String addUser(User user, @RequestParam String rawPassword) {
        user.setPassword(passwordEncoder.encode(rawPassword));
        userService.add(user);
        return "redirect:/admin/users";
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        if (!userService.removeById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/admin/users";
    }

    @PutMapping("/users/{id}")
    public String updateUser(User user, @RequestParam String rawPassword) {
        if (!rawPassword.isEmpty()) {
            user.setPassword(passwordEncoder.encode(rawPassword));
        }
        if (!userService.update(user)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/admin/users";
    }
}