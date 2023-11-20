package com.tourGuide.tourGuide.clients;

import com.tourGuide.tourGuide.model.Provider;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "${trip_pricer-service-name}", url = "${trip_pricer-service-url}")
public interface TripPricerProxy {
    @GetMapping("/getTripDeals")
    List<Provider> getPrice(@RequestParam String apiKey,
                                @RequestParam UUID attractionId, @RequestParam int adults,
                                @RequestParam int children,
                                @RequestParam int nightsStay, @RequestParam int rewardsPoints);
}
