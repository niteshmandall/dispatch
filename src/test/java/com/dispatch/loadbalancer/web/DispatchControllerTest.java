package com.dispatch.loadbalancer.web;

import com.dispatch.loadbalancer.dto.DispatchPlanResponse;
import com.dispatch.loadbalancer.dto.OrderRequest;
import com.dispatch.loadbalancer.service.DispatchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DispatchController.class)
class DispatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DispatchService dispatchService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddOrders_InvalidInput_ReturnsBadRequest() throws Exception {
        OrderRequest request = OrderRequest.builder().orders(null).build(); // Invalid

        mockMvc.perform(post("/api/dispatch/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetDispatchPlan_ReturnsOk() throws Exception {
        when(dispatchService.generateDispatchPlan()).thenReturn(DispatchPlanResponse.builder().build());

        mockMvc.perform(get("/api/dispatch/plan"))
                .andExpect(status().isOk());
    }
}
