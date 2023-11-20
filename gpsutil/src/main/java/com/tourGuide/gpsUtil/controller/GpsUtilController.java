package com.tourGuide.gpsUtil.controller;

import com.tourGuide.gpsUtil.model.Attraction;
import com.tourGuide.gpsUtil.model.VisitedLocation;
import com.tourGuide.gpsUtil.service.GpsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class GpsUtilController {
    @Autowired
    private GpsUtil gpsUtil;

    @GetMapping("/getLocation")
    public VisitedLocation getUserLocation(@RequestParam UUID userId) {
        UUID uuid = UUID.fromString(String.valueOf(userId));
        return gpsUtil.getUserLocation(uuid);
    }

    @GetMapping("/getAttractions")
    public List<Attraction> getAttractions() {
        //List<Attraction> attractions = new CopyOnWriteArrayList<>();
        List<Attraction> attractions = new ArrayList<>();
        attractions.addAll(gpsUtil.getAttractions());
        return attractions;
    }
}
