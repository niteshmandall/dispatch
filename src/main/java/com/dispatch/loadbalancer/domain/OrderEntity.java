package com.dispatch.loadbalancer.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {

    @Id
    private String orderId;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    private String address;

    @NotNull
    private Integer packageWeight;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Priority priority;
}
