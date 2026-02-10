package com.dispatch.loadbalancer.service.strategy;

import com.dispatch.loadbalancer.domain.OrderEntity;
import com.dispatch.loadbalancer.domain.VehicleEntity;
import com.dispatch.loadbalancer.dto.AssignedOrder;
import com.dispatch.loadbalancer.dto.DispatchPlanResponse;
import com.dispatch.loadbalancer.dto.VehicleDispatchPlan;
import com.dispatch.loadbalancer.service.DistanceCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PriorityDistanceStrategy implements DispatchOptimizationStrategy {

    private final DistanceCalculator distanceCalculator;

    @Override
    public DispatchPlanResponse optimize(List<OrderEntity> orders, List<VehicleEntity> vehicles) {
        // 1. Sort orders by priority
        List<OrderEntity> sortedOrders = orders.stream()
                .sorted(Comparator.comparingInt(this::getPriorityValue))
                .toList();

        // 2. Initialize tracking for vehicles
        Map<String, VehicleState> vehicleStates = new HashMap<>();
        for (VehicleEntity v : vehicles) {
            vehicleStates.put(v.getVehicleId(), new VehicleState(v));
        }

        // 3. Assign orders
        for (OrderEntity order : sortedOrders) {
            VehicleState bestVehicle = null;
            double minAddedDistance = Double.MAX_VALUE;

            for (VehicleState vehicle : vehicleStates.values()) {
                if (vehicle.canCarry(order.getPackageWeight())) {
                    double distance = distanceCalculator.calculateDistance(
                            vehicle.currentLat, vehicle.currentLon,
                            order.getLatitude(), order.getLongitude());
                    if (distance < minAddedDistance) {
                        minAddedDistance = distance;
                        bestVehicle = vehicle;
                    }
                }
            }

            if (bestVehicle != null) {
                bestVehicle.assignOrder(order, minAddedDistance);
            } else {
                // Log or handle unassigned order
                System.out.println(
                        "Order " + order.getOrderId() + " mainly could not be assigned due to capacity limits.");
            }
        }

        // 4. Construct Response
        List<VehicleDispatchPlan> plans = vehicleStates.values().stream()
                .map(VehicleState::toDispatchPlan)
                .collect(Collectors.toList());

        return DispatchPlanResponse.builder()
                .dispatchPlan(plans)
                .build();
    }

    private int getPriorityValue(OrderEntity order) {
        return switch (order.getPriority().toUpperCase()) {
            case "HIGH" -> 1;
            case "MEDIUM" -> 2;
            case "LOW" -> 3;
            default -> 4;
        };
    }

    private static class VehicleState {
        String vehicleId;
        int capacity;
        int currentLoad;
        double currentLat;
        double currentLon;
        double totalDistance;
        List<AssignedOrder> assignedOrders = new ArrayList<>();

        VehicleState(VehicleEntity entity) {
            this.vehicleId = entity.getVehicleId();
            this.capacity = entity.getCapacity();
            this.currentLoad = 0;
            this.currentLat = entity.getCurrentLatitude();
            this.currentLon = entity.getCurrentLongitude();
            this.totalDistance = 0.0;
        }

        boolean canCarry(int weight) {
            return currentLoad + weight <= capacity;
        }

        void assignOrder(OrderEntity order, double distance) {
            this.currentLoad += order.getPackageWeight();
            this.totalDistance += distance;
            this.currentLat = order.getLatitude(); // Move vehicle to order location
            this.currentLon = order.getLongitude();

            this.assignedOrders.add(AssignedOrder.builder()
                    .orderId(order.getOrderId())
                    .latitude(order.getLatitude())
                    .longitude(order.getLongitude())
                    .address(order.getAddress())
                    .packageWeight(order.getPackageWeight())
                    .priority(order.getPriority())
                    .build());
        }

        VehicleDispatchPlan toDispatchPlan() {
            return VehicleDispatchPlan.builder()
                    .vehicleId(vehicleId)
                    .totalLoad(currentLoad)
                    .totalDistance(String.format("%.2f km", totalDistance))
                    .assignedOrders(assignedOrders)
                    .build();
        }
    }
}
