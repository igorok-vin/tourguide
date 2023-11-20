package com.tourGuide.tourGuide.service;

import com.tourGuide.tourGuide.clients.GpsUtilProxy;
import com.tourGuide.tourGuide.clients.RewardCentralProxy;
import com.tourGuide.tourGuide.helper.InternalTestHelper;
import com.tourGuide.tourGuide.model.*;
import com.tourGuide.tourGuide.tracker.Tracker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RewardsServiceTest {

    @Mock
    private GpsUtilProxy gpsUtilProxy;

    @Mock
    private RewardCentralProxy rewardCentralProxy;

    @InjectMocks
    private RewardsService rewardsService;

    private User user;

    private List<Attraction> attractionList;

    TourGuideService tourGuideService;

    private Tracker tracker = new Tracker(tourGuideService);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        InternalTestHelper.setInternalUserNumber(0);

        user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

        attractionList = new ArrayList<>();
        attractionList.add(new Attraction("Disneyland", "Anaheim", "CA", 33.817595, -117.922008));
        attractionList.add(new Attraction("Jackson Hole", "Jackson Hole", "WY", 43.582767, -110.821999));
        attractionList.add(new Attraction("Mojave National Preserve", "Kelso", "CA", 35.141689, -115.510399));
    }

    @Test
    public void userGetRewardsTest() {
        VisitedLocation visitedLocation1 = new VisitedLocation(user.getUserId(), new Location(33.817595, -117.922008), new Date());
        VisitedLocation visitedLocation2 = new VisitedLocation(user.getUserId(), new Location(43.582767, -110.821999), new Date());
        VisitedLocation visitedLocation3 = new VisitedLocation(user.getUserId(), new Location(35.141689, -115.510399), new Date());

        CopyOnWriteArrayList<VisitedLocation> visitedLocations = new CopyOnWriteArrayList<>();
        visitedLocations.add(visitedLocation1);
        visitedLocations.add(visitedLocation2);
        visitedLocations.add(visitedLocation3);
        user.setVisitedLocations(visitedLocations);

        when(gpsUtilProxy.getAttractions()).thenReturn(attractionList);
       when(rewardCentralProxy.getAttractionRewardPoints(any(),any())).thenReturn(1);
        rewardsService.setProximityBuffer(150);
        rewardsService.calculateRewards(user);
        List<UserReward> userRewards = user.getUserRewards();
        System.out.println(userRewards);
        tracker.stopTracking();

        assertEquals(3,userRewards.size());
        assertEquals(3, user.getVisitedLocations().size());
    }

    @Test
    public void isWithinAttractionProximityTest() {
        rewardsService.setProximityBuffer(150);
        Attraction attraction = new Attraction("Mojave National Preserve", "Kelso", "CA", 35.141689, -115.510399);
        VisitedLocation visitedLocation = new VisitedLocation(user.getUserId(), new Location(35.141689, -115.510399), new Date());
        boolean isWithinAttractionProximity = rewardsService.isWithinAttractionProximity(attraction, visitedLocation.getLocation());

        assertTrue(isWithinAttractionProximity);
    }
}
