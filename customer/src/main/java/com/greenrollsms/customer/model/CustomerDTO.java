package com.greenrollsms.customer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO{
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
}
