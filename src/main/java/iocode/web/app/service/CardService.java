package iocode.web.app.service;

import iocode.web.app.entity.*;
import iocode.web.app.repository.AccountRepository;
import iocode.web.app.repository.CardRepository;
import iocode.web.app.repository.TransactionRepository;
import iocode.web.app.service.helper.AccountHelper;
import iocode.web.app.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class CardService {

    private final CardRepository cardRepository;
    private final AccountHelper accountHelper;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public Card getCard(User user) {
        return cardRepository.findByOwnerUid(user.getUid()).orElseThrow();
    }

    public Card createCard(double amount, User user) throws Exception {
        if(amount < 2) {
            throw new IllegalArgumentException("Amount should be at least $2");
        }
        if(!accountRepository.existsByCodeAndOwnerUid("USD", user.getUid())) {
            throw new IllegalArgumentException("USD Account not found for this user so card cannot be created");
        }
        var usdAccount = accountRepository.findByCodeAndOwnerUid("USD", user.getUid()).orElseThrow();
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
        accountHelper.createAccountTransaction(1, Type.WITHDRAW, 0.00, user, usdAccount);
        accountHelper.createAccountTransaction(amount-1, Type.WITHDRAW, 0.00, user, usdAccount);
        createCardTransaction(amount-1, Type.WITHDRAW, 0.00, user, card);
        accountRepository.save(usdAccount);
        return card;
    }

    private long generateCardNumber() {
        return new RandomUtil().generateRandom(16);
    }

    public Transaction creditCard(double amount, User user) {
        var usdAccount = accountRepository.findByCodeAndOwnerUid("USD", user.getUid()).orElseThrow();
        usdAccount.setBalance(usdAccount.getBalance() - amount);
        accountHelper.createAccountTransaction(amount, Type.WITHDRAW, 0.00, user, usdAccount);
        var card = user.getCard();
        card.setBalance(card.getBalance() + amount);
        cardRepository.save(card);
        return createCardTransaction(amount, Type.CREDIT, 0.00, user, card);
    }

    public Transaction debitCard(double amount, User user) {
        var usdAccount = accountRepository.findByCodeAndOwnerUid("USD", user.getUid()).orElseThrow();
        usdAccount.setBalance(usdAccount.getBalance() + amount);
        accountHelper.createAccountTransaction(amount, Type.DEPOSIT, 0.00, user, usdAccount);
        var card = user.getCard();
        card.setBalance(card.getBalance() - amount);
        cardRepository.save(card);
        return createCardTransaction(amount, Type.DEBIT, 0.00, user, card);

    }

    private Transaction createCardTransaction(double amount, Type type, double txFee, User user, Card card) {
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
