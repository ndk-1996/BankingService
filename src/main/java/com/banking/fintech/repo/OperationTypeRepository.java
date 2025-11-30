package com.banking.fintech.repo;

import com.banking.fintech.entity.OperationTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationTypeRepository extends JpaRepository<OperationTypeEntity, Long> {
}
