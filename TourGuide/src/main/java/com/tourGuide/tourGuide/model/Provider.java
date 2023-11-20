package com.tourGuide.tourGuide.model;

import java.util.UUID;

public class Provider {
    public String name;
    public double price;
    public UUID tripId;

    public Provider(UUID tripId, String name, double price) {
        this.name = name;
        this.tripId = tripId;
        this.price = price;
    }

    public Provider() {
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public UUID getTripId() {
        return tripId;
    }

    @Override
    public String toString() {
        return "Provider{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", tripId=" + tripId +
                '}';
    }

}
