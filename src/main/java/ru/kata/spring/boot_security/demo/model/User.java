package ru.kata.spring.boot_security.demo.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email") // Уникальность email
})
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Имя обязательно")
    @Pattern(regexp = "^[A-Za-zА-Яа-яЁё\\s\\-]+$", message = "Допустимы только буквы, пробелы и дефисы")
    private String firstName;

    @NotBlank(message = "Фамилия обязательна")
    @Pattern(regexp = "^[A-Za-zА-Яа-яЁё\\s\\-]+$", message = "Допустимы только буквы, пробелы и дефисы")
    private String lastName;


    // ОДНО объявление email с валидациями
    @Email(message = "Электронная почта должна быть верной")
    @NotBlank(message = "Email обязателен")
    @Column(unique = true) // Уникальность в БД
    private String email;

    @NotBlank(message = "Имя пользователя обязательно")
    private String username;

    @NotBlank(message = "Пароль обязателен")
    private String password;

    @Column(nullable = true) // Разрешает NULL
    private Integer age;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;


    public User() {
    }

    public User(String username, String password, String firstName, String lastName, String email, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.roles = roles;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    // UserDetails methods
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
       if (this == o) {
           return true;
        }

        if (!(o instanceof User)) {
           return false;
        }

       User user = (User) o;

      return id != null && id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}