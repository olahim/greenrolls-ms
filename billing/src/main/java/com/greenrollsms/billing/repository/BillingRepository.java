package com.greenrollsms.billing.repository;

import com.greenrollsms.billing.model.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Integer> {
    Optional<Billing> findBillingByTransactionId(String transactionId);
}