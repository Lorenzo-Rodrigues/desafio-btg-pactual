package com.lorenzo.desafio_btg_pactual.controller;

import com.lorenzo.desafio_btg_pactual.controller.dto.ApiResponse;
import com.lorenzo.desafio_btg_pactual.controller.dto.OrderResponse;
import com.lorenzo.desafio_btg_pactual.controller.dto.PaginationResponse;
import com.lorenzo.desafio_btg_pactual.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/customers/{customerId}/orders")
    public ResponseEntity<?> listOrders(@PathVariable("customerId") Long customerId,
                                        @RequestParam(name = "page",defaultValue = "0") Integer page,
                                        @RequestParam(name = "pageSize",defaultValue = "10")Integer pageSize){


        Page<OrderResponse> pageResponse = orderService.findAllByCustomerId(customerId, PageRequest.of(page,pageSize));
        BigDecimal totalOnOrders = orderService.findTotalOnOrdersByCustomerId(customerId);

        return ResponseEntity.ok(new ApiResponse<>(
                Map.of("totalOnOrders",totalOnOrders),
                pageResponse.getContent(),
                PaginationResponse.fromPage(pageResponse)
        ));
    }
}
