package com.tourGuide.tourGuide.TestPerfomence;

import com.tourGuide.tourGuide.clients.GpsUtilProxy;
import com.tourGuide.tourGuide.clients.RewardCentralProxy;
import com.tourGuide.tourGuide.clients.TripPricerProxy;
import com.tourGuide.tourGuide.helper.InternalTestHelper;
import com.tourGuide.tourGuide.model.Attraction;
import com.tourGuide.tourGuide.model.User;
import com.tourGuide.tourGuide.model.VisitedLocation;
import com.tourGuide.tourGuide.service.RewardsService;
import com.tourGuide.tourGuide.service.TourGuideService;
import org.apache.commons.lang3.time.StopWatch;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class PerformanceTest {

    @Autowired
    private GpsUtilProxy gpsUtilProxy;

    @Autowired
    private RewardCentralProxy rewardCentralProxy;

    @Autowired
    private TripPricerProxy tripPricerProxy;

    @Autowired
    TourGuideService tourGuideService;

    @Autowired
    RewardsService rewardsService;

    /*
     * A note on performance improvements:
     *
     *     The number of users generated for the high volume tests can be easily adjusted via this method:
     *
     *     		InternalTestHelper.setInternalUserNumber(100000);
     *
     *
     *     These tests can be modified to suit new solutions, just as long as the performance metrics
     *     at the end of the tests remains consistent.
     *
     *     These are performance metrics that we are trying to hit:
     *
     *     highVolumeTrackLocation: 100,000 users within 15 minutes:
     *     		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
     *
     *     highVolumeGetRewards: 100,000 users within 20 minutes:
     *          assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
     */

    @Test
    public void getUserLocationTest() {

        RewardsService rewardsService = new RewardsService(gpsUtilProxy, rewardCentralProxy);
        TourGuideService tourGuideService = new TourGuideService(gpsUtilProxy, rewardsService, rewardCentralProxy, tripPricerProxy);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

        VisitedLocation location = tourGuideService.trackUserLocation(user).join();
        tourGuideService.tracker.stopTracking();
        assertTrue(location.getUserId().equals(user.getUserId()));
    }

    //Test result: highVolumeTrackLocation: Time Elapsed: 390 seconds.
    @Test
    public void highVolumeTrackLocationTest() throws InterruptedException {
        rewardsService = new RewardsService(gpsUtilProxy, rewardCentralProxy);
        // Users should be incremented up to 100,000, and test finishes within 15 minutes
        InternalTestHelper.setInternalUserNumber(1);
        tourGuideService = new TourGuideService(gpsUtilProxy, rewardsService, rewardCentralProxy, tripPricerProxy);
        List<User> allUsers;
        allUsers = tourGuideService.getAllUsers();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (User user : allUsers) {
            tourGuideService.trackUserLocation(user);
        }
        tourGuideService.shutdownExecutorService();
        rewardsService.shutdownExecutorService();
        stopWatch.stop();
        tourGuideService.tracker.stopTracking();

        System.out.println("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
        assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }

    //Test result: highVolumeGetRewards: Time Elapsed: 510s with setInternalUserNumber(100000).
    @Test
    public void highVolumeGetRewardsTest() throws InterruptedException {
        rewardsService = new RewardsService(gpsUtilProxy, rewardCentralProxy);
        // Users should be incremented up to 100,000, and test finishes within 15 minutes
        InternalTestHelper.setInternalUserNumber(1);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        tourGuideService = new TourGuideService(gpsUtilProxy, rewardsService, rewardCentralProxy, tripPricerProxy);
        List<User> allUsers = tourGuideService.getAllUsers();
        Attraction attractionList =  gpsUtilProxy.getAttractions().get(0);
        for(User user: allUsers) {
            user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attractionList, new Date()));
        }
       rewardsService.calculateRewardsForListUsers(allUsers);
        for (User user1 : allUsers) {
            while (user1.getUserRewards().isEmpty()) {
                TimeUnit.MILLISECONDS.sleep(10);
            }
        }
        stopWatch.stop();
        tourGuideService.tracker.stopTracking();
        System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");

        assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
        for (User user : allUsers) {
            assertTrue(user.getUserRewards().size() > 0);}
    }
}
