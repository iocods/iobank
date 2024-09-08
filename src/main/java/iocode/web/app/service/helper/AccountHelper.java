package iocode.web.app.service.helper;

import iocode.web.app.dto.AccountDto;
import iocode.web.app.entity.*;
import iocode.web.app.repository.AccountRepository;
import iocode.web.app.repository.TransactionRepository;
import iocode.web.app.util.RandomUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.naming.OperationNotSupportedException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Getter
public class AccountHelper {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;


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
        var senderTransaction = Transaction.builder()
            .account(senderAccount)
            .status(Status.COMPLETED)
            .type(Type.WITHDRAW)
            .txFee(amount * 0.01)
            .amount(amount)
            .owner(senderAccount.getOwner())
            .build();

        var recipientTransaction = Transaction.builder()
            .account(senderAccount)
            .status(Status.COMPLETED)
            .type(Type.DEPOSIT)
            .amount(amount)
            .owner(receiverAccount.getOwner())
            .build();
        return transactionRepository.saveAll(List.of(senderTransaction, recipientTransaction)).getFirst();
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
}
