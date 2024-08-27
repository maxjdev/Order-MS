package com.ms.Order.service;

import com.ms.Order.entity.Order;
import com.ms.Order.factory.OrderCreatedEventFactory;
import com.ms.Order.factory.OrderEntityFactory;
import com.ms.Order.repository.OrderRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.bson.Document;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    OrderRepository repository;

    @Mock
    MongoTemplate template;

    @InjectMocks
    OrderService service;

    @Captor
    ArgumentCaptor<Order> orderEntityArgumentCaptor;

    @Captor
    ArgumentCaptor<Aggregation> aggregationCaptor;

    @Nested
    class Save{
        @Test
        void shouldCallRepositorySave() {
            var event = OrderCreatedEventFactory.buildWithOneItem();

            service.save(event);

            verify(repository, times(1)).save(any());
        }

        @Test
        void shouldMapEventToEntityWithSuccess() {
            var event = OrderCreatedEventFactory.buildWithOneItem();

            service.save(event);

            verify(repository, times(1)).save(orderEntityArgumentCaptor.capture());

            var entity = orderEntityArgumentCaptor.getValue();

            assertEquals(event.codigoPedido(), entity.getOrderId());
            assertEquals(event.codigoCliente(), entity.getCustomerId());
            assertNotNull(entity.getTotal());
            assertEquals(event.itens().get(0).produto(), entity.getItems().get(0).getProduct());
            assertEquals(event.itens().get(0).quantidade(), entity.getItems().get(0).getQuantity());
            assertEquals(event.itens().get(0).preco(), entity.getItems().get(0).getPrice());
        }

        @Test
        void shouldCalculateOrderTotalWithSuccess() {
            var event = OrderCreatedEventFactory.buildWithTwoItems();
            var totalItem1 = event.itens().get(0).preco().multiply(BigDecimal.valueOf(event.itens().get(0).quantidade()));
            var totalItem2 = event.itens().get(1).preco().multiply(BigDecimal.valueOf(event.itens().get(1).quantidade()));
            var orderTotal = totalItem1.add(totalItem2);

            service.save(event);

            verify(repository, times(1)).save(orderEntityArgumentCaptor.capture());

            var entity = orderEntityArgumentCaptor.getValue();

            assertNotNull(entity.getTotal());
            assertEquals(orderTotal, entity.getTotal());
        }
    }

    @Nested
    class findAllByCustomerId {

        @Test
        void shouldCallRepository() {
            var customerId = 1L;
            var pageRequest = PageRequest.of(0, 10);
            doReturn(OrderEntityFactory.buildWithPage())
                    .when(repository).findAllByCustomerId(eq(customerId), eq(pageRequest));

            var response = service.findAllByCustomerId(customerId, pageRequest);

            verify(repository, times(1)).findAllByCustomerId(eq(customerId), eq(pageRequest));
        }

        @Test
        void shouldMapResponse() {
            var customerId = 1L;
            var pageRequest = PageRequest.of(0, 10);
            var page = OrderEntityFactory.buildWithPage();
            doReturn(page).when(repository).findAllByCustomerId(anyLong(), any());

            var response = repository.findAllByCustomerId(customerId, pageRequest);

            assertEquals(page.getTotalPages(), response.getTotalPages());
            assertEquals(page.getTotalElements(), response.getTotalElements());
            assertEquals(page.getSize(), response.getSize());
            assertEquals(page.getNumber(), response.getNumber());

            assertEquals(page.getContent().get(0).getOrderId(), response.getContent().get(0).getOrderId());
            assertEquals(page.getContent().get(0).getCustomerId(), response.getContent().get(0).getCustomerId());
            assertEquals(page.getContent().get(0).getTotal(), response.getContent().get(0).getTotal());
        }
    }

    @Nested
    class FindTotalOnOrdersByCustomerId {

        @Test
        void shouldCallMongoTemplate() {
            var customerId = 1L;
            var totalExpected = BigDecimal.valueOf(1);
            var aggregationResult = mock(AggregationResults.class);
            doReturn(new Document("total",  totalExpected)).when(aggregationResult).getUniqueMappedResult();
            doReturn(aggregationResult).when(template).aggregate(any(Aggregation.class), anyString(), eq(Document.class));

            var total = service.findTotalOnOrdersByCustomerId(customerId);

            verify(template, times(1)).aggregate(any(Aggregation.class), anyString(), eq(Document.class));
            assertEquals(totalExpected, total);
        }

        @Test
        void shouldUseCorrectAggregation() {
            var customerId = 1L;
            var totalExpected = BigDecimal.valueOf(1);
            var aggregationResult = mock(AggregationResults.class);
            doReturn(new Document("total",  totalExpected)).when(aggregationResult).getUniqueMappedResult();
            doReturn(aggregationResult).when(template).aggregate(aggregationCaptor.capture(), anyString(), eq(Document.class));

            service.findTotalOnOrdersByCustomerId(customerId);

            var aggregation = aggregationCaptor.getValue();
            var aggregationExpected = newAggregation(
                    match(Criteria.where("customerId").is(customerId)),
                    group().sum("total").as("total")
            );

            assertEquals(aggregationExpected.toString(), aggregation.toString());
        }

        @Test
        void shouldQueryCorrectTable() {
            var customerId = 1L;
            var totalExpected = BigDecimal.valueOf(1);
            var aggregationResult = mock(AggregationResults.class);
            doReturn(new Document("total",  totalExpected)).when(aggregationResult).getUniqueMappedResult();
            doReturn(aggregationResult).when(template).aggregate(any(Aggregation.class), eq("tb_orders"), eq(Document.class));

            service.findTotalOnOrdersByCustomerId(customerId);

            verify(template, times(1)).aggregate(any(Aggregation.class), eq("tb_orders"), eq(Document.class));
        }
    }
}