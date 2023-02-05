package com.greenrollsms.billing.service;

import com.greenrollsms.billing.RabbitMQConfiguration;
import com.greenrollsms.billing.core.response.FraudResponse;
import com.greenrollsms.billing.model.Billing;
import com.greenrollsms.billing.model.BillingDTO;
import com.greenrollsms.billing.model.BillingStatus;
import com.greenrollsms.billing.model.BillingVM;
import com.greenrollsms.billing.repository.BillingRepository;
import com.greenrollsms.common.GlobalMessage;
import com.greenrollsms.common.Message;
import com.greenrollsms.common.ResponsePayload;
import com.greenrollsms.common.exception.CustomException;
import com.greenrollsms.common.rabbit_messages.BillingMessage;
import com.greenrollsms.common.rabbit_messages.FundedMessage;
import com.greenrollsms.common.rabbit_messages.RQMessage;
import com.greenrollsms.common.service.BaseValidationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@AllArgsConstructor
@Service
@Slf4j
public class BillingService extends BaseValidationService implements MessageListener {

    private final BillingRepository billingRepository;
    private final RabbitTemplate rabbitTemplate;
    private final RestTemplate restTemplate;

    public BillingDTO fundCustomerAccount(BillingVM request) throws CustomException {
        validateSaveEntity(request, "amount", "customerId", "id");

        ObjectMapper objectMapper = new ObjectMapper();
        ResponsePayload<?> fraudResponse =  restTemplate.getForObject("http://localhost:8085/api/v1/fraud-check/{customerId}",
                ResponsePayload.class, request.customerId());

        fraudResponse.validateDate();
        FraudResponse response = objectMapper.convertValue(fraudResponse.getData().get(0), FraudResponse.class) ;

        if (response.isFraud()) {
            GlobalMessage message = new GlobalMessage("Customer is fraudulent", "", Message.Severity.ERROR );
            throw new CustomException(message);
        }

        Billing billing = Billing.builder()
                .status(BillingStatus.Pending)
                .amount(request.amount())
                .customerId(request.customerId())
                .transactionId(UUID.randomUUID().toString())
                .build();

        billingRepository.saveAndFlush(billing);

        BillingMessage message = BillingMessage.builder()
                .amount(billing.getAmount())
                .customerId(billing.getCustomerId())
                .transactionId(billing.getTransactionId())
                .build();

        RQMessage rqMessage = RQMessage.message(message);
        byte[] body = SerializationUtils.serialize(rqMessage);
        org.springframework.amqp.core.Message message1 = new org.springframework.amqp.core.Message(body);

        rabbitTemplate.convertAndSend("TOPIC_WORKERI_EXCHANGE", "TOPIC_WORKERI_ROUTER", message1);


        return BillingDTO.builder()
                .amount(billing.getAmount())
                .customerId(billing.getCustomerId())
                .id(billing.getId())
                .transactionId(billing.getTransactionId())
                .status(billing.getStatus())
                .build();
    }


    @Override
    public void onMessage(org.springframework.amqp.core.Message message1) {
        RQMessage<FundedMessage> rqMessage = (RQMessage<FundedMessage>) SerializationUtils.deserialize(message1.getBody());
        log.info("receiveMessage {}", rqMessage);
        FundedMessage message = rqMessage.getMessage();
        Billing billing = billingRepository.findBillingByTransactionId(message.getTransactionId()).get();
        if (message.getIsSuccessfull()) {
            log.info("receiveMessage SUCCESSS {}", rqMessage);
            billing.setStatus(BillingStatus.Successful);
            billingRepository.save(billing);
        }
    }
}

