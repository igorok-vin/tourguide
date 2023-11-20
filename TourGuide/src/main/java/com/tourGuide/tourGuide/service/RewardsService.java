package com.tourGuide.tourGuide.service;

import com.tourGuide.tourGuide.clients.GpsUtilProxy;
import com.tourGuide.tourGuide.clients.RewardCentralProxy;
import com.tourGuide.tourGuide.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

import static com.tourGuide.tourGuide.service.TourGuideService.testMode;

@Service
public class RewardsService {
    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

    // proximity in miles
    private int defaultProximityBuffer = 10;
    private int proximityBuffer = defaultProximityBuffer;
    private int attractionProximityRange = 200;

    @Autowired
    private GpsUtilProxy gpsUtilProxy;

    @Autowired
    private RewardCentralProxy rewardCentralProxy;


    final ExecutorService executorService = Executors.newFixedThreadPool(800);

    @Autowired
    public RewardsService(GpsUtilProxy gpsUtilProxy, RewardCentralProxy rewardCentralProxy) {
        this.gpsUtilProxy = gpsUtilProxy;
        this.rewardCentralProxy = rewardCentralProxy;
    }

    public void setProximityBuffer(int proximityBuffer) {
        this.proximityBuffer = proximityBuffer;
    }

    public void setDefaultProximityBuffer() {
        proximityBuffer = defaultProximityBuffer;
    }


    public void calculateRewardsForListUsers(List<User> userList) {
        CompletableFuture.runAsync(() -> {
            userList.forEach(user -> {
                calculateRewards(user);
            });
        }, executorService);
    }

    public void calculateRewards(User user) {
        List<VisitedLocation> userLocations = new CopyOnWriteArrayList<>(user.getVisitedLocations());
        List<Attraction> attractions = new CopyOnWriteArrayList<>(gpsUtilProxy.getAttractions());

        for (VisitedLocation visitedLocation : userLocations) {
            for (Attraction attraction : attractions) {
                if (user.getUserRewards().stream().noneMatch(r -> r.getAttraction().getAttractionName().equals(attraction.getAttractionName()))) {
                    if (nearAttraction(visitedLocation, attraction)) {
                        if(testMode){
                            CompletableFuture.supplyAsync(() -> getRewardPoints(attraction.getAttractionId(), user.getUserId()), executorService).thenAccept(points -> {
                                UserReward userReward = new UserReward(visitedLocation, attraction, points);
                                user.addUserReward(userReward, user);
                            }).join();
                        } else {
                            CompletableFuture.supplyAsync(() -> getRewardPoints(attraction.getAttractionId(), user.getUserId()), executorService).thenAccept(points -> {
                                UserReward userReward = new UserReward(visitedLocation, attraction, points);
                                user.addUserReward(userReward, user);
                            });
                        }
                    }
                }
            }
        }
    }


    public void shutdownExecutorService() throws InterruptedException {
        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    }

    public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
        return getDistance(attraction, location) > attractionProximityRange ? false : true;
    }

    private boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
        return getDistance(attraction, visitedLocation.getLocation()) > proximityBuffer ? false : true;
    }

    public int getRewardPoints(UUID attractionId, UUID userId) {
        return rewardCentralProxy.getAttractionRewardPoints(attractionId, userId);
    }

    public double getDistance(Location loc1, Location loc2) {
        double lat1 = Math.toRadians(loc1.getLatitude());
        double lon1 = Math.toRadians(loc1.getLongitude());
        double lat2 = Math.toRadians(loc2.getLatitude());
        double lon2 = Math.toRadians(loc2.getLongitude());

        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
        return statuteMiles;
    }

}
