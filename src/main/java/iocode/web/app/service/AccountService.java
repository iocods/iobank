package iocode.web.app.service;

import iocode.web.app.dto.AccountDto;
import iocode.web.app.dto.TransferDto;
import iocode.web.app.entity.Account;
import iocode.web.app.entity.Transaction;
import iocode.web.app.entity.User;
import iocode.web.app.repository.AccountRepository;
import iocode.web.app.service.helper.AccountHelper;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountHelper accountHelper;
    private final ExchangeRateService exchangeRateService;
    public Account createAccount(AccountDto accountDto, User user) throws Exception {
        return accountHelper.createAccount(accountDto, user);
    }

    public List<Account> getUserAccounts(String uid) {
        return accountRepository.findAllByOwnerUid(uid);
    }

    public Transaction transferFunds(TransferDto transferDto, User user) throws Exception {
        var senderAccount = accountRepository.findByCodeAndOwnerUid(transferDto.getCode(), user.getUid())
            .orElseThrow(() -> new UnsupportedOperationException("Account of type currency do not exists for user"));
        var receiverAccount = accountRepository.findByAccountNumber(transferDto.getRecipientAccountNumber()).orElseThrow();
        return accountHelper.performTransfer(senderAccount, receiverAccount, transferDto.getAmount(), user);
    }

    public Map<String, Double> getExchangeRate() {
        return exchangeRateService.getRates();
    }
}
