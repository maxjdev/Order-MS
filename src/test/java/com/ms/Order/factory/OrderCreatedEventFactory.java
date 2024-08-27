package com.ms.Order.factory;

import com.ms.Order.listener.dto.OrderCreatedEvent;
import com.ms.Order.listener.dto.OrderItemEvent;

import java.math.BigDecimal;
import java.util.List;

public class OrderCreatedEventFactory {
    public static OrderCreatedEvent buildWithOneItem() {
        var items = new OrderItemEvent("Notebook", 1, BigDecimal.valueOf(20.50));
        var event = new OrderCreatedEvent(1L, 2L, List.of(items));
        return event;
    }
    public static OrderCreatedEvent buildWithTwoItems() {
        var item1 = new OrderItemEvent("Notebook", 1, BigDecimal.valueOf(20.50));
        var item2 = new OrderItemEvent("mouse", 1, BigDecimal.valueOf(35.25));
        var event = new OrderCreatedEvent(1L, 2L, List.of(item1, item2));
        return event;
    }
}
