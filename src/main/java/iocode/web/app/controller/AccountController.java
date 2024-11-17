package iocode.web.app.controller;

import iocode.web.app.dto.AccountDto;
import iocode.web.app.dto.ConvertDto;
import iocode.web.app.dto.TransferDto;
import iocode.web.app.entity.Account;
import iocode.web.app.entity.Transaction;
import iocode.web.app.entity.User;
import iocode.web.app.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller for handling account-related operations.
 */
@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    /**
     * Creates a new account for the authenticated user.
     *
     * @param accountDto The details of the new account.
     * @param authentication The current authentication context.
     * @return The created account.
     * @throws Exception If an error occurs during account creation.
     */
    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody AccountDto accountDto, Authentication authentication) throws Exception {
        var user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(accountService.createAccount(accountDto, user));
    }

    /**
     * Retrieves a list of accounts associated with the authenticated user.
     *
     * @param authentication The current authentication context.
     * @return The list of user accounts.
     */
    @GetMapping
    public ResponseEntity<List<Account>> getUserAccounts(Authentication authentication) {
        var user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(accountService.getUserAccounts(user.getUid()));
    }

    /**
     * Transfers funds from one account to another for the authenticated user.
     *
     * @param transferDto The details of the transfer.
     * @param authentication The current authentication context.
     * @return The created transaction.
     * @throws Exception If an error occurs during the transfer.
     */
    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transferFunds(@RequestBody TransferDto transferDto, Authentication authentication) throws Exception {
        var user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(accountService.transferFunds(transferDto, user));
    }

    /**
     * Retrieves the current exchange rates for supported currencies.
     *
     * @return The map of currency codes to exchange rates.
     */
    @GetMapping("/rates")
    public ResponseEntity<Map<String, Double>> getExchangeRate() {
        return ResponseEntity.ok(accountService.getExchangeRate());
    }

    @PostMapping("/find")
    public ResponseEntity<Account> findAccount(@RequestBody TransferDto dto) {
        return ResponseEntity.ok(accountService.findAccount(dto.getCode() ,dto.getRecipientAccountNumber()));
    }

    /**
     * Converts a specified amount of currency from one account to another for the authenticated user.
     *
     * @param convertDto The details of the conversion.
     * @param authentication The current authentication context.
     * @return The created transaction.
     * @throws Exception If an error occurs during the conversion.
     */
    @PostMapping("/convert")
    public ResponseEntity<Transaction> convertCurrency(@RequestBody ConvertDto convertDto, Authentication authentication) throws Exception {
        var user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(accountService.convertCurrency(convertDto, user));
    }
}
