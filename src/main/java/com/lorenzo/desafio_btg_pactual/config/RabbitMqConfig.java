package com.lorenzo.desafio_btg_pactual.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMqConfig {

    public static final String FANOUT_ORDER_EXCHANGE = "btg-pactual.order-created.exchange";
    public static final String ORDER_CREATED_QUEUE = "order-created";
    public static final String ORDER_CREATED_DLQ = "order-created.dlq";
    public static final String FANOUT_ORDER_DLX = "orders.v1.order-created.dlx";

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public Queue orderQueueDLQ(){
        return new Queue(ORDER_CREATED_DLQ);
    }
    @Bean
    public FanoutExchange fanoutExchangeDLX(){
        return new FanoutExchange(FANOUT_ORDER_DLX);
    }
    @Bean
    public Binding bindingDLQ(){
        var queue = orderQueueDLQ();
        var exchange = fanoutExchangeDLX();
        return BindingBuilder.bind(queue).to(exchange);
    }

    @Bean
    public Queue orderQueue(){
        Map<String,Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange",FANOUT_ORDER_DLX);
        return new Queue(ORDER_CREATED_QUEUE,true,false,false, args);
    }
    @Bean
    public FanoutExchange orderFanoutExchange(){
        return new FanoutExchange(FANOUT_ORDER_EXCHANGE);
    }
    @Bean
    public Binding bind(){
        var queue = orderQueue();
        var exchange = orderFanoutExchange();
        return BindingBuilder.bind(queue).to(exchange);
    }
}
