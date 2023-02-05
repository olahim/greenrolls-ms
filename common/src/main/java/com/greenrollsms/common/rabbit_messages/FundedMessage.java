package com.greenrollsms.common.rabbit_messages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FundedMessage implements IRabbitMQ {
    private String amount;
    private String transactionId;
    private Boolean isSuccessfull;
}
