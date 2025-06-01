package com.lorenzo.desafio_btg_pactual.listener;

import com.lorenzo.desafio_btg_pactual.listener.dto.OrderCreatedEvent;
import com.lorenzo.desafio_btg_pactual.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import static com.lorenzo.desafio_btg_pactual.config.RabbitMqConfig.ORDER_CREATED_QUEUE;

@Component
public class OrderCreatedListener {
     private final OrderService orderService;

    public OrderCreatedListener(OrderService orderService) {
        this.orderService = orderService;
    }

    @RabbitListener(queues = ORDER_CREATED_QUEUE)
    public void listen(Message<OrderCreatedEvent> message){
        orderService.save(message.getPayload());
    }
}
