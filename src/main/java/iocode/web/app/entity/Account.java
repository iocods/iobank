package iocode.web.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Represents an account entity in the application.
 *
 * @author Iocode
 * @since 1.0
 */
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    /**
     * Unique identifier for the account.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String accountId;

    /**
     * Current balance of the account.
     */
    private double balance;

    /**
     * Name of the account.
     */
    private String accountName;

    /**
     * Unique account number.
     */
    @Column(unique = true, nullable = false)
    private long accountNumber;

    /**
     * Currency of the account.
     */
    private String currency;

    /**
     * Code associated with the account.
     */
    private String code;

    /**
     * Label for the account.
     */
    private String label;

    /**
     * Symbol representing the account.
     */
    private char symbol;

    /**
     * Timestamp of the last update to the account.
     */
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    /**
     * Timestamp of the account creation.
     */
    @CreationTimestamp
    private LocalDateTime createdAt;

    /**
     * User who owns the account.
     * This field is annotated with {@link JsonIgnore} to prevent infinite recursion during JSON serialization.
     */
    @ManyToOne
    @JoinColumn(name = "owner_id")
    @JsonIgnore
    private User owner;

    /**
     * List of transactions associated with the account.
     * This field is annotated with {@link OneToMany} to establish a one-to-many relationship with the {@link Transaction} entity.
     * The {@link CascadeType.ALL} option is used to propagate changes to the transactions.
     * The {@link FetchType.LAZY} option is used to fetch the transactions lazily.
     */
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions;
}