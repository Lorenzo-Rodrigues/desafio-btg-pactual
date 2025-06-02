package com.lorenzo.desafio_btg_pactual.service;

import com.lorenzo.desafio_btg_pactual.controller.dto.OrderResponse;
import com.lorenzo.desafio_btg_pactual.entity.OrderEntity;
import com.lorenzo.desafio_btg_pactual.entity.OrderItem;
import com.lorenzo.desafio_btg_pactual.listener.dto.OrderCreatedEvent;
import com.lorenzo.desafio_btg_pactual.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final MongoTemplate mongoTemplate;

    public OrderService(OrderRepository orderRepository, MongoTemplate mongoTemplate) {
        this.orderRepository = orderRepository;
        this.mongoTemplate = mongoTemplate;
    }


    public void save(OrderCreatedEvent event){
        var entity = new OrderEntity();

         entity.setOrderId(event.codigoPedido());
         entity.setCustomerId(event.codigoCliente());
         entity.setItems(getOrderItems(event));
         entity.setTotal(getTotal(event));

         log.info("saving order ...");
         orderRepository.save(entity);
    }

    public Page<OrderResponse> findAllByCustomerId(Long customerId, PageRequest pageRequest){
        log.info("finding customer's id {} orders...", customerId);
        var orders = orderRepository.findAllByCustomerId(customerId,pageRequest);

        return orders.map(OrderResponse::fromEntity);
    }

    public BigDecimal findTotalOnOrdersByCustomerId(Long customerId){
        var aggregations = newAggregation(
                match(Criteria.where("customerId").is(customerId)),
                group().sum("total").as("total")
        );
        log.info("operating aggregation ...");
        var response = mongoTemplate.aggregate(aggregations,"orders", Document.class);

        return new BigDecimal(response.getUniqueMappedResult().get("total").toString());
    }

    private BigDecimal getTotal(OrderCreatedEvent event){
        return event.itens().stream()
                .map(i -> i.preco().multiply(BigDecimal.valueOf(i.quantidade())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }


    private static List<OrderItem> getOrderItems(OrderCreatedEvent event){
        return event.itens().stream()
                .map(i -> new OrderItem(i.produto(),i.quantidade(),i.preco()))
                .toList();
    }
}
