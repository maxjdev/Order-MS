package com.ms.Order.listener;

import com.ms.Order.listener.dto.OrderCreatedEvent;
import com.ms.Order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import static com.ms.Order.config.RabbitMqConfig.ORDER_CREATED_QUEUE;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderCreatedListener {
    private final OrderService service;

    @RabbitListener(queues = ORDER_CREATED_QUEUE)
    public void listen(Message<OrderCreatedEvent> message) {
        log.info("Message consumed: " + message);
        service.save(message.getPayload());
    }
}
