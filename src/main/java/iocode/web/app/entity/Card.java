package iocode.web.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Represents a credit or debit card entity.
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
public class Card {

    /**
     * Unique identifier for the card.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String cardId;

    /**
     * Unique card number.
     */
    @Column(nullable = false, unique = true)
    private long cardNumber;

    /**
     * Card holder's name.
     */
    private String cardHolder;

    /**
     * Current balance on the card.
     */
    private Double balance;

    /**
     * Date of issuance.
     */
    @CreationTimestamp
    private LocalDate iss;

    /**
     * Last updated timestamp.
     */
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    /**
     * Expiration date of the card.
     */
    private LocalDateTime exp;

    /**
     * Card verification value.
     */
    private String cvv;

    /**
     * Personal identification number.
     */
    private String pin;

    /**
     * Billing address associated with the card.
     */
    private String billingAddress;

    /**
     * User who owns this card.
     *
     * @see User
     */
    @OneToOne
    @JoinColumn(name = "owner_id")
    @JsonIgnore
    private User owner;

    /**
     * List of transactions associated with this card.
     *
     * @see Transaction
     */
    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Transaction> transactions;
}
