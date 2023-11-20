package com.tourGuide.gpsUtil.service;

import com.tourGuide.gpsUtil.model.Attraction;
import com.tourGuide.gpsUtil.model.Location;
import com.tourGuide.gpsUtil.model.VisitedLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


public class GpsUtilTest {
    private GpsUtil gpsUtil;

    @BeforeEach
    void setup() {
        gpsUtil = new GpsUtil();
    }

    @Test
    void getAttractionsTest() {
        int attractionsCount = gpsUtil.getAttractions().size();
        List< Attraction> attractionList = gpsUtil.getAttractions();

        assertThat(attractionsCount).isEqualTo(26);
        assertThat(attractionList.get(1).attractionName).isEqualTo("Jackson Hole");
        assertThat(attractionList.get(5).city).isEqualTo("Hot Springs");
        assertThat(attractionList.get(2).latitude).isEqualTo(35.141689);
        assertThat(attractionList.get(3).longitude).isEqualTo(-115.90065);
        assertThat(attractionList.get(4).state).isEqualTo("AR");
    }

    @Test
    void  getUserLocationTest() {
        UUID userId = UUID.randomUUID();

        VisitedLocation result = gpsUtil.getUserLocation(userId);

        assertThat(result.userId).isEqualTo(userId);
    }
}
