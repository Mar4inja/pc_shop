package de.ait.pcshop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @Column(name = "country")
    private String country;

    @Column(name = "post_index")
    private String postIndex;

    @Column(name = "city")
    private String city;

    @Column(name = "street")
    private String street;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;

    public User(String title, String firstName, String lastName, String email, String password, String country, String city, String street, String postIndex) {
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.country = country;
        this.city = city;
        this.street = street;
        this.postIndex = postIndex;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Можно добавить логику проверки на истекший срок учетной записи
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Можно добавить логику проверки на блокировку учетной записи
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Можно добавить логику проверки на истекшие учетные данные
    }

    @Override
    public boolean isEnabled() {
        return isActive; // Можно добавить дополнительную логику активации учетной записи
    }
}
