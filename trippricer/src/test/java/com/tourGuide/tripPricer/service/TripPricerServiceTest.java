package com.tourGuide.tripPricer.service;

import com.tourGuide.tripPricer.model.Provider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TripPricerServiceTest {
    TripPricerServiceImp tripPricerService;

    @BeforeEach
    public void setUp() {
        tripPricerService = new TripPricerServiceImp();
    }

    @Test
    public void getPrice(){
        String api = "stringApi";
        UUID attractionId = UUID.randomUUID();
        int adults = 2;
        int children = 2;
        int nightsStay = 7;
        int rewardsPoints = 200;
        List<Provider> result1 = tripPricerService.getPrice(api, attractionId,adults,children,nightsStay,rewardsPoints);

        String api2 = "stringApi";
        UUID attractionId2 = UUID.randomUUID();
        int adults2 = 2;
        int children2 = 1;
        int nightsStay2 = 5;
        int rewardsPoints2 = 500;
        List<Provider> result2 = tripPricerService.getPrice(api2, attractionId2,adults2,children2,nightsStay2,rewardsPoints2);

        assertTrue(result1.size()>0);
        assertTrue(result2.size()>0);
    }

}
