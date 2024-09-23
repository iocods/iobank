package iocode.web.app.service.helper;

import iocode.web.app.dto.AccountDto;
import iocode.web.app.dto.ConvertDto;
import iocode.web.app.entity.*;
import iocode.web.app.repository.AccountRepository;
import iocode.web.app.repository.TransactionRepository;
import iocode.web.app.service.ExchangeRateService;
import iocode.web.app.service.TransactionService;
import iocode.web.app.util.RandomUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.naming.OperationNotSupportedException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Getter
public class AccountHelper {

    private final AccountRepository accountRepository;
    private final TransactionService transactionService;
    private final ExchangeRateService exchangeRateService;


    private final Map<String, String> CURRENCIES = Map.of(
        "USD", "United States Dollar",
        "EUR", "Euro",
        "GBP", "British Pound",
        "JPY", "Japanese Yen",
        "NGN", "Nigerian Naira",
        "INR", "Indian Rupee"
    );


    public Account createAccount(AccountDto accountDto, User user) throws Exception {
        long accountNumber;
        validateAccountNonExistsForUser(accountDto.getCode(), user.getUid());
        do{
            accountNumber = new RandomUtil().generateRandom(10);
        } while(accountRepository.existsByAccountNumber(accountNumber));

        var account = Account.builder()
            .accountNumber(accountNumber)
            .accountName(user.getFirstname() + " " + user.getLastname())
            .balance(1000)
            .owner(user)
            .code(accountDto.getCode())
            .symbol(accountDto.getSymbol())
            .label(CURRENCIES.get(accountDto.getCode()))
            .build();
        return accountRepository.save(account);
    }

    public Transaction performTransfer(Account senderAccount, Account receiverAccount, double amount, User user) throws Exception {
        // Implementation of transfer logic goes here
        validateSufficientFunds(senderAccount, (amount * 1.01));
        senderAccount.setBalance(senderAccount.getBalance() - amount * 1.01);
        receiverAccount.setBalance(receiverAccount.getBalance() + amount);
        accountRepository.saveAll(List.of(senderAccount, receiverAccount));
        var senderTransaction = transactionService.createAccountTransaction(amount, Type.WITHDRAW, amount * 0.01, user, senderAccount);
        var receiverTransaction = transactionService.createAccountTransaction(amount, Type.DEPOSIT, 0.00, receiverAccount.getOwner(), receiverAccount);

        return senderTransaction;
    }

    public void validateAccountNonExistsForUser(String code, String uid) throws Exception {
        if(accountRepository.existsByCodeAndOwnerUid(code, uid)) {
            throw new Exception("Account of this type already exist for this user");
        }
    }

    public void validateAccountOwner(Account account, User user) throws OperationNotSupportedException {
        if(!account.getOwner().getUid().equals(user.getUid())) {
            throw new OperationNotSupportedException("Invalid account owner");
        }
    }

    public void validateSufficientFunds(Account account, double amount) throws Exception {
        if(account.getBalance() < amount) {
            throw new OperationNotSupportedException("Insufficient funds in the account");
        }
    }

    public void validateAmount(double amount) throws Exception {
        if(amount <= 0) {
            throw new IllegalArgumentException("Invalid amount");
        }
    }

    public void validateDifferentCurrencyType(ConvertDto convertDto) throws Exception {
        if(convertDto.getToCurrency().equals(convertDto.getFromCurrency())){
            throw new IllegalArgumentException("Conversion between the same currency types is not allowed");
        }
    }

    public void validateAccountOwnership(ConvertDto convertDto, String uid) throws Exception {
        accountRepository.findByCodeAndOwnerUid(convertDto.getFromCurrency(), uid).orElseThrow();
        accountRepository.findByCodeAndOwnerUid(convertDto.getToCurrency(), uid).orElseThrow();
    }
    public void validateAccountOwnership(String code, String uid) throws Exception {
        accountRepository.findByCodeAndOwnerUid(code, uid).orElseThrow();
    }

    public void validateConversion(ConvertDto convertDto, String uid) throws Exception {
        validateDifferentCurrencyType(convertDto);
        validateAccountOwnership(convertDto, uid);
        validateAmount(convertDto.getAmount());
        validateSufficientFunds(accountRepository.findByCodeAndOwnerUid(convertDto.getFromCurrency(), uid).get(), convertDto.getAmount());
    }

    public Transaction convertCurrency(ConvertDto convertDto, User user) throws Exception {
        validateConversion(convertDto, user.getUid());
        var rates = exchangeRateService.getRates();
        var sendingRates = rates.get(convertDto.getFromCurrency());
        var receivingRates = rates.get(convertDto.getToCurrency());
        var computedAmount = (receivingRates/sendingRates) * convertDto.getAmount();
        Account fromAccount = accountRepository.findByCodeAndOwnerUid(convertDto.getFromCurrency(), user.getUid()).orElseThrow();
        Account toAccount = accountRepository.findByCodeAndOwnerUid(convertDto.getToCurrency(), user.getUid()).orElseThrow();
        fromAccount.setBalance(fromAccount.getBalance() - (convertDto.getAmount() * 1.01));
        toAccount.setBalance(toAccount.getBalance() + computedAmount);
        accountRepository.saveAll(List.of(fromAccount, toAccount));

        var fromAccountTransaction = transactionService.createAccountTransaction(convertDto.getAmount(), Type.CONVERSION, convertDto.getAmount() * 0.01, user, fromAccount);
        var toAccountTransaction = transactionService.createAccountTransaction(computedAmount, Type.DEPOSIT, convertDto.getAmount() * 0.00, user, toAccount);
        return fromAccountTransaction;
    }

    public boolean existsByCodeAndOwnerUid(String code, String uid) {
        return accountRepository.existsByCodeAndOwnerUid(code, uid);
    }

    public Optional<Account> findByCodeAndOwnerUid(String code, String uid) {
        return accountRepository.findByCodeAndOwnerUid(code, uid);
    }

    public Account save(Account usdAccount) {
        return accountRepository.save(usdAccount);
    }
}
