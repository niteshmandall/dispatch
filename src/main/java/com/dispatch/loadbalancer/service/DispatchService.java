package com.dispatch.loadbalancer.service;

import com.dispatch.loadbalancer.domain.OrderEntity;
import com.dispatch.loadbalancer.domain.Priority;
import com.dispatch.loadbalancer.domain.VehicleEntity;
import com.dispatch.loadbalancer.dto.DispatchPlanResponse;
import com.dispatch.loadbalancer.dto.OrderRequest;
import com.dispatch.loadbalancer.dto.VehicleRequest;
import com.dispatch.loadbalancer.repository.OrderRepository;
import com.dispatch.loadbalancer.repository.VehicleRepository;
import com.dispatch.loadbalancer.service.strategy.DispatchOptimizationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DispatchService {

    private final OrderRepository orderRepository;
    private final VehicleRepository vehicleRepository;
    private final DispatchOptimizationStrategy dispatchOptimizationStrategy;

    @Transactional
    public void saveOrders(OrderRequest orderRequest) {
        List<OrderEntity> entities = orderRequest.getOrders().stream()
                .map(dto -> OrderEntity.builder()
                        .orderId(dto.getOrderId())
                        .latitude(dto.getLatitude())
                        .longitude(dto.getLongitude())
                        .address(dto.getAddress())
                        .packageWeight(dto.getPackageWeight())
                        .priority(dto.getPriority())
                        .build())
                .collect(Collectors.toList());
        orderRepository.saveAll(entities);
    }

    @Transactional
    public void saveVehicles(VehicleRequest vehicleRequest) {
        List<VehicleEntity> entities = vehicleRequest.getVehicles().stream()
                .map(dto -> VehicleEntity.builder()
                        .vehicleId(dto.getVehicleId())
                        .capacity(dto.getCapacity())
                        .currentLatitude(dto.getCurrentLatitude())
                        .currentLongitude(dto.getCurrentLongitude())
                        .currentAddress(dto.getCurrentAddress())
                        .build())
                .collect(Collectors.toList());
        vehicleRepository.saveAll(entities);
    }

    public DispatchPlanResponse generateDispatchPlan() {
        List<OrderEntity> orders = orderRepository.findAll();
        List<VehicleEntity> vehicles = vehicleRepository.findAll();

        return dispatchOptimizationStrategy.optimize(orders, vehicles);
    }
}
