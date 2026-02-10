package com.dispatch.loadbalancer.service.strategy;

import com.dispatch.loadbalancer.domain.OrderEntity;
import com.dispatch.loadbalancer.domain.VehicleEntity;
import com.dispatch.loadbalancer.dto.DispatchPlanResponse;

import java.util.List;

public interface DispatchOptimizationStrategy {
    DispatchPlanResponse optimize(List<OrderEntity> orders, List<VehicleEntity> vehicles);
}
