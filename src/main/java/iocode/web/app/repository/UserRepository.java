package iocode.web.app.repository;

import iocode.web.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    User findByUsernameIgnoreCase(String username);
}
