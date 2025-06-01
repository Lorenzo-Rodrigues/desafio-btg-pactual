package com.lorenzo.desafio_btg_pactual.controller.dto;

import com.lorenzo.desafio_btg_pactual.entity.OrderEntity;

import java.math.BigDecimal;

public record OrderResponse(Long orderId,
                            Long customerId,
                            BigDecimal total) {
    public static OrderResponse fromEntity(OrderEntity entity){
        return new OrderResponse(entity.getOrderId(),entity.getCustomerId(),entity.getTotal());
    }
}
