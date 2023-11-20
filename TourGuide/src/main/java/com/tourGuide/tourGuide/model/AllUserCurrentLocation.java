package com.tourGuide.tourGuide.model;

import java.util.UUID;

public class AllUserCurrentLocation {
    UUID userId;
    Location location;

    public AllUserCurrentLocation(UUID userId, Location location) {
        this.userId = userId;
        this.location = location;
    }

    public AllUserCurrentLocation() {
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "AllUserCurrentLocation{" +
                "userId=" + userId +
                ", location=" + location +
                '}';
    }
}
