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
public class DispatchPlanResponse {
    private List<VehicleDispatchPlan> dispatchPlan;
}
