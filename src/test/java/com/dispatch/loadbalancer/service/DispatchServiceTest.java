package com.dispatch.loadbalancer.service;

import com.dispatch.loadbalancer.domain.OrderEntity;
import com.dispatch.loadbalancer.domain.VehicleEntity;
import com.dispatch.loadbalancer.dto.DispatchPlanResponse;
import com.dispatch.loadbalancer.dto.OrderRequest;
import com.dispatch.loadbalancer.dto.VehicleRequest;
import com.dispatch.loadbalancer.repository.OrderRepository;
import com.dispatch.loadbalancer.repository.VehicleRepository;
import com.dispatch.loadbalancer.service.strategy.DispatchOptimizationStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DispatchServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private VehicleRepository vehicleRepository;
    @Mock
    private DispatchOptimizationStrategy strategy;

    @InjectMocks
    private DispatchService dispatchService;

    @Test
    void testSaveOrders() {
        OrderRequest request = OrderRequest.builder()
                .orders(Collections.emptyList())
                .build();

        dispatchService.saveOrders(request);

        verify(orderRepository).saveAll(anyList());
    }

    @Test
    void testSaveVehicles() {
        VehicleRequest request = VehicleRequest.builder()
                .vehicles(Collections.emptyList())
                .build();

        dispatchService.saveVehicles(request);

        verify(vehicleRepository).saveAll(anyList());
    }

    @Test
    void testGenerateDispatchPlan() {
        when(orderRepository.findAll()).thenReturn(List.of(new OrderEntity()));
        when(vehicleRepository.findAll()).thenReturn(List.of(new VehicleEntity()));
        when(strategy.optimize(anyList(), anyList())).thenReturn(new DispatchPlanResponse());

        dispatchService.generateDispatchPlan();

        verify(strategy).optimize(anyList(), anyList());
    }
}
