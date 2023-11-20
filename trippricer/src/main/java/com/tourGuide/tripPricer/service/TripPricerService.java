package com.tourGuide.tripPricer.service;

import com.tourGuide.tripPricer.model.Provider;

import java.util.List;
import java.util.UUID;

public interface TripPricerService {
    List<Provider> getPrice(String apiKey, UUID attractionId, int adults, int children, int nightsStay, int rewardsPoints);

    String getProviderName(String apiKey, int adults);
}
