package com.tourGuide.tourGuide.controller;


import com.tourGuide.tourGuide.clients.GpsUtilProxy;
import com.tourGuide.tourGuide.model.*;
import com.tourGuide.tourGuide.service.TourGuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TourGuideController {
	@Autowired
    TourGuideService tourGuideService;

    @Autowired
    GpsUtilProxy gpsUtilProxy;
	
    @GetMapping("/")
    public String index() {
        return "Greetings from TourGuide!";
    }


    @GetMapping("/getLocation")
    public VisitedLocation getLocation(@RequestParam String userName) {
    	VisitedLocation visitedLocation = tourGuideService.getUserLocation(userName);
        return visitedLocation;
    }
    
    //  TODO: Change this method to no longer return a List of Attractions.
 	//  Instead: Get the closest five tourist attractions to the user - no matter how far away they are.
 	//  Return a new JSON object that contains:
    	// Name of Tourist attraction, 
        // Tourist attractions lat/long, 
        // The user's location lat/long, 
        // The distance in miles between the user's location and each of the attractions.
        // The reward points for visiting each Attraction.
        //    Note: Attraction reward points can be gathered from RewardsCentral
    @GetMapping("/getNearbyAttractions")
    public List<NearByAttraction> getNearbyAttractions(@RequestParam String userName) /*throws JsonProcessingException*/ {
    	VisitedLocation visitedLocation = tourGuideService.getUserLocation(userName);
    	return tourGuideService.getNearByAttractions(visitedLocation);
    }
    
    @GetMapping("/getRewards")
    public List<UserReward> getRewards(@RequestParam String userName) {
        return tourGuideService.getUserRewards(userName);
    }
    
    @GetMapping("/getAllCurrentLocations")
    public List<AllUserCurrentLocation> getAllCurrentLocations() {
    	// TODO: Get a list of every user's most recent location as JSON
    	//- Note: does not use gpsUtil to query for their current location, 
    	//        but rather gathers the user's current location from their stored location history.
    	//
    	// Return object should be the just a JSON mapping of userId to Locations similar to:
    	//     {
    	//        "019b04a9-067a-4c76-8817-ee75088c3822": {"longitude":-48.188821,"latitude":74.84371} 
    	//        ...
    	//     }
        List<AllUserCurrentLocation> allUserCurrentLocations = tourGuideService.getAllUsersCurrentLocation();
        return allUserCurrentLocations;
    }

    /*http://localhost:8080/getTripDeals?userName=internalUser4&tripDuration=7&ticketQuantity=3&numberOfAdults=2&numberOfChildren=3*/
    @GetMapping("/getTripDeals")
    public List<Provider>getTripDeals (@RequestParam String userName,
                                       @RequestParam int tripDuration,
                                       @RequestParam int ticketQuantity,
                                       @RequestParam int numberOfAdults,
                                       @RequestParam(required = false) int numberOfChildren) {
        List<Provider> providers = tourGuideService.getTripDeals(userName,tripDuration, ticketQuantity,numberOfAdults,numberOfChildren );
        return providers;
    }
}