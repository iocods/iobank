package iocode.web.app.repository;

import iocode.web.app.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Account} entities.
 * Extends Spring Data JPA's {@link JpaRepository} to provide basic CRUD operations.
 * Additional methods are provided to perform specific queries on {@link Account} entities.
 */
public interface AccountRepository extends JpaRepository<Account, String> {

    /**
     * Checks if an account with the given account number exists.
     *
     * @param accountNumber the account number to check
     * @return true if an account with the given account number exists, false otherwise
     */
    boolean existsByAccountNumber(Long accountNumber);

    /**
     * Checks if an account with the given code and owner UID exists.
     *
     * @param code the code to check
     * @param uid the owner UID to check
     * @return true if an account with the given code and owner UID exists, false otherwise
     */
    boolean existsByCodeAndOwnerUid(String code, String uid);

    /**
     * Retrieves all accounts owned by the user with the given UID.
     *
     * @param uid the owner UID
     * @return a list of accounts owned by the user with the given UID
     */
    List<Account> findAllByOwnerUid(String uid);

    /**
     * Retrieves the account with the given code and owner UID.
     *
     * @param code the code to search for
     * @param uid the owner UID to search for
     * @return an {@link Optional} containing the account with the given code and owner UID,
     * or an empty {@link Optional} if no such account exists
     */
    Optional<Account> findByCodeAndOwnerUid(String code, String uid);

    /**
     * Retrieves the account with the given account number.
     *
     * @param recipientAccountNumber the account number to search for
     * @return an {@link Optional} containing the account with the given account number,
     * or an empty {@link Optional} if no such account exists
     */
    Optional<Account> findByAccountNumber(long recipientAccountNumber);

    Optional<Account> findByCodeAndAccountNumber(String code, long recipientAccountNumber);
}
