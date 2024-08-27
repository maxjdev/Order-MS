package com.ms.Order.factory;

import com.ms.Order.entity.Order;
import com.ms.Order.entity.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.util.List;

public class OrderEntityFactory {
    public static Order build() {
        var items = new OrderItem("notebook", 1, BigDecimal.valueOf(20.50));
        var entity = new Order();

        entity.setOrderId(1L);
        entity.setCustomerId(2L);
        entity.setTotal(BigDecimal.valueOf(20.50));
        entity.setItems(List.of(items));

        return entity;
    }
    public static Page<Order> buildWithPage() {
        return new PageImpl<>(List.of(build()));
    }
}
