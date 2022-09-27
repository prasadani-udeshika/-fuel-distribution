package com.prasadani.orderservice.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prasadani.orderservice.models.Event;
import com.prasadani.orderservice.models.Order;
import com.prasadani.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Consumer {

    @Autowired
    Producer producer;

    @Autowired
    OrderService orderService;

    ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "new-order-response")
    public void readFromTopic(String message) throws JsonProcessingException {


        System.out.println("Incomming message found. : " + message);
        String now = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now());
        Event event = objectMapper.readValue(message, Event.class);

        if (event.getType().equals("allocation-complete")){
            String eventData = event.getData();
            String allocatedDateTime = eventData.split("#")[0];
            String responseMsg = eventData.split("#")[1];
            Order order = orderService.findOrder(event.getOrderId());
            if (event.getResult().equals("success")){
                order.setCurrentStatus("ALLOCATED");
                order.getWorkFlowStatus().getAllocation().setStatus("success");
                order.getWorkFlowStatus().getAllocation().setDate(allocatedDateTime);
            }else {
                order.getWorkFlowStatus().getAllocation().setStatus("failed: "+responseMsg);
            }
            Order o1 = orderService.updateOrder(order);
            if (o1.getCurrentStatus().equals("ALLOCATED")){
                String allocatedDate = o1.getWorkFlowStatus().getAllocation().getDate().split(" ")[0];
                producer.publishToTopic(new Event(o1.getOrderId(), "ALLOCATION_COMPLETE", allocatedDate, "pending", "order-service"));
            }else {
                System.out.println("Allocation status is not updated....");
            }
        } else if (event.getType().equals("schedule-complete")) {
            String scheduledDate = event.getData();
            Order order = orderService.findOrder(event.getOrderId());
            if (event.getResult().equals("success")){
                order.setCurrentStatus("SCHEDULLED");
                order.getWorkFlowStatus().getSchedule().setStatus("success");
                order.getWorkFlowStatus().getSchedule().setArrival_date(scheduledDate);
            }else {
                order.getWorkFlowStatus().getSchedule().setStatus("failed");
            }
            order.getWorkFlowStatus().getSchedule().setProcessdate(now);
            orderService.updateOrder(order);

        } else if (event.getType().equals("to-dispatch-process")) {
            Order order = orderService.findOrder(event.getOrderId());
            if (order.getCurrentStatus().equals("SCHEDULLED")){
                producer.publishToTopic(new Event(order.getOrderId(), "TO_DISPATCH", null, "pending", "order-service"));
            }else {
                System.out.println("Can't dispatch order because it is not scheduled yet....or already dispatched...");
            }
        } else if (event.getType().equals("dispatch-complete")) {
            String eventData = event.getData();
            String dispatchedDateTime = eventData.split("#")[0];
            String responseMsg = eventData.split("#")[1];
            Order order = orderService.findOrder(event.getOrderId());
            if (event.getResult().equals("success")){
                order.setCurrentStatus("DISPATCHED");
                order.getWorkFlowStatus().getDispatch().setStatus("success");
                order.getWorkFlowStatus().getDispatch().setDate(dispatchedDateTime);
            }else {
                order.getWorkFlowStatus().getDispatch().setStatus("failed: "+responseMsg);
            }
            orderService.updateOrder(order);
        }
    }


}
