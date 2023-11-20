package com.tourGuide.tourGuide.model;

public class NearByAttraction {
    private String attractionName;

    private double attractionLatitude;

    private double attractionLongitude;

    private double userLatitude;

    private double userLongitude;

    private double distance;

    private int rewardPoints;

    public NearByAttraction() {
    }
    public NearByAttraction(String attractionName, double attractionLatitude, double attractionLongitude,  double userLatitude, double userLongitude, double distance, int rewardPoints) {
        this.attractionName = attractionName;
        this.attractionLatitude = attractionLatitude;
        this.attractionLongitude = attractionLongitude;
        this.userLatitude = userLatitude;
        this.userLongitude = userLongitude;
        this.distance = distance;
        this.rewardPoints = rewardPoints;
    }

    public NearByAttraction(String attractionName, double attractionLongitude, double attractionLatitude, double distance, int rewardPoints) {
        this.attractionName = attractionName;
        this.attractionLongitude = attractionLongitude;
        this.attractionLatitude = attractionLatitude;
        this.distance = distance;
        this.rewardPoints = rewardPoints;
    }

    public String getAttractionName() {
        return attractionName;
    }

    public void setAttractionName(String attractionName) {
        this.attractionName = attractionName;
    }

    public double getAttractionLatitude() {
        return attractionLatitude;
    }

    public void setAttractionLatitude(double attractionLatitude) {
        this.attractionLatitude = attractionLatitude;
    }

    public double getAttractionLongitude() {
        return attractionLongitude;
    }

    public void setAttractionLongitude(double attractionLongitude) {
        this.attractionLongitude = attractionLongitude;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getUserLatitude() {
        return userLatitude;
    }

    public void setUserLatitude(double userLatitude) {
        this.userLatitude = userLatitude;
    }

    public double getUserLongitude() {
        return userLongitude;
    }

    public void setUserLongitude(double userLongitude) {
        this.userLongitude = userLongitude;
    }

    public int getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(int rewardPoints) {
        this.rewardPoints = rewardPoints;
    }

    @Override
    public String toString() {
        return "NearByAttraction{" +
                "attractionName='" + attractionName + '\'' +
                ", attractionLatitude=" + attractionLatitude +
                ", attractionLongitude=" + attractionLongitude +
                ", userLatitude=" + userLatitude +
                ", userLongitude=" + userLongitude +
                ", distance=" + distance +
                ", rewardPoints=" + rewardPoints +
                '}';
    }
}