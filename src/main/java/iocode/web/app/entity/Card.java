package iocode.web.app.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String cardId;

    @Column(nullable = false, unique = true)
    private long cardNumber;
    private String cardHolder;
    private Double balance;
    @CreationTimestamp
    private LocalDate iss;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private LocalDateTime exp;
    private String cvv;
    private String pin;
    private String billingAddress;

    @OneToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions;
}
