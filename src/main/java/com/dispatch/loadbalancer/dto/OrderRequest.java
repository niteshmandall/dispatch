package com.dispatch.loadbalancer.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

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
