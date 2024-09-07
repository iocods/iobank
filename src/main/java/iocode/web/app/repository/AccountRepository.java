package iocode.web.app.repository;

import iocode.web.app.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, String> {
    boolean existsByAccountNumber(Long accountNumber);

    boolean existsByCodeAndOwnerUid(String code, String uid);

    List<Account> findAllByOwnerUid(String uid);
}
