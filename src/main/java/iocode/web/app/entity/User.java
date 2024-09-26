package iocode.web.app.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Represents a user in the banking application. Implements the UserDetails interface for Spring Security.
 *
 * @author Iocode
 * @version 1.0
 */
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bank_user")
public class User implements UserDetails {

    /**
     * Unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uid;

    /**
     * First name of the user.
     */
    private String firstname;

    /**
     * Last name of the user.
     */
    private String lastname;

    /**
     * Unique username for the user.
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * Date of birth of the user.
     */
    private Date dob;

    /**
     * Telephone number of the user.
     */
    private long tel;

    /**
     * Additional tag for the user.
     */
    private String tag;

    /**
     * Encrypted password for the user.
     */
    @JsonIgnore
    private String password;

    /**
     * Gender of the user.
     */
    private String gender;

    /**
     * Timestamp of when the user was created.
     */
    @CreationTimestamp
    private LocalDateTime createdAt;

    /**
     * Timestamp of when the user was last updated.
     */
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    /**
     * List of roles assigned to the user.
     */
    private List<String> roles;

    /**
     * One-to-one relationship with the Card entity, representing the user's card.
     */
    @OneToOne(mappedBy = "owner")
    @JsonIgnore
    private Card card;

    /**
     * One-to-many relationship with the Transaction entity, representing the user's transactions.
     */
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Transaction> transactions;

    /**
     * One-to-many relationship with the Account entity, representing the user's accounts.
     */
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Account> accounts;

    /**
     * Returns a collection of granted authorities based on the user's roles.
     *
     * @return Collection of granted authorities.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}

