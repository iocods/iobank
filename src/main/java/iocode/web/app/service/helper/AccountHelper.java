package iocode.web.app.service.helper;

import iocode.web.app.dto.AccountDto;
import iocode.web.app.entity.Account;
import iocode.web.app.entity.User;
import iocode.web.app.repository.AccountRepository;
import iocode.web.app.util.RandomUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.naming.OperationNotSupportedException;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Getter
public class AccountHelper {

    private final AccountRepository accountRepository;

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


    public void validateAccountNonExistsForUser(String code, String uid) throws Exception {
        if(accountRepository.existsByCodeAndOwnerUid(code, uid)) {
            throw new Exception("Account of this type already exist for this user");
        }
    }
}
