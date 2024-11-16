package iocode.web.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Represents a financial transaction in the application.
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
public class Transaction {

    /**
     * Unique identifier for the transaction.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String txId;

    /**
     * The amount of money involved in the transaction.
     */
    private Double amount;

    /**
     * The transaction fee associated with the transaction.
     */
    private Double txFee;

    /**
     * The identifier of the sender of the transaction.
     */
    private String sender;

    /**
     * The identifier of the receiver of the transaction.
     */
    private String receiver;

    private String description;

    /**
     * The timestamp of the last update to the transaction.
     */
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    /**
     * The timestamp of the creation of the transaction.
     */
    @CreationTimestamp
    private LocalDateTime createdAt;

    /**
     * The status of the transaction.
     */
    @Enumerated(value = EnumType.STRING)
    private Status status;

    /**
     * The type of the transaction.
     */
    @Enumerated(value = EnumType.STRING)
    private Type type;

    /**
     * The card associated with the transaction.
     */
    @ManyToOne
    @JoinColumn(name = "card_id")
    @JsonIgnore
    private Card card;

    /**
     * The user who owns the transaction.
     */
    @ManyToOne
    @JoinColumn(name = "owner_id")
    @JsonIgnore
    private User owner;

    /**
     * The account associated with the transaction.
     */
    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonIgnore
    private Account account;

}