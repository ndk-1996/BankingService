package com.banking.fintech.repo;

import com.banking.fintech.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    @Query("select t from TransactionEntity t where t.accountEntity.accountId = ?1 and t.balance < 0")
    List<TransactionEntity> getAllNegativeBalTransactions(Long accountId);
}
