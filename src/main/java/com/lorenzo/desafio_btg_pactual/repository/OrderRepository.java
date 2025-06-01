package com.lorenzo.desafio_btg_pactual.repository;

import com.lorenzo.desafio_btg_pactual.entity.OrderEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<OrderEntity,Long> {
}
