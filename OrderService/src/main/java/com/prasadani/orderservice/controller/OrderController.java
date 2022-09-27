package com.prasadani.orderservice.controller;


import com.prasadani.orderservice.models.Order;
import com.prasadani.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {

    @Autowired
    OrderService orderService;

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public Order newOrder(@RequestBody Order order) {

        return orderService.saveNewOrder(order);

    }

    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public Order getOrder(@RequestParam String oid){
        return orderService.findOrder(oid);
    }



}
