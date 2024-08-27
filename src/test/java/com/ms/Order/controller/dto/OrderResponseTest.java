package com.ms.Order.controller.dto;

import com.ms.Order.factory.OrderEntityFactory;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OrderResponseTest {
    @Nested
    class FromEntity {
        @Test
        void shouldMapCorrectly() {
            var input = OrderEntityFactory.build();

            var output = OrderResponse.fromResponse(input);

            assertEquals(input.getOrderId() ,output.orderId());
            assertEquals(input.getCustomerId() ,output.customerId());
            assertEquals(input.getTotal() ,output.total());
        }
    }
}
