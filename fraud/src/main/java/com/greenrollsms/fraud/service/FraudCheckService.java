package com.greenrollsms.fraud.service;

import com.greenrollsms.fraud.model.FraudCheck;
import com.greenrollsms.fraud.repository.FraudCheckRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public record FraudCheckService(FraudCheckRepository fraudCheckRepository) {

public Boolean isFraudulentCustomer(Integer customerId) {
        FraudCheck audit = FraudCheck.builder()
        .customerId(customerId)
        .createdAt(LocalDateTime.now())
        .isFraudster(false)
        .build();
        fraudCheckRepository.save(audit);
        return false;
        }
        }