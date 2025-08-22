package ru.kata.spring.boot_security.demo.configs;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override

    public void run(String... args) throws Exception {
        // Создаем роли, если их нет
        if (userService.getAllRoles().isEmpty()) {
            Role adminRole = new Role("ROLE_ADMIN");
            Role userRole = new Role("ROLE_USER");
            userService.saveRole(adminRole);
            userService.saveRole(userRole);
            System.out.println("Created roles: ROLE_ADMIN and ROLE_USER");
        }

        // Создаем администратора, если его нет
        if (userService.findByUsername("admin") == null) {
            String adminPassword = "admin";
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(adminPassword);
            admin.setFirstName("Admin");
            admin.setLastName("Adminov");
            admin.setEmail("admin@mail.ru");
            admin.setRoles(Set.of(
                    userService.getRoleByName("ROLE_ADMIN"),
                    userService.getRoleByName("ROLE_USER")
            ));
            userService.saveUser(admin);
            System.out.println("Created admin user with username: admin and password: " + adminPassword);
        }

        // Создаем обычного пользователя, если его нет
        if (userService.findByUsername("user") == null) {
            String userPassword = "user";
            User user = new User();
            user.setUsername("user");
            user.setPassword(userPassword);
            user.setFirstName("User");
            user.setLastName("Userov");
            user.setEmail("user@mail.ru");
            user.setRoles(Set.of(
                    userService.getRoleByName("ROLE_USER")
            ));
            userService.saveUser(user);
            System.out.println("Created user with username: user and password: " + userPassword);
        }
    }
}