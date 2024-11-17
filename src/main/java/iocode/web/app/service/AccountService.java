package iocode.web.app.service;

import iocode.web.app.dto.AccountDto;
import iocode.web.app.dto.ConvertDto;
import iocode.web.app.dto.TransferDto;
import iocode.web.app.entity.Account;
import iocode.web.app.entity.Transaction;
import iocode.web.app.entity.User;
import iocode.web.app.repository.AccountRepository;
import iocode.web.app.service.helper.AccountHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * This class provides services related to bank accounts.
 * It handles account creation, retrieval, fund transfer, currency conversion, and exchange rate retrieval.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountHelper accountHelper;
    private final ExchangeRateService exchangeRateService;

    /**
     * Creates a new account for the given user.
     *
     * @param accountDto The account details.
     * @param user The user who owns the account.
     * @return The created account.
     * @throws Exception If any error occurs during account creation.
     */
    public Account createAccount(AccountDto accountDto, User user) throws Exception {
        return accountHelper.createAccount(accountDto, user);
    }

    /**
     * Retrieves all accounts owned by the user with the given uid.
     *
     * @param uid The unique identifier of the user.
     * @return A list of accounts owned by the user.
     */
    public List<Account> getUserAccounts(String uid) {
        return accountRepository.findAllByOwnerUid(uid);
    }

    /**
     * Transfers funds from one account to another.
     *
     * @param transferDto The transfer details.
     * @param user The user initiating the transfer.
     * @return The transaction record of the transfer.
     * @throws Exception If any error occurs during the transfer.
     */
    public Transaction transferFunds(TransferDto transferDto, User user) throws Exception {
        System.out.println("Currency Code: " + transferDto.getCode());
        var senderAccount = accountRepository.findByCodeAndOwnerUid(transferDto.getCode(), user.getUid())
            .orElseThrow(() -> new UnsupportedOperationException("Account of type currency do not exists for user"));
        var receiverAccount = accountRepository.findByAccountNumber(transferDto.getRecipientAccountNumber()).orElseThrow();
        return accountHelper.performTransfer(senderAccount, receiverAccount, transferDto.getAmount(), user);
    }

    /**
     * Retrieves the current exchange rates for all supported currencies.
     *
     * @return A map of currency codes to their exchange rates.
     */
    public Map<String, Double> getExchangeRate() {
        return exchangeRateService.getRates();
    }

    /**
     * Converts the amount from one currency to another.
     *
     * @param convertDto The conversion details.
     * @param user The user initiating the conversion.
     * @return The transaction record of the conversion.
     * @throws Exception If any error occurs during the conversion.
     */
    public Transaction convertCurrency(ConvertDto convertDto, User user) throws Exception {
        return accountHelper.convertCurrency(convertDto, user);
    }

    public Account findAccount(String code, long recipientAccountNumber) {
        System.out.println("Account Number : " + recipientAccountNumber);
        System.out.println("Code: " + code);
        return accountRepository.findByCodeAndAccountNumber(code, recipientAccountNumber).orElseThrow();
    }
}
