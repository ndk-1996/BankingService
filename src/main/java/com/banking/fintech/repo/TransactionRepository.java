package com.banking.fintech.repo;

import com.banking.fintech.entity.TransactionEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM TransactionEntity t WHERE t.accountEntity.accountId = ?1 and t.balance < 0 ORDER BY t.eventDate")
    List<TransactionEntity> getNegativeBalTransactions(Long accountId);
}
