package com.dispatch.loadbalancer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDispatchPlan {
    private String vehicleId;
    private Integer totalLoad;
    private String totalDistance; // String to include unit "km"
    private List<AssignedOrder> assignedOrders;
}
