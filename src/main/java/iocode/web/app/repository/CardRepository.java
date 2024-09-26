package iocode.web.app.repository;

import iocode.web.app.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * This interface represents a repository for managing {@link Card} entities.
 * It extends Spring Data JPA's {@link JpaRepository} to provide basic CRUD operations.
 *
 * @author YourName
 * @since 1.0
 */
public interface CardRepository extends JpaRepository<Card, String> {

    /**
     * Checks if a card with the given card number exists in the database.
     *
     * @param cardNumber the card number to check
     * @return {@code true} if a card with the given card number exists, {@code false} otherwise
     */
    boolean existsByCardNumber(double cardNumber);

    /**
     * Finds a {@link Card} entity by its owner's unique identifier (UID).
     *
     * @param uid the owner's UID
     * @return an {@link Optional} containing the {@link Card} entity if found, or an empty {@link Optional} if not found
     */
    Optional<Card> findByOwnerUid(String uid);
}
