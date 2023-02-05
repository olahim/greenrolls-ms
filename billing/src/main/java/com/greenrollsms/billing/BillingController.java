package com.greenrollsms.billing;

import com.greenrollsms.billing.model.BillingDTO;
import com.greenrollsms.billing.model.BillingVM;
import com.greenrollsms.billing.service.BillingService;
import com.greenrollsms.common.IResponsePayload;
import com.greenrollsms.common.Message;
import com.greenrollsms.common.ResponsePayload;
import com.greenrollsms.common.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("api/v1/billings")
public record BillingController(BillingService billingService) {

@PostMapping
public IResponsePayload<BillingDTO> fundCustomerAccount(@RequestBody BillingVM request) {
        log.info("fund customer account {}", request);
        IResponsePayload<BillingDTO> payload = new ResponsePayload();

        try {
        BillingDTO data = billingService.fundCustomerAccount(request);
        payload.addData(data);
        payload.addMessage("Funding in process", Message.Severity.SUCCESS);
        } catch (CustomException e) {
        payload.addMessage(e.getMessage(), Message.Severity.ERROR);
        }

        return payload;
        }

}
