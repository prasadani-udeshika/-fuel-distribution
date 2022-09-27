package com.prasadani.orderservice.repository;

import com.prasadani.orderservice.models.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String>{
    
}
