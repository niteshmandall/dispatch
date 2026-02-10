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
public class VehicleRequest {
    @NotEmpty(message = "Vehicles list cannot be empty")
    @Valid
    private List<VehicleDto> vehicles;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class VehicleDto {
    @NotNull(message = "Vehicle ID is required")
    private String vehicleId;

    @NotNull(message = "Capacity is required")
    private Integer capacity;

    @NotNull(message = "Current latitude is required")
    private Double currentLatitude;

    @NotNull(message = "Current longitude is required")
    private Double currentLongitude;

    private String currentAddress;
}
