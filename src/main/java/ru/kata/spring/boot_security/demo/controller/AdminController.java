package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import jakarta.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String adminPage(@AuthenticationPrincipal User currentUser, Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("roles", userService.getAllRoles());
        return "admin/index";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", userService.getAllRoles());
        return "admin/new";
    }

    @PostMapping
    public String create(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult,
                         @RequestParam(value = "selectedRoles", required = false) Set<String> selectedRoles,
                         Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("allRoles", userService.getAllRoles());
            return "admin/new";
        }

        try {
            userService.createUserWithRoles(user, selectedRoles);
            return "redirect:/admin";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при создании пользователя: " + e.getMessage());
            model.addAttribute("allRoles", userService.getAllRoles());
            return "admin/new";
        }
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") long id, Model model) {
        User user = userService.getUser(id);
        if (user == null) {
            return "redirect:/admin";
        }
        model.addAttribute("user", user);
        model.addAttribute("allRoles", userService.getAllRoles());
        return "admin/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") User user,
                         @RequestParam(value = "selectedRoles", required = false) Set<String> selectedRoles,
                         Model model) {

        try {
            // Установим роли вручную перед обновлением
            if (selectedRoles != null && !selectedRoles.isEmpty()) {
                Set<Role> roles = selectedRoles.stream()
                        .map(userService::getRoleByName)
                        .collect(Collectors.toSet());
                user.setRoles(roles);
            }

            userService.updateUser(user);  // Без updateUserWithRoles, т.к. роли уже установлены вручную
            return "redirect:/admin";

        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при обновлении пользователя: " + e.getMessage());
            model.addAttribute("allRoles", userService.getAllRoles());
            return "admin/edit";
        }
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
