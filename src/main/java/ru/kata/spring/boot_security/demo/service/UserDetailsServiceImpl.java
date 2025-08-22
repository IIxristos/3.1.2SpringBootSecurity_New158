package ru.kata.spring.boot_security.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    // Добавляем конструктор с зависимостью
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        logger.debug("Attempting to load user by username or email: {}", usernameOrEmail);

        // Сначала пробуем найти по username
        User user = userRepository.findByUsername(usernameOrEmail);

        // Если не нашли, пробуем по email
        if (user == null) {
            user = userRepository.findByEmail(usernameOrEmail);
        }

        if (user == null) {
            logger.error("User not found with username or email: {}", usernameOrEmail);
            throw new UsernameNotFoundException(String.format("User '%s' not found", usernameOrEmail));
        }

        logger.info("Successfully loaded user: {}", user.getUsername());
        logger.debug("User details: username={}, email={}, roles={}",
                user.getUsername(), user.getEmail(), user.getRoles());

        return user;
    }
}