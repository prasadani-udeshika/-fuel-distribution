package com.prasadani.orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.prasadani.orderservice.models.Event;
import com.prasadani.orderservice.models.Order;
import com.prasadani.orderservice.models.workflow.*;
import com.prasadani.orderservice.repository.OrderRepository;
import com.prasadani.orderservice.service.kafka.Producer;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class OrderServiceImpl  implements OrderService{
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    Producer producer;

    @Override
    public Order saveNewOrder(Order order){

     String now = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now());

     order.setCurrentStatus("ORDER_CREATED");
     order.setWorkFlowStatus(new WorkFlowStatus(
             new Created("success",now),
             new Allocation("pending","-"),
             new Schedule("pending","-","-"),
             new Dispatch("pending","-"),
             new OrderReceived("pending","-")

     ));

        Order result = orderRepository.save(order);

        Event event = new Event(
                result.getOrderId(),
                "NEW_ORDER",
                result.getFuelTypeId()+"#"+result.getQuantity(),
                "PENDING",
                "order-service"

        );
        try {
            producer.publishToTopic(event);
        } catch (JsonProcessingException e) {
            System.out.println("Error while publishing event from order service");
            throw new RuntimeException(e);
        }

        return result;

    }

    @Override
    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order findOrder(String oid) {
        Optional<Order> order = orderRepository.findById(oid);
        if (order.isPresent()){
            return order.get();
        }else {
            return null;
        }
    }
}
