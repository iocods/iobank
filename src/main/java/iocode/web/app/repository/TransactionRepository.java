package iocode.web.app.repository;

import iocode.web.app.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.net.ContentHandler;
import java.util.List;

/**
 * This interface extends JpaRepository for managing Transaction entities.
 * It provides methods for retrieving transactions based on various criteria.
 */
public interface TransactionRepository extends JpaRepository<Transaction, String> {

    /**
     * Retrieves a page of transactions for the specified user.
     *
     * @param uid       The unique identifier of the user.
     * @param pageable  The pageable object for pagination.
     * @return          A page of transactions for the specified user.
     */
    Page<Transaction> findAllByOwnerUid(String uid, Pageable pageable);

    /**
     * Retrieves a page of transactions for the specified user and card.
     *
     * @param cardId    The unique identifier of the card.
     * @param uid       The unique identifier of the user.
     * @param pageable  The pageable object for pagination.
     * @return          A page of transactions for the specified user and card.
     */
    Page<Transaction> findAllByCardCardIdAndOwnerUid(String cardId, String uid, Pageable pageable);

    /**
     * Retrieves a page of transactions for the specified user and account.
     *
     * @param accountId The unique identifier of the account.
     * @param uid       The unique identifier of the user.
     * @param pageable  The pageable object for pagination.
     * @return          A page of transactions for the specified user and account.
     */
    Page<Transaction> findAllByAccountAccountIdAndOwnerUid(String accountId, String uid, Pageable pageable);
}
