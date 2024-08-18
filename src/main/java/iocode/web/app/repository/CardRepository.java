package iocode.web.app.repository;

import iocode.web.app.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository  extends JpaRepository<Card, String> {
}
