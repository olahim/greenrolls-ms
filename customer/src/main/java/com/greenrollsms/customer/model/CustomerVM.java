package com.greenrollsms.customer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public record CustomerVM(
        String firstName,
        String lastName,
        String email
        ) {
        }