package com.dispatch.loadbalancer.service.strategy;

import com.dispatch.loadbalancer.domain.OrderEntity;
import com.dispatch.loadbalancer.domain.Priority;
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
                                .priority(Priority.HIGH)
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
                                .priority(Priority.HIGH)
                                .latitude(1.0)
                                .longitude(1.0)
                                .build();

                OrderEntity order2 = OrderEntity.builder()
                                .orderId("O2")
                                .packageWeight(5)
                                .priority(Priority.LOW)
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

        @Test
        void testOptimize_EmptyInput() {
                // Act
                DispatchPlanResponse response = strategy.optimize(new ArrayList<>(), new ArrayList<>());

                // Assert
                assertNotNull(response);
                assertTrue(response.getDispatchPlan().isEmpty());
                assertTrue(response.getUnassignedOrders().isEmpty());
        }

        @Test
        void testOptimize_ExactCapacityMatch() {
                // Arrange
                VehicleEntity vehicle = VehicleEntity.builder().vehicleId("V1").capacity(10).currentLatitude(0.0)
                                .currentLongitude(0.0).build();
                vehicles.add(vehicle);

                OrderEntity order = OrderEntity.builder().orderId("O1").packageWeight(10).priority(Priority.HIGH)
                                .latitude(1.0).longitude(1.0).build();
                orders.add(order);

                when(distanceCalculator.calculateDistance(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                                .thenReturn(5.0);

                // Act
                DispatchPlanResponse response = strategy.optimize(orders, vehicles);

                // Assert
                assertEquals(1, response.getDispatchPlan().size());
                assertEquals(10, response.getDispatchPlan().get(0).getTotalLoad());
                assertTrue(response.getUnassignedOrders().isEmpty());
        }

        @Test
        void testOptimize_OrderExceedsCapacity() {
                // Arrange
                VehicleEntity vehicle = VehicleEntity.builder().vehicleId("V1").capacity(5).currentLatitude(0.0)
                                .currentLongitude(0.0).build();
                vehicles.add(vehicle);

                OrderEntity order = OrderEntity.builder().orderId("O1").packageWeight(10).priority(Priority.HIGH)
                                .latitude(1.0).longitude(1.0).build();
                orders.add(order);

                // Act
                DispatchPlanResponse response = strategy.optimize(orders, vehicles);

                // Assert
                assertEquals(1, response.getDispatchPlan().size());
                assertEquals(0, response.getDispatchPlan().get(0).getTotalLoad()); // Nothing assigned
                assertEquals(1, response.getUnassignedOrders().size());
                assertEquals("O1", response.getUnassignedOrders().get(0).getOrderId());
        }

        @Test
        void testOptimize_PriorityHandling() {
                // Arrange
                VehicleEntity vehicle = VehicleEntity.builder().vehicleId("V1").capacity(20).currentLatitude(0.0)
                                .currentLongitude(0.0).build();
                vehicles.add(vehicle);

                OrderEntity highPrio = OrderEntity.builder().orderId("HIGH").packageWeight(10).priority(Priority.HIGH)
                                .latitude(1.0).longitude(1.0).build();
                OrderEntity lowPrio = OrderEntity.builder().orderId("LOW").packageWeight(10).priority(Priority.LOW)
                                .latitude(1.0).longitude(1.0).build();
                OrderEntity medPrio = OrderEntity.builder().orderId("MED").packageWeight(10).priority(Priority.MEDIUM)
                                .latitude(1.0).longitude(1.0).build(); // Should fit if HIGH + MED = 20

                // Add in mixed order
                orders.add(lowPrio);
                orders.add(highPrio);
                orders.add(medPrio);

                when(distanceCalculator.calculateDistance(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                                .thenReturn(5.0);

                // Act
                DispatchPlanResponse response = strategy.optimize(orders, vehicles);

                // Assert
                // Expect HIGH and MEDIUM to be assigned (Standard Greedy Priority), LOW
                // unassigned if capacity full?
                // Capacity 20. HIGH(10) + MED(10) = 20. LOW(10) -> Unassigned.
                assertEquals(1, response.getDispatchPlan().size());
                assertEquals(20, response.getDispatchPlan().get(0).getTotalLoad());

                List<String> assignedIds = response.getDispatchPlan().get(0).getAssignedOrders().stream()
                                .map(ao -> ao.getOrderId()).toList();
                assertTrue(assignedIds.contains("HIGH"));
                assertTrue(assignedIds.contains("MED"));
                assertFalse(assignedIds.contains("LOW"));

                assertEquals(1, response.getUnassignedOrders().size());
                assertEquals("LOW", response.getUnassignedOrders().get(0).getOrderId());
        }

        @Test
        void testOptimize_ZeroDistanceCoordinates() {
                // Arrange: Vehicle and Order at same location (0,0)
                VehicleEntity vehicle = VehicleEntity.builder().vehicleId("V1").capacity(10).currentLatitude(0.0)
                                .currentLongitude(0.0).build();
                vehicles.add(vehicle);

                OrderEntity order = OrderEntity.builder().orderId("O1").packageWeight(5).priority(Priority.HIGH)
                                .latitude(0.0).longitude(0.0).build();
                orders.add(order);

                when(distanceCalculator.calculateDistance(0.0, 0.0, 0.0, 0.0)).thenReturn(0.0);

                // Act
                DispatchPlanResponse response = strategy.optimize(orders, vehicles);

                // Assert
                assertEquals(1, response.getDispatchPlan().size());
                assertEquals("V1", response.getDispatchPlan().get(0).getVehicleId());
                // assertEquals("0.00 km",
                // response.getDispatchPlan().get(0).getTotalDistance()); // Depending on format
        }
}
