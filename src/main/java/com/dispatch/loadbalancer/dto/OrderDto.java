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
    @jakarta.validation.constraints.Pattern(regexp = "HIGH|MEDIUM|LOW", message = "Priority must be HIGH, MEDIUM, or LOW")
    private String priority; // validation can be added for Enum match
}
