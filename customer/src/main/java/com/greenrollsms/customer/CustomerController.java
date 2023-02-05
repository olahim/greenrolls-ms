package com.greenrollsms.customer;

import com.greenrollsms.common.IResponsePayload;
import com.greenrollsms.common.Message;
import com.greenrollsms.common.ResponsePayload;
import com.greenrollsms.common.exception.CustomException;
import com.greenrollsms.customer.model.CustomerDTO;
import com.greenrollsms.customer.model.CustomerVM;
import com.greenrollsms.customer.service.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/customers")
@AllArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public IResponsePayload<CustomerDTO> register(@RequestBody CustomerVM request) {
        log.info("new customer registration {}", request);
        IResponsePayload<CustomerDTO> payload = new ResponsePayload();

        try {
            CustomerDTO data = customerService.register(request);
            payload.addData(data);
            payload.addMessage("Customer Registered", Message.Severity.SUCCESS);
        } catch (CustomException e) {
            payload.addMessage(e.getMessage(), Message.Severity.ERROR);
        }

        return payload;
    }

    @GetMapping
    public IResponsePayload<CustomerDTO> register() {
        IResponsePayload<CustomerDTO> payload = new ResponsePayload();

        return payload;
    }

}

