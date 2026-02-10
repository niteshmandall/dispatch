package com.dispatch.loadbalancer.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    @NotEmpty(message = "Orders list cannot be empty")
    @Valid
    private List<OrderDto> orders;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class OrderDto {
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
    private String priority; // validation can be added for Enum match
}
