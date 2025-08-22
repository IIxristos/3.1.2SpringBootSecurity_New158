package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import java.util.List;
import java.util.Set;

public interface UserService {
    void saveUser(User user);
    void updateUser(User user);
    void deleteUser(long id);
    void createUserWithRoles(User user, Set<String> roleNames);
    void updateUserWithRoles(User user, Set<String> roleNames);
    User getUser(long id);
    User findByUsername(String username);
    List<User> getAllUsers();
    Set<Role> getAllRoles();
    Role getRoleByName(String name);
    void saveRole(Role role);
    void addRoleToUser(String username, String roleName);

}