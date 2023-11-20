package com.tourGuide.gpsUtil.IT;

import com.tourGuide.gpsUtil.controller.GpsUtilController;
import com.tourGuide.gpsUtil.model.Attraction;
import com.tourGuide.gpsUtil.model.VisitedLocation;
import com.tourGuide.gpsUtil.service.GpsUtil;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

//@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GpsUtilIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GpsUtil gpsUtil;

    @Autowired
    private GpsUtilController gpsUtilController;
    @Autowired
    WebApplicationContext context;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    public void getUserLocationTest() throws Exception {
        UUID uuid = UUID.randomUUID();
        VisitedLocation visitedLocation = gpsUtil.getUserLocation(uuid);
        mockMvc.perform(get("/getLocation").param("userId", String.valueOf(uuid)))
                .andDo(print())
                .andExpect(status().isOk());
        assertTrue(visitedLocation.userId == uuid);;
    }

    @Test
    public void getAttractionsTest() throws Exception {
        List<Attraction> attractions = gpsUtil.getAttractions();
        mockMvc.perform(get("/getAttractions"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[2].attractionName",is("Mojave National Preserve")))
                .andExpect(jsonPath("$.length()",is(26)));

        assertTrue(attractions.size()==26);
    }

}
