package com.satyik.orderservice.service;

import com.satyik.orderservice.dto.OrderLineItemsDto;
import com.satyik.orderservice.dto.OrderRequest;
import com.satyik.orderservice.model.Order;
import com.satyik.orderservice.model.OrderLineItems;
import com.satyik.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;
    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemsList(orderLineItems);

        Boolean result = webClient.get()
                        .uri("http:/localhost:8082/api/inventory")
                        .retrieve()
                        .bodyToMono(Boolean.class)
                        .block();

        if(Boolean.TRUE.equals(result))
            orderRepository.save(order);
        else
            throw new IllegalArgumentException("Product is not in Stock");
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
