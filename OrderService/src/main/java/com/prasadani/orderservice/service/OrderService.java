package com.prasadani.orderservice.service;

import com.prasadani.orderservice.models.Order;

public interface OrderService {
    Order saveNewOrder(Order order)

    Order updateOrder(Order order);

    Order findOrder(String oid);
}
