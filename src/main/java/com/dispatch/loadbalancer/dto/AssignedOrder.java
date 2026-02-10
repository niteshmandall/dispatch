package com.dispatch.loadbalancer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignedOrder {
    private String orderId;
    private Double latitude;
    private Double longitude;
    private String address;
    private Integer packageWeight;
    private String priority;
}
