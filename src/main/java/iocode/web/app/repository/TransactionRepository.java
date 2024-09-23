package iocode.web.app.repository;

import iocode.web.app.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.net.ContentHandler;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String> {


    Page<Transaction> findAllByOwnerUid(String uid, Pageable pageable);
    Page<Transaction> findAllByCardCardIdAndOwnerUid(String cardId, String uid, Pageable pageable);
    Page<Transaction> findAllByAccountAccountIdAndOwnerUid(String accountId, String uid, Pageable pageable);
}
