package com.tourGuide.tourGuide.clients;

import com.tourGuide.tourGuide.model.Attraction;
import com.tourGuide.tourGuide.model.VisitedLocation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "${gps_util-service-name}", url = "${gps_util-service-url}")
public interface GpsUtilProxy {
    @GetMapping("/getAttractions")
    List<Attraction> getAttractions();

    @GetMapping("/getLocation")
    VisitedLocation getUserLocation (@RequestParam UUID userId);


}
