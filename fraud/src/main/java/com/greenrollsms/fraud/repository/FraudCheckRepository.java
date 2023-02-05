package com.greenrollsms.fraud.repository;

import com.greenrollsms.fraud.model.FraudCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FraudCheckRepository extends JpaRepository<FraudCheck, Integer> {
}