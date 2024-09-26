package iocode.web.app.service;

import iocode.web.app.entity.*;
import iocode.web.app.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This service class provides methods for managing transactions.
 * It interacts with the TransactionRepository to perform CRUD operations.
 */
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    /**
     * Retrieves all transactions for a given user, paginated.
     *
     * @param page The page number to retrieve.
     * @param user The user whose transactions are to be retrieved.
     * @return A list of transactions for the given user, paginated.
     */
    public List<Transaction> getAllTransactions(String page, User user) {
        Pageable pageable = PageRequest.of(Integer.parseInt(page), 10, Sort.by("createdAt").ascending());
        return transactionRepository.findAllByOwnerUid(user.getUid(), pageable).getContent();
    }

    /**
     * Retrieves transactions for a given card and user, paginated.
     *
     * @param cardId The ID of the card whose transactions are to be retrieved.
     * @param page The page number to retrieve.
     * @param user The user whose transactions are to be retrieved.
     * @return A list of transactions for the given card and user, paginated.
     */
    public List<Transaction> getTransactionsByCardId(String cardId, String page, User user) {
        Pageable pageable = PageRequest.of(Integer.parseInt(page), 10, Sort.by("createdAt").ascending());
        return transactionRepository.findAllByCardCardIdAndOwnerUid(cardId, user.getUid(), pageable).getContent();
    }

    /**
     * Retrieves transactions for a given account and user, paginated.
     *
     * @param accountId The ID of the account whose transactions are to be retrieved.
     * @param page The page number to retrieve.
     * @param user The user whose transactions are to be retrieved.
     * @return A list of transactions for the given account and user, paginated.
     */
    public List<Transaction> getTransactionsByAccountId(String accountId, String page, User user) {
        Pageable pageable = PageRequest.of(Integer.parseInt(page), 10, Sort.by("createdAt").ascending());
        return transactionRepository.findAllByAccountAccountIdAndOwnerUid(accountId, user.getUid(), pageable).getContent();
    }

    /**
     * Creates a new transaction for a given account.
     *
     * @param amount The amount of the transaction.
     * @param type The type of the transaction.
     * @param txFee The transaction fee.
     * @param user The user associated with the transaction.
     * @param account The account associated with the transaction.
     * @return The newly created transaction.
     */
    public Transaction createAccountTransaction(double amount, Type type, double txFee, User user, Account account) {
        var tx = Transaction.builder()
            .txFee(txFee)
            .amount(amount)
            .type(type)
            .status(Status.COMPLETED)
            .owner(user)
            .account(account)
            .build();
        return transactionRepository.save(tx);
    }

    /**
     * Creates a new transaction for a given card.
     *
     * @param amount The amount of the transaction.
     * @param type The type of the transaction.
     * @param txFee The transaction fee.
     * @param user The user associated with the transaction.
     * @param card The card associated with the transaction.
     * @return The newly created transaction.
     */
    public Transaction createCardTransaction(double amount, Type type, double txFee, User user, Card card) {
        Transaction tx = Transaction.builder()
            .txFee(txFee)
            .amount(amount)
            .type(type)
            .card(card)
            .status(Status.COMPLETED)
            .owner(user)
            .build();
        return transactionRepository.save(tx);
    }
}
