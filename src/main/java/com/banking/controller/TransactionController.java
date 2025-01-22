package com.banking.controller;

import com.banking.model.Transaction;
import com.banking.model.User;
import com.banking.repository.TransactionRepository;
import com.banking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @PostMapping("/transfer")
    public String transfer(@RequestParam Long senderId, @RequestParam Long recipientId, @RequestParam double amount) {
        User sender = userRepository.findById(senderId).orElse(null);
        User recipient = userRepository.findById(recipientId).orElse(null);

        if (sender == null || recipient == null) {
            return "User not found.";
        }

        if (sender.getBalance() < amount) {
            return "Insufficient balance.";
        }

        sender.setBalance(sender.getBalance() - amount);
        recipient.setBalance(recipient.getBalance() + amount);

        userRepository.save(sender);
        userRepository.save(recipient);

        Transaction transaction = new Transaction();
        transaction.setSenderId(senderId);
        transaction.setRecipientId(recipientId);
        transaction.setAmount(amount);
        transactionRepository.save(transaction);

        return "Transfer successful!";
    }
}
