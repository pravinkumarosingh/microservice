package com.service.order.service;

import com.service.order.client.InventoryClient;
import com.service.order.dto.OrderRequest;
import com.service.order.model.Order;
import com.service.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    public void placeOrder(OrderRequest orderRequest) {

        boolean isProductInStock = inventoryClient.isInStock(orderRequest.getSkuCode(), orderRequest.getQuantity());

        if (isProductInStock){
            Order order = Order.builder()
                    .orderNo(UUID.randomUUID().toString())
                    .skuCode(orderRequest.getSkuCode())
                    .price(orderRequest.getPrice())
                    .quantity(orderRequest.getQuantity())
                    .build();
            orderRepository.save(order);
            log.info("Place {} order" , order.getSkuCode());
        } else {
            throw new RuntimeException("Product with SKU Code " + orderRequest.getSkuCode() + " is not in stock");
        }


    }

}
