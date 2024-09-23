package iocode.web.app.repository;

import iocode.web.app.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardRepository  extends JpaRepository<Card, String> {
    boolean existsByCardNumber(double cardNumber);

    Optional<Card> findByOwnerUid(String uid);

}
