package com.dispatch.loadbalancer.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vehicles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleEntity {

    @Id
    private String vehicleId;

    @NotNull
    private Integer capacity;

    @NotNull
    private Double currentLatitude;

    @NotNull
    private Double currentLongitude;

    private String currentAddress;
}
