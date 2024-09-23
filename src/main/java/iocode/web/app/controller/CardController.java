package iocode.web.app.controller;

import iocode.web.app.entity.Card;
import iocode.web.app.entity.Transaction;
import iocode.web.app.entity.User;
import iocode.web.app.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/card")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;
    @GetMapping
    public ResponseEntity<Card> getCard(Authentication authentication){
        var user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(cardService.getCard(user));
    }

    @PostMapping("/create")
    public ResponseEntity<Card> createCard(@RequestParam double amount, Authentication authentication) throws Exception {
        var user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(cardService.createCard(amount, user));
    }

    @PostMapping("/credit")
    public ResponseEntity<Transaction> creditCard(@RequestParam double amount, Authentication authentication){
        var user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(cardService.creditCard(amount, user));
    }
    @PostMapping("/debit")
    public ResponseEntity<Transaction> debitCard(@RequestParam double amount, Authentication authentication){
        var user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(cardService.debitCard(amount, user));
    }
}
