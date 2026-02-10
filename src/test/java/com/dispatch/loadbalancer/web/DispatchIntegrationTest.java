package com.dispatch.loadbalancer.web;

import com.dispatch.loadbalancer.domain.OrderEntity;
import com.dispatch.loadbalancer.domain.Priority;
import com.dispatch.loadbalancer.domain.VehicleEntity;
import com.dispatch.loadbalancer.dto.DispatchPlanResponse;
import com.dispatch.loadbalancer.dto.OrderDto;
import com.dispatch.loadbalancer.dto.OrderRequest;
import com.dispatch.loadbalancer.dto.VehicleDto;
import com.dispatch.loadbalancer.dto.VehicleRequest;
import com.dispatch.loadbalancer.repository.OrderRepository;
import com.dispatch.loadbalancer.repository.VehicleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DispatchIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        vehicleRepository.deleteAll();
    }

    @Test
    void testDispatchOptimizationWithAssignmentData() throws Exception {
        // 1. Post Vehicles (VEH001 - VEH005)
        List<VehicleDto> vehicleDtos = new ArrayList<>();
        vehicleDtos.add(createVehicle("VEH001", 100, 28.7041, 77.1025, "Karol Bagh, Delhi, India"));
        vehicleDtos.add(createVehicle("VEH002", 80, 28.5355, 77.3910, "Sector 18, Noida, Uttar Pradesh, India"));
        vehicleDtos.add(createVehicle("VEH003", 120, 28.4595, 77.0266, "Cyber Hub, Gurgaon, Haryana, India"));
        vehicleDtos.add(createVehicle("VEH004", 90, 28.6139, 77.2090, "Connaught Place, Delhi, India"));
        vehicleDtos.add(createVehicle("VEH005", 110, 28.7041, 77.1025, "Karol Bagh, Delhi, India"));

        VehicleRequest vehicleRequest = VehicleRequest.builder().vehicles(vehicleDtos).build();

        mockMvc.perform(post("/api/dispatch/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehicleRequest)))
                .andExpect(status().isOk());

        // 2. Post Orders (ORD001 - ORD030)
        List<OrderDto> orderDtos = new ArrayList<>();
        // Add sample orders based on the prompt
        orderDtos.add(createOrder("ORD001", 15, "HIGH", 28.6139, 77.2090, "Connaught Place, Delhi, India"));
        orderDtos.add(createOrder("ORD002", 10, "MEDIUM", 28.6139, 77.2090, "Connaught Place, Delhi, India"));
        orderDtos.add(createOrder("ORD003", 20, "LOW", 28.7041, 77.1025, "Karol Bagh, Delhi, India"));
        orderDtos.add(createOrder("ORD004", 25, "HIGH", 28.5355, 77.3910, "Sector 18, Noida, Uttar Pradesh, India"));
        orderDtos.add(createOrder("ORD005", 30, "MEDIUM", 28.4595, 77.0266, "Cyber Hub, Gurgaon, Haryana, India"));
        orderDtos.add(createOrder("ORD006", 40, "LOW", 28.6139, 77.2090, "Connaught Place, Delhi, India"));
        orderDtos.add(createOrder("ORD007", 20, "HIGH", 28.7041, 77.1025, "Karol Bagh, Delhi, India"));
        orderDtos.add(createOrder("ORD008", 25, "HIGH", 28.5355, 77.3910, "Sector 18, Noida, Uttar Pradesh, India"));
        orderDtos.add(createOrder("ORD009", 15, "MEDIUM", 28.4595, 77.0266, "Cyber Hub, Gurgaon, Haryana, India"));
        orderDtos.add(createOrder("ORD010", 30, "LOW", 28.6139, 77.2090, "Connaught Place, Delhi, India"));
        orderDtos.add(createOrder("ORD011", 20, "HIGH", 28.7041, 77.1025, "Karol Bagh, Delhi, India"));
        orderDtos.add(createOrder("ORD012", 10, "MEDIUM", 28.5355, 77.3910, "Sector 18, Noida, Uttar Pradesh, India"));
        orderDtos.add(createOrder("ORD013", 25, "HIGH", 28.4595, 77.0266, "Cyber Hub, Gurgaon, Haryana, India"));
        orderDtos.add(createOrder("ORD014", 15, "LOW", 28.6139, 77.2090, "Connaught Place, Delhi, India"));
        orderDtos.add(createOrder("ORD015", 20, "HIGH", 28.7041, 77.1025, "Karol Bagh, Delhi, India"));
        orderDtos.add(createOrder("ORD016", 30, "LOW", 28.5355, 77.3910, "Sector 18, Noida, Uttar Pradesh, India"));
        orderDtos.add(createOrder("ORD017", 15, "MEDIUM", 28.4595, 77.0266, "Cyber Hub, Gurgaon, Haryana, India"));
        orderDtos.add(createOrder("ORD018", 10, "HIGH", 28.6139, 77.2090, "Connaught Place, Delhi, India"));
        orderDtos.add(createOrder("ORD019", 20, "LOW", 28.7041, 77.1025, "Karol Bagh, Delhi, India"));
        orderDtos.add(createOrder("ORD020", 30, "HIGH", 28.5355, 77.3910, "Sector 18, Noida, Uttar Pradesh, India"));
        orderDtos.add(createOrder("ORD021", 25, "MEDIUM", 28.6139, 77.2090, "Connaught Place, Delhi, India"));
        orderDtos.add(createOrder("ORD022", 20, "LOW", 28.7041, 77.1025, "Karol Bagh, Delhi, India"));
        orderDtos.add(createOrder("ORD023", 30, "HIGH", 28.5355, 77.3910, "Sector 18, Noida, Uttar Pradesh, India"));
        orderDtos.add(createOrder("ORD024", 15, "LOW", 28.4595, 77.0266, "Cyber Hub, Gurgaon, Haryana, India"));
        orderDtos.add(createOrder("ORD025", 20, "MEDIUM", 28.6139, 77.2090, "Connaught Place, Delhi, India"));
        orderDtos.add(createOrder("ORD026", 25, "HIGH", 28.7041, 77.1025, "Karol Bagh, Delhi, India"));
        orderDtos.add(createOrder("ORD027", 20, "MEDIUM", 28.5355, 77.3910, "Sector 18, Noida, Uttar Pradesh, India"));
        orderDtos.add(createOrder("ORD028", 15, "LOW", 28.4595, 77.0266, "Cyber Hub, Gurgaon, Haryana, India"));
        orderDtos.add(createOrder("ORD029", 30, "HIGH", 28.6139, 77.2090, "Connaught Place, Delhi, India"));
        orderDtos.add(createOrder("ORD030", 20, "LOW", 28.7041, 77.1025, "Karol Bagh, Delhi, India"));

        OrderRequest orderRequest = OrderRequest.builder().orders(orderDtos).build();

        mockMvc.perform(post("/api/dispatch/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isOk());

        // 3. Get Dispatch Plan
        MvcResult result = mockMvc.perform(get("/api/dispatch/plan"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        DispatchPlanResponse response = objectMapper.readValue(content, DispatchPlanResponse.class);

        // 4. Assertions
        // Verify total orders assigned (should be 30 ideally, but depends on capacity
        // constraint)
        int totalAssigned = response.getDispatchPlan().stream().mapToInt(p -> p.getAssignedOrders().size()).sum();
        int totalUnassigned = response.getUnassignedOrders() != null ? response.getUnassignedOrders().size() : 0;

        assertEquals(30, totalAssigned + totalUnassigned, "Total orders processed should match input");

        // Print results for manual inspection in logs
        System.out.println("Assigned: " + totalAssigned);
        System.out.println("Unassigned: " + totalUnassigned);
        response.getDispatchPlan().forEach(plan -> {
            System.out.println("Vehicle " + plan.getVehicleId() + " Load: " + plan.getTotalLoad() + " Distance: "
                    + plan.getTotalDistance());
            plan.getAssignedOrders().forEach(order -> {
                System.out
                        .println("  - " + order.getOrderId() + " (" + order.getPriority() + ") " + order.getAddress());
            });
        });
    }

    private VehicleDto createVehicle(String id, int capacity, double lat, double lon, String address) {
        return VehicleDto.builder()
                .vehicleId(id)
                .capacity(capacity)
                .currentLatitude(lat)
                .currentLongitude(lon)
                .currentAddress(address)
                .build();
    }

    private OrderDto createOrder(String id, int weight, String priority, double lat, double lon, String address) {
        return OrderDto.builder()
                .orderId(id)
                .packageWeight(weight)
                .priority(Priority.valueOf(priority))
                .latitude(lat)
                .longitude(lon)
                .address(address)
                .build();
    }
}
