package iocode.web.app.service;

import iocode.web.app.entity.*;
import iocode.web.app.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    public List<Transaction> getAllTransactions(String page, User user) {
        Pageable pageable = PageRequest.of(Integer.parseInt(page), 10, Sort.by("createdAt").ascending());
        return transactionRepository.findAllByOwnerUid(user.getUid(), pageable).getContent();
    }

    public List<Transaction> getTransactionsByCardId(String cardId, String page, User user) {
        Pageable pageable = PageRequest.of(Integer.parseInt(page), 10, Sort.by("createdAt").ascending());
        return transactionRepository.findAllByCardCardIdAndOwnerUid(cardId, user.getUid(), pageable).getContent();
    }

    public List<Transaction> getTransactionsByAccountId(String accountId, String page, User user) {
        Pageable pageable = PageRequest.of(Integer.parseInt(page), 10, Sort.by("createdAt").ascending());
        return transactionRepository.findAllByAccountAccountIdAndOwnerUid(accountId, user.getUid(), pageable).getContent();
    }

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
