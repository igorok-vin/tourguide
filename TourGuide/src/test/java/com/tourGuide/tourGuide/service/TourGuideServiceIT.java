package com.tourGuide.tourGuide.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tourGuide.tourGuide.clients.GpsUtilProxy;
import com.tourGuide.tourGuide.clients.RewardCentralProxy;
import com.tourGuide.tourGuide.clients.TripPricerProxy;
import com.tourGuide.tourGuide.helper.InternalTestHelper;
import com.tourGuide.tourGuide.model.NearByAttraction;
import com.tourGuide.tourGuide.model.Provider;
import com.tourGuide.tourGuide.model.User;
import com.tourGuide.tourGuide.model.VisitedLocation;
import com.tourGuide.tourGuide.tracker.Tracker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TourGuideServiceIT {

    @Autowired
    private GpsUtilProxy gpsUtilProxy;

    @Autowired
    private RewardCentralProxy rewardCentralProxy;

    @Autowired
    private RewardsService rewardsService;

    @Autowired
    private TripPricerProxy tripPricerProxy;

    @Autowired
    private TourGuideService tourGuideService;


    @Autowired
    private Tracker tracker;

    @BeforeEach
    public void setUp() {
        InternalTestHelper.setInternalUserNumber(0);
    }

    @Test
    public void getUserLocationTest() {
        TourGuideService tourGuideService = new TourGuideService(gpsUtilProxy, rewardsService, rewardCentralProxy, tripPricerProxy);
        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user).join();
        tourGuideService.tracker.stopTracking();
        assertTrue(visitedLocation.getUserId().equals(user.getUserId()));
    }

    @Test
    public void addUserTest() {
        TourGuideService tourGuideService = new TourGuideService(gpsUtilProxy, rewardsService, rewardCentralProxy, tripPricerProxy);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

        tourGuideService.addUser(user);
        tourGuideService.addUser(user2);

        User retrivedUser = tourGuideService.getUser(user.getUserName());
        User retrivedUser2 = tourGuideService.getUser(user2.getUserName());

        tourGuideService.tracker.stopTracking();

        assertEquals(user, retrivedUser);
        assertEquals(user2, retrivedUser2);
    }

    @Test
    public void getAllUsersTest() {
        TourGuideService tourGuideService = new TourGuideService(gpsUtilProxy, rewardsService, rewardCentralProxy, tripPricerProxy);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

        tourGuideService.addUser(user);
        tourGuideService.addUser(user2);

        List<User> allUsers = tourGuideService.getAllUsers();

        tourGuideService.tracker.stopTracking();

        assertTrue(allUsers.contains(user));
        assertTrue(allUsers.contains(user2));
    }

    @Test
    public void trackUser() {
        TourGuideService tourGuideService = new TourGuideService(gpsUtilProxy, rewardsService, rewardCentralProxy, tripPricerProxy);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user).join();

        tourGuideService.tracker.stopTracking();

        assertEquals(user.getUserId(), visitedLocation.getUserId());
    }


    @Test
    public void getNearbyAttractionsTest() {
        TourGuideService tourGuideService = new TourGuideService(gpsUtilProxy, rewardsService, rewardCentralProxy, tripPricerProxy);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
       // tourGuideService.trackUser(user);
        tourGuideService.trackUserLocation(user).join();

        List<NearByAttraction> attractions = tourGuideService.getNearByAttractions(user.getLastVisitedLocation());

        tourGuideService.tracker.stopTracking();
        assertEquals(5, attractions.size());
    }

    @Test
    public void getTripDealsTest() {
        TourGuideService tourGuideService = new TourGuideService(gpsUtilProxy, rewardsService, rewardCentralProxy, tripPricerProxy);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        tourGuideService.addUser(user);

        List<Provider> providers = tourGuideService.getTripDeals(user.getUserName(),5,3,2,2);

        tourGuideService.tracker.stopTracking();

        assertEquals(8, providers.size());
    }
}
