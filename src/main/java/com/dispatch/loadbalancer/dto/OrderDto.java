package com.dispatch.loadbalancer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    @NotNull(message = "Order ID is required")
    private String orderId;

    @NotNull(message = "Latitude is required")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    private Double longitude;

    private String address;

    @NotNull(message = "Package weight is required")
    private Integer packageWeight;

    @NotNull(message = "Priority is required")
    private com.dispatch.loadbalancer.domain.Priority priority;
}
