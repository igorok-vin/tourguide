package com.tourGuide.tourGuide.service;

import com.tourGuide.tourGuide.clients.GpsUtilProxy;
import com.tourGuide.tourGuide.clients.RewardCentralProxy;
import com.tourGuide.tourGuide.clients.TripPricerProxy;
import com.tourGuide.tourGuide.helper.InternalTestHelper;
import com.tourGuide.tourGuide.model.*;
import com.tourGuide.tourGuide.tracker.Tracker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TourGuideServiceTest {

    @Mock
    private GpsUtilProxy gpsUtilProxy;

    @Mock
    private RewardCentralProxy rewardCentralProxy;

    @Mock
    private RewardsService rewardsService;

    @Mock
    private TripPricerProxy tripPricerProxy;

    @Mock
    private Tracker tracker;

    @InjectMocks
   private TourGuideService tourGuideService;

    private User user;

    @BeforeEach
    public void setUp() {
        InternalTestHelper.setInternalUserNumber(0);
        MockitoAnnotations.openMocks(this);

        user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
    }

    @Test
    public void getUserLocationTest() {
        tourGuideService.addUser(user);
        VisitedLocation visitedLocation = new VisitedLocation(user.getUserId(), new Location(43.582767, -110.821999), new Date());
        user.addToVisitedLocations(visitedLocation);

        VisitedLocation location = tourGuideService.getUserLocation(user.getUserName());
        tourGuideService.tracker.stopTracking();

        assertEquals(-110.821999, location.getLocation().getLongitude());
    }

    @Test
    public void addUserTest() {
        tourGuideService.addUser(user);
        User retrivedUser = tourGuideService.getUser("jon");
        tourGuideService.tracker.stopTracking();
        assertEquals(user, retrivedUser);
    }

    @Test
    public void getAllUsersTest() {
        User user1 = new User(UUID.randomUUID(), "john", "000", "jon@tourGuide.com");
        User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");
        tourGuideService.addUser(user);
        tourGuideService.addUser(user1);
        tourGuideService.addUser(user2);
        List<User> result = tourGuideService.getAllUsers();

        assertTrue(result.contains(user));
        assertTrue(result.contains(user1));
        assertTrue(result.contains(user2));
    }

    @Test
    public void getNearbyAttractionsTest() {
        VisitedLocation visitedLocation = new VisitedLocation(user.getUserId(), new Location(43.582767, -110.821999), new Date());

        CopyOnWriteArrayList<Attraction> attractionList = new CopyOnWriteArrayList<>();
        attractionList.add(new Attraction("Disneyland", "Anaheim", "CA", 33.817595, -117.922008));
        attractionList.add(new Attraction("Jackson Hole", "Jackson Hole", "WY", 43.582767, -110.821999));
        attractionList.add(new Attraction("Mojave National Preserve", "Kelso", "CA", 35.141689, -115.510399));

        doReturn(attractionList).when(gpsUtilProxy).getAttractions();
        when(rewardCentralProxy.getAttractionRewardPoints(any(UUID.class), any(UUID.class))).thenReturn(275, 533, 478);

        List<NearByAttraction> result = tourGuideService.getNearByAttractions(visitedLocation);

        tourGuideService.tracker.stopTracking();
        assertEquals(3, result.size());
    }

    @Test
    public void getTripDealsTest() {
        tourGuideService.addUser(user);
        User retrivedUser = tourGuideService.getUser("jon");
        VisitedLocation visitedLocation = new VisitedLocation(retrivedUser.getUserId(), new Location(35.141689, -115.510399), new Date());
        Attraction attraction = new Attraction("Mojave National Preserve", "Kelso", "CA", 35.141689, -115.510399);
        List<UserReward> rewardList = new ArrayList<>();
        rewardList.add(new UserReward(visitedLocation, attraction, 150));
        List<Provider> providersTest = Arrays.asList(
                new Provider(UUID.randomUUID(), "Hawaii Tours", 1850D),
                new Provider(UUID.randomUUID(), "Whistler Ski", 1430D));

        when(tripPricerProxy.getPrice(anyString(), any(UUID.class), anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(providersTest);

        List<Provider> result = tourGuideService.getTripDeals("jon", 2, 5, 2, 3);

        tourGuideService.tracker.stopTracking();
        assertEquals(2, result.size());
        assertEquals(1430D, result.get(1).getPrice());

    }
}
