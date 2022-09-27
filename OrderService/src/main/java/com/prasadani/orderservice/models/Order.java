package com.prasadani.orderservice.models;

import com.prasadani.orderservice.models.workflow.WorkFlowStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Order")
public class Order {

    @Id
    private String OrderId;
    private int fuelTypeId;
    private String fuelDescription;
    private int quantity;
    private String fuelStationName;
    private String fuelStationEmail;
    private String currentStatus;
    private WorkFlowStatus workFlowStatus;

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public int getFuelTypeId() {
        return fuelTypeId;
    }

    public void setFuelTypeId(int fuelTypeId) {
        this.fuelTypeId = fuelTypeId;
    }

    public String getFuelDescription() {
        return fuelDescription;
    }

    public void setFuelDescription(String fuelDescription) {
        this.fuelDescription = fuelDescription;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getFuelStationName() {
        return fuelStationName;
    }

    public void setFuelStationName(String fuelStationName) {
        this.fuelStationName = fuelStationName;
    }

    public String getFuelStationEmail() {
        return fuelStationEmail;
    }

    public void setFuelStationEmail(String fuelStationEmail) {
        this.fuelStationEmail = fuelStationEmail;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public WorkFlowStatus getWorkFlowStatus() {
        return workFlowStatus;
    }

    public void setWorkFlowStatus(WorkFlowStatus workFlowStatus) {
        this.workFlowStatus = workFlowStatus;
    }
}
