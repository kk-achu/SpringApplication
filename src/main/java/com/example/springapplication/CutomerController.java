package com.example.springapplication;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CutomerController {

    private final CustomerService customerService;

    public CutomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/api/customers")
    public List<CustomerDTO> getCustomers() {
        return customerService.getCustomers();
    }
}