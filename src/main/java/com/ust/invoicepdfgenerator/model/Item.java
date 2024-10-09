package com.ust.invoicepdfgenerator.model;

import lombok.Data;

@Data
public class Item {
    private String itemName;
    private int numberOfDays;
    private double pricePerDay;
    //private double totalPrice;

    public double getTotalPrice() {
        return numberOfDays * pricePerDay;
    }
}
