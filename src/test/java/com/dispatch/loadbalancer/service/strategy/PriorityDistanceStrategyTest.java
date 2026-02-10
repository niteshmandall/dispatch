package com.dispatch.loadbalancer.service.strategy;

import com.dispatch.loadbalancer.domain.OrderEntity;
import com.dispatch.loadbalancer.domain.VehicleEntity;
import com.dispatch.loadbalancer.dto.DispatchPlanResponse;
import com.dispatch.loadbalancer.service.DistanceCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PriorityDistanceStrategyTest {

        @Mock
        private DistanceCalculator distanceCalculator;

        @InjectMocks
        private PriorityDistanceStrategy strategy;

        private List<OrderEntity> orders;
        private List<VehicleEntity> vehicles;

        @BeforeEach
        void setUp() {
                orders = new ArrayList<>();
                vehicles = new ArrayList<>();
        }

        @Test
        void testOptimize_AssignsOrdersCorrectly() {
                // Arrange
                VehicleEntity vehicle = VehicleEntity.builder()
                                .vehicleId("V1")
                                .capacity(100)
                                .currentLatitude(0.0)
                                .currentLongitude(0.0)
                                .build();
                vehicles.add(vehicle);

                OrderEntity order = OrderEntity.builder()
                                .orderId("O1")
                                .packageWeight(10)
                                .priority("HIGH")
                                .latitude(1.0)
                                .longitude(1.0)
                                .build();
                orders.add(order);

                when(distanceCalculator.calculateDistance(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                                .thenReturn(10.0);

                // Act
                DispatchPlanResponse response = strategy.optimize(orders, vehicles);

                // Assert
                assertNotNull(response);
                assertEquals(1, response.getDispatchPlan().size());
                assertEquals("V1", response.getDispatchPlan().get(0).getVehicleId());
                assertEquals(10, response.getDispatchPlan().get(0).getTotalLoad());
                assertEquals(1, response.getDispatchPlan().get(0).getAssignedOrders().size());
        }

        @Test
        void testOptimize_RespectsCapacity() {
                // Arrange
                VehicleEntity vehicle = VehicleEntity.builder()
                                .vehicleId("V1")
                                .capacity(10)
                                .currentLatitude(0.0)
                                .currentLongitude(0.0)
                                .build();
                vehicles.add(vehicle);

                OrderEntity order1 = OrderEntity.builder()
                                .orderId("O1")
                                .packageWeight(10)
                                .priority("HIGH")
                                .latitude(1.0)
                                .longitude(1.0)
                                .build();

                OrderEntity order2 = OrderEntity.builder()
                                .orderId("O2")
                                .packageWeight(5)
                                .priority("LOW")
                                .latitude(2.0)
                                .longitude(2.0)
                                .build();

                orders.add(order1);
                orders.add(order2);

                when(distanceCalculator.calculateDistance(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                                .thenReturn(10.0);

                // Act
                DispatchPlanResponse response = strategy.optimize(orders, vehicles);

                // Assert
                assertEquals(1, response.getDispatchPlan().size());
                assertEquals(10, response.getDispatchPlan().get(0).getTotalLoad()); // Only 10 assigned
                assertEquals(1, response.getDispatchPlan().get(0).getAssignedOrders().size());
                assertEquals("O1", response.getDispatchPlan().get(0).getAssignedOrders().get(0).getOrderId());

                // Verify unassigned
                assertNotNull(response.getUnassignedOrders());
                assertEquals(1, response.getUnassignedOrders().size());
                assertEquals("O2", response.getUnassignedOrders().get(0).getOrderId());
        }
}
