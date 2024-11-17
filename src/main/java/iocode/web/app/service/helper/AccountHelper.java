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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger logger = LoggerFactory.getLogger(AccountHelper.class);

    private final Map<String, String> CURRENCIES = Map.of(
        "USD", "United States Dollar",
        "EUR", "Euro",
        "GBP", "British Pound",
        "JPY", "Japanese Yen",
        "NGN", "Nigerian Naira",
        "INR", "Indian Rupee"
    );


    /**
     * Creates a new account for the given user based on the provided account details.
     *
     * @param accountDto The account details to be used for creating the new account.
     * @param user The user for whom the account is being created.
     * @return The newly created account.
     * @throws Exception If there is an error during the account creation process.
     */
    public Account createAccount(AccountDto accountDto, User user) throws Exception {
        long accountNumber;
        try {
            validateAccountNonExistsForUser(accountDto.getCode(), user.getUid());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
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

        /**
     * Performs a transfer of funds from one account to another.
     *
     * @param senderAccount The account from which the funds will be transferred.
     * @param receiverAccount The account to which the funds will be transferred.
     * @param amount The amount of funds to be transferred.
     * @param user The user initiating the transfer.
     * @return The transaction record for the transfer from the sender's account.
     * @throws Exception If there is an error during the transfer process.
     */
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

        /**
     * Validates that an account of the given currency type does not already exist for the specified user.
     *
     * @param code The currency code of the account to be validated.
     * @param uid The unique identifier of the user for whom the account is being validated.
     * @throws Exception If an account of the given currency type already exists for the specified user.
     */
    public void validateAccountNonExistsForUser(String code, String uid) throws Exception {
        if(accountRepository.existsByCodeAndOwnerUid(code, uid)) {
            throw new Exception("Account of this type already exist for this user");
        }
    }

        /**
     * Validates that the provided user is the owner of the specified account.
     *
     * @param account The account to be validated.
     * @param user The user initiating the validation.
     * @throws OperationNotSupportedException If the provided user is not the owner of the specified account.
     */
    public void validateAccountOwner(Account account, User user) throws OperationNotSupportedException {
        if(!account.getOwner().getUid().equals(user.getUid())) {
            throw new OperationNotSupportedException("Invalid account owner");
        }
    }

        /**
     * Validates that the balance of the specified account is sufficient for the given amount.
     *
     * @param account The account for which the balance is being validated.
     * @param amount The amount for which the balance is being checked.
     * @throws OperationNotSupportedException If the balance of the account is insufficient for the given amount.
     */
    public void validateSufficientFunds(Account account, double amount) throws Exception {
        if(account.getBalance() < amount) {
            throw new OperationNotSupportedException("Insufficient funds in the account");
        }
    }

        /**
     * Validates that the provided amount is greater than zero.
     *
     * @param amount The amount to be validated.
     * @throws IllegalArgumentException If the provided amount is less than or equal to zero.
     */
    public void validateAmount(double amount) throws Exception {
        if(amount <= 0) {
            throw new IllegalArgumentException("Invalid amount");
        }
    }

        /**
     * Validates that the provided currencies for conversion are different.
     *
     * @param convertDto The DTO containing the currencies to be validated.
     * @throws IllegalArgumentException If the currencies are the same.
     */
    public void validateDifferentCurrencyType(ConvertDto convertDto) throws Exception {
        if(convertDto.getToCurrency().equals(convertDto.getFromCurrency())){
            throw new IllegalArgumentException("Conversion between the same currency types is not allowed");
        }
    }

        /**
     * Validates that the provided user is the owner of both the sender and receiver accounts involved in a currency conversion.
     *
     * @param convertDto The DTO containing the currencies to be validated.
     * @param uid The unique identifier of the user for whom the account ownership is being validated.
     * @throws Exception If the provided user is not the owner of either the sender or receiver account.
     */
    public void validateAccountOwnership(ConvertDto convertDto, String uid) throws Exception {
        accountRepository.findByCodeAndOwnerUid(convertDto.getFromCurrency(), uid).orElseThrow();
        accountRepository.findByCodeAndOwnerUid(convertDto.getToCurrency(), uid).orElseThrow();
    }
        /**
     * Validates that the provided user is the owner of the specified account.
     *
     * @param code The currency code of the account to be validated.
     * @param uid The unique identifier of the user for whom the account ownership is being validated.
     * @throws Exception If the provided user is not the owner of the specified account.
     */
    public void validateAccountOwnership(String code, String uid) throws Exception {
        accountRepository.findByCodeAndOwnerUid(code, uid).orElseThrow();
    }

        /**
     * Validates the conversion process by ensuring that the sender's account has sufficient funds,
     * the currencies involved in the conversion are different, and the user is the owner of both accounts.
     *
     * @param convertDto The DTO containing the currencies and amount to be validated.
     * @param uid The unique identifier of the user initiating the conversion.
     * @throws Exception If any of the following conditions are not met:
     * - The sender's account does not have sufficient funds.
     * - The currencies involved in the conversion are the same.
     * - The user is not the owner of both sender and receiver accounts.
     */
    public void validateConversion(ConvertDto convertDto, String uid) throws Exception {
        validateDifferentCurrencyType(convertDto);
        validateAccountOwnership(convertDto, uid);
        validateAmount(convertDto.getAmount());
        validateSufficientFunds(accountRepository.findByCodeAndOwnerUid(convertDto.getFromCurrency(), uid).get(), convertDto.getAmount());
    }

        /**
     * Performs a currency conversion between two accounts owned by the same user.
     * The function validates the conversion process, calculates the converted amount,
     * updates the balances of the sender and receiver accounts, and creates transaction records.
     *
     * @param convertDto The DTO containing the currencies and amount to be converted.
     * @param user The user initiating the conversion.
     * @return The transaction record for the conversion from the sender's account.
     * @throws Exception If any of the following conditions are not met:
     * - The sender's account does not have sufficient funds.
     * - The currencies involved in the conversion are the same.
     * - The user is not the owner of both sender and receiver accounts.
     */
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

        /**
     * Checks if an account with the given currency code and user identifier exists.
     *
     * @param code The currency code of the account.
     * @param uid The unique identifier of the user.
     * @return True if the account exists, false otherwise.
     */
    public boolean existsByCodeAndOwnerUid(String code, String uid) {
        return accountRepository.existsByCodeAndOwnerUid(code, uid);
    }

    /**
     * Retrieves the account with the given currency code and user identifier.
     *
     * @param code The currency code of the account.
     * @param uid The unique identifier of the user.
     * @return The account if found, otherwise an empty Optional.
     */
    public Optional<Account> findByCodeAndOwnerUid(String code, String uid) {
        return accountRepository.findByCodeAndOwnerUid(code, uid);
    }

    /**
     * Saves the given account to the database.
     *
     * @param usdAccount The account to be saved.
     * @return The saved account.
     */
    public Account save(Account usdAccount) {
        return accountRepository.save(usdAccount);
    }

}

