package iocode.web.app.controller;

import iocode.web.app.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    @GetMapping("/transactions")
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/transactions/c/{cardId}")
    public List<Transaction> getTransactionsByCardId(@PathVariable Long cardId) {
        return transactionService.getTransactionsByCardId(cardId);
    }

    @GetMapping("/transactions/a/{accountId}")
    public List<Transaction> getTransactionsByAccountId(@PathVariable Long accountId) {
        return transactionService.getTransactionsByAccountId(accountId);
    }
}
