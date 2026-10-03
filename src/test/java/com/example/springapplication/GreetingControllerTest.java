package com.example.springapplication;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GreetingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void returnsGreetingWithProvidedName() throws Exception {
        mockMvc.perform(get("/api/greeting").param("name", "Spring"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {"message":"Hello, Spring!"}
                        """));
    }

    @Test
    void usesWorldWhenNameIsMissing() throws Exception {
        mockMvc.perform(get("/api/greeting"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {"message":"Hello, World!"}
                        """));
    }

    @Test
    void exposesActuatorHealthIncludingDatabaseStatus() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.components.db.status").value("UP"));
    }

    @Test
    void requiresAuthenticationForCustomerOrders() throws Exception {
        mockMvc.perform(get("/api/customers/1/orders"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void customerCanAccessCustomerRoutesButNotActuatorMetrics() throws Exception {
        mockMvc.perform(get("/api/customers/999999/orders")
                        .with(httpBasic("customer", "customer-dev-only")))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/actuator/metrics")
                        .with(httpBasic("customer", "customer-dev-only")))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminCanAccessActuatorMetrics() throws Exception {
        mockMvc.perform(get("/actuator/metrics")
                        .with(httpBasic("admin", "admin-dev-only")))
                .andExpect(status().isOk());
    }
}
