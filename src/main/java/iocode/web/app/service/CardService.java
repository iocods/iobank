package iocode.web.app.service;

import iocode.web.app.entity.*;
import iocode.web.app.repository.CardRepository;
import iocode.web.app.service.helper.AccountHelper;
import iocode.web.app.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * This class provides services related to credit/debit cards.
 * It interacts with the database to perform operations like creating a new card,
 * crediting/debiting the card, and retrieving card details.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CardService {

    private final CardRepository cardRepository;
    private final AccountHelper accountHelper;
    private final TransactionService transactionService;

    /**
     * Retrieves the card associated with the given user.
     *
     * @param user The user whose card needs to be retrieved.
     * @return The card associated with the given user.
     * @throws RuntimeException If no card is found for the given user.
     */
    public Card getCard(User user) {
        return cardRepository.findByOwnerUid(user.getUid()).orElseThrow();
    }

    /**
     * Creates a new credit/debit card for the given user and deducts the specified amount from the user's USD account.
     *
     * @param amount The amount to be deducted from the user's USD account.
     * @param user The user for whom the card needs to be created.
     * @return The newly created card.
     * @throws Exception If the amount is less than $2 or if the USD account is not found for the given user.
     */
    public Card createCard(double amount, User user) throws Exception {
        if(amount < 2) {
            throw new IllegalArgumentException("Amount should be at least $2");
        }
        if(!accountHelper.existsByCodeAndOwnerUid("USD", user.getUid())) {
            throw new IllegalArgumentException("USD Account not found for this user so card cannot be created");
        }
        var usdAccount = accountHelper.findByCodeAndOwnerUid("USD", user.getUid()).orElseThrow();
        accountHelper.validateSufficientFunds(usdAccount, amount);
        usdAccount.setBalance(usdAccount.getBalance() - amount);
        long cardNumber;
        do{
            cardNumber = generateCardNumber();
        } while (cardRepository.existsByCardNumber(cardNumber));
        Card card = Card.builder()
            .cardHolder(user.getFirstname() + " " + user.getLastname())
            .cardNumber(cardNumber)
            .exp(LocalDateTime.now().plusYears(3))
            .owner(user)
            .cvv(new RandomUtil().generateRandom(3).toString())
            .balance(amount - 1)
            .build();
        card = cardRepository.save(card);
        transactionService.createAccountTransaction(1, Type.WITHDRAW, 0.00, user, usdAccount);
        transactionService.createAccountTransaction(amount-1, Type.WITHDRAW, 0.00, user, usdAccount);
        transactionService.createCardTransaction(amount-1, Type.WITHDRAW, 0.00, user, card);
        accountHelper.save(usdAccount);
        return card;
    }

    /**
     * Generates a random 16-digit card number.
     *
     * @return A random 16-digit card number.
     */
    private long generateCardNumber() {
        return new RandomUtil().generateRandom(16);
    }

    /**
     * Credits the specified amount to the user's card and deducts it from the user's USD account.
     *
     * @param amount The amount to be credited to the user's card.
     * @param user The user whose card needs to be credited.
     * @return The transaction record for the credit operation.
     * @throws RuntimeException If the USD account is not found for the given user.
     */
    public Transaction creditCard(double amount, User user) {
        var usdAccount = accountHelper.findByCodeAndOwnerUid("USD", user.getUid()).orElseThrow();
        usdAccount.setBalance(usdAccount.getBalance() - amount);
        transactionService.createAccountTransaction(amount, Type.WITHDRAW, 0.00, user, usdAccount);
        var card = user.getCard();
        card.setBalance(card.getBalance() + amount);
        cardRepository.save(card);
        return transactionService.createCardTransaction(amount, Type.CREDIT, 0.00, user, card);
    }

    /**
     * Debits the specified amount from the user's card and adds it to the user's USD account.
     *
     * @param amount The amount to be debited from the user's card.
     * @param user The user whose card needs to be debited.
     * @return The transaction record for the debit operation.
     * @throws RuntimeException If the USD account is not found for the given user.
     */
    public Transaction debitCard(double amount, User user) {
        var usdAccount = accountHelper.findByCodeAndOwnerUid("USD", user.getUid()).orElseThrow();
        usdAccount.setBalance(usdAccount.getBalance() + amount);
        transactionService.createAccountTransaction(amount, Type.DEPOSIT, 0.00, user, usdAccount);
        var card = user.getCard();
        card.setBalance(card.getBalance() - amount);
        cardRepository.save(card);
        return transactionService.createCardTransaction(amount, Type.DEBIT, 0.00, user, card);
    }
}
