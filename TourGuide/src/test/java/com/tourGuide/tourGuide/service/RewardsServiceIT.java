package com.tourGuide.tourGuide.service;

import com.tourGuide.tourGuide.clients.GpsUtilProxy;
import com.tourGuide.tourGuide.clients.RewardCentralProxy;
import com.tourGuide.tourGuide.clients.TripPricerProxy;
import com.tourGuide.tourGuide.helper.InternalTestHelper;
import com.tourGuide.tourGuide.model.Attraction;
import com.tourGuide.tourGuide.model.User;
import com.tourGuide.tourGuide.model.UserReward;
import com.tourGuide.tourGuide.model.VisitedLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class RewardsServiceIT {

    @Autowired
    private GpsUtilProxy gpsUtilProxy;

    @Autowired
    private RewardCentralProxy rewardCentralProxy;

    @Autowired
    private TripPricerProxy tripPricerProxy;

    @BeforeEach
    public void setUp() {
        InternalTestHelper.setInternalUserNumber(0);
        Locale.setDefault(Locale.US);
    }

    @Test
    public void testGetAllAttractionsTest() {
        List<Attraction> attractions = gpsUtilProxy.getAttractions();
        System.out.println(attractions);
        Assertions.assertEquals(attractions.size(), 26);
    }

    @Test
    public void userGetRewardsTest() {
        InternalTestHelper.setInternalUserNumber(0);
        RewardsService rewardsService = new RewardsService(gpsUtilProxy, rewardCentralProxy);
        TourGuideService tourGuideService = new TourGuideService(gpsUtilProxy, rewardsService,rewardCentralProxy,tripPricerProxy);
        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        Attraction attraction = gpsUtilProxy.getAttractions().get(0);
        user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));

        tourGuideService.trackUserLocation(user).join();
        List<UserReward> userRewards = user.getUserRewards();
        System.out.println("-------------"+userRewards);
        tourGuideService.tracker.stopTracking();
        assertTrue(userRewards.size() > 0);
    }

    @Test
    public void isWithinAttractionProximityTest() {
        RewardsService rewardsService = new RewardsService(gpsUtilProxy, rewardCentralProxy);
        Attraction attraction = gpsUtilProxy.getAttractions().get(0);
        assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
    }

    @Test
    public void nearAllAttractionsTest() {
        RewardsService rewardsService = new RewardsService(gpsUtilProxy, rewardCentralProxy);
        rewardsService.setProximityBuffer(Integer.MAX_VALUE);
        InternalTestHelper.setInternalUserNumber(10);
        TourGuideService tourGuideService = new TourGuideService(gpsUtilProxy, rewardsService,rewardCentralProxy,tripPricerProxy);
        rewardsService.calculateRewards(tourGuideService.getAllUsers().get(0));
        List<UserReward> userRewards = tourGuideService.getUserRewards(tourGuideService.getAllUsers().get(0).getUserName());
        tourGuideService.tracker.stopTracking();
        assertEquals(gpsUtilProxy.getAttractions().size(), userRewards.size());
    }
}
