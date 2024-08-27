package com.ms.Order.listener;

import com.ms.Order.factory.OrderCreatedEventFactory;
import com.ms.Order.service.OrderService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.support.MessageBuilder;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderCreatedListenerTest {
    @Mock
    OrderService service;

    @InjectMocks
    OrderCreatedListener listener;

    @Nested
    class listen {
        @Test
        void shouldCallServiceWithCorrectParams() {
            var event = OrderCreatedEventFactory.buildWithOneItem();
            var message = MessageBuilder.withPayload(event).build();

            listener.listen(message);

            verify(service, times(1)).save(eq(message.getPayload()));
        }
    }
}
