package com.dispatch.loadbalancer.web;

import com.dispatch.loadbalancer.dto.DispatchPlanResponse;
import com.dispatch.loadbalancer.dto.OrderRequest;
import com.dispatch.loadbalancer.dto.VehicleRequest;
import com.dispatch.loadbalancer.service.DispatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/dispatch")
@RequiredArgsConstructor
public class DispatchController {

    private final DispatchService dispatchService;

    @PostMapping("/orders")
    public ResponseEntity<Map<String, String>> addOrders(@Valid @RequestBody OrderRequest orderRequest) {
        dispatchService.saveOrders(orderRequest);
        return ResponseEntity.ok(Map.of("message", "Delivery orders accepted.", "status", "success"));
    }

    @PostMapping("/vehicles")
    public ResponseEntity<Map<String, String>> addVehicles(@Valid @RequestBody VehicleRequest vehicleRequest) {
        dispatchService.saveVehicles(vehicleRequest);
        return ResponseEntity.ok(Map.of("message", "Vehicle details accepted.", "status", "success"));
    }

    @GetMapping("/plan")
    public ResponseEntity<DispatchPlanResponse> getDispatchPlan() {
        return ResponseEntity.ok(dispatchService.generateDispatchPlan());
    }
}
