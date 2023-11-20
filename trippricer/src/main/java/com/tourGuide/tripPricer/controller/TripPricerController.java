package com.tourGuide.tripPricer.controller;

import com.tourGuide.tripPricer.model.Provider;
import com.tourGuide.tripPricer.service.TripPricerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class TripPricerController {
    @Autowired
    private TripPricerService tripPricerService;

    @GetMapping("/getTripDeals")
    List<Provider> getPrice(@RequestParam String apiKey,
                            @RequestParam UUID attractionId, @RequestParam int adults,
                            @RequestParam int children,
                            @RequestParam int nightsStay, @RequestParam int rewardsPoints){
        return tripPricerService.getPrice(apiKey,attractionId,adults,children,nightsStay,rewardsPoints);
    }

}
