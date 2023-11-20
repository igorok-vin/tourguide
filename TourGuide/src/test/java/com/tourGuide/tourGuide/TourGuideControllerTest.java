package com.tourGuide.tourGuide;

import com.tourGuide.tourGuide.controller.TourGuideController;
import com.tourGuide.tourGuide.helper.InternalTestHelper;
import com.tourGuide.tourGuide.model.*;
import com.tourGuide.tourGuide.service.TourGuideService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@WebMvcTest(TourGuideController.class)
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TourGuideControllerTest {


    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private TourGuideService tourGuideService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
    }

    @AfterAll
    public void afterAll(){
        InternalTestHelper.setInternalUserNumber(0);
        user = null;
    }

    @Test
    public void indexTest() throws Exception {
        mockMvc.perform(get("/")).
                andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Greetings from TourGuide!")));
    }

    @Test
    public void getLocationTest() throws Exception {
        VisitedLocation visitedLocation = new VisitedLocation(user.getUserId(), new Location(43.582767, -110.821999), new Date());
        CopyOnWriteArrayList<VisitedLocation> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
        copyOnWriteArrayList.add(visitedLocation);

        user.setVisitedLocations(copyOnWriteArrayList);
        when(tourGuideService.getUserLocation(anyString())).thenReturn(visitedLocation);

        mockMvc.perform(get("/getLocation?userName=jon"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.location.longitude", is(-110.821999)))
                .andExpect(jsonPath("$.location.latitude", is(43.582767)))
                .andExpect(jsonPath("$.userId", is(String.valueOf(user.getUserId()))));

    }

    @Test
    public void getNearbyAttractionsTest() throws Exception {
        VisitedLocation visitedLocation = new VisitedLocation(user.getUserId(), new Location(43.582767, -110.821999), new Date());

        List<Attraction> attractions = new ArrayList();
        attractions.add(new Attraction("Disneyland", "Paris", "France", 48.871900D, 2.776623D));

        NearByAttraction nearByAttraction = new NearByAttraction(attractions.get(0).getAttractionName(), attractions.get(0).getLatitude(), attractions.get(0).getLongitude(), visitedLocation.getLocation().getLatitude(), visitedLocation.getLocation().getLongitude(), 250.0,2000);

        List<NearByAttraction> nearByAttractions = new ArrayList<>();
        nearByAttractions.add(nearByAttraction);

        when(tourGuideService.getUserLocation(anyString())).thenReturn(visitedLocation);
        when(tourGuideService.getNearByAttractions(any(VisitedLocation.class))).thenReturn(nearByAttractions);

        mockMvc.perform(get("/getNearbyAttractions?userName=jon"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].distance", is(250.0)))
                .andExpect(jsonPath("$.[0].userLatitude", is(43.582767)))
                .andExpect(jsonPath("$.[0].userLongitude", is(-110.821999)))
                .andExpect(jsonPath("$.[0].rewardPoints", is(2000)))
                .andExpect(jsonPath("$.[0].attractionName", is("Disneyland")));
    }

    @Test
    public void getRewardsTest() throws Exception {
        VisitedLocation visitedLocation = new VisitedLocation(user.getUserId(), new Location(43.582767, -110.821999), new Date());

        Attraction attraction = new Attraction("Disneyland", "Paris", "France", 48.871900D, 2.776623D);

        UserReward userReward = new UserReward(visitedLocation, attraction, 265);

        List<UserReward> rewardList = new ArrayList<>();
        rewardList.add(userReward);

        when(tourGuideService.getUserRewards(anyString())).thenReturn(rewardList);
        mockMvc.perform(get("/getRewards?userName=jon"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].visitedLocation.location.longitude", is(-110.821999)))
                .andExpect(jsonPath("$.[0].visitedLocation.location.latitude", is(43.582767)))
                .andExpect(jsonPath("$.[0].attraction.latitude", is(48.8719)))
                .andExpect(jsonPath("$.[0].attraction.longitude", is(2.776623)))
                .andExpect(jsonPath("$.[0].attraction.attractionName", is("Disneyland")))
                .andExpect(jsonPath("$.[0].attraction.attractionName", is("Disneyland")))
                .andExpect(jsonPath("$.[0].attraction.city", is("Paris")))
                .andExpect(jsonPath("$.[0].attraction.state", is("France")))
                .andExpect(jsonPath("$.[0].rewardPoints", is(265)))
                .andExpect(jsonPath("$.[0].visitedLocation.userId", is(String.valueOf(user.getUserId()))));
    }

    @Test
    public void getAllCurrentLocationsTest() throws Exception {
        AllUserCurrentLocation userLocations = new AllUserCurrentLocation(UUID.randomUUID(),new Location(10,20));
        List<AllUserCurrentLocation> locationsList = new ArrayList<>();
        locationsList.add(userLocations);

        when(tourGuideService.getAllUsersCurrentLocation()).thenReturn(locationsList);

        mockMvc.perform(get("/getAllCurrentLocations"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].location.latitude", is(10.0)))
                .andExpect(jsonPath("$.[0].location.longitude", is(20.0)))
                .andExpect(jsonPath("$.[0].userId", is(String.valueOf(locationsList.get(0).getUserId()))));
    }

    @Test
    public void getTripDealsTest() throws Exception {
        Provider provider1 = new Provider(UUID.randomUUID(),"New York Travels", 1430.0);
        Provider provider2 = new Provider(UUID.randomUUID(),"Boston Travels", 1580.0);

        List<Provider> providerList = new ArrayList<>();
        providerList.add(provider1);
        providerList.add(provider2);

        when(tourGuideService.getTripDeals(anyString(),anyInt(),anyInt(),anyInt(),anyInt())).thenReturn(providerList);

        mockMvc.perform(get("/getTripDeals?userName=jon&tripDuration=7&ticketQuantity=3&numberOfAdults=2&numberOfChildren=3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[1].name", is("Boston Travels")))
                .andExpect(jsonPath("$.[0].price", is(1430.0)));
    }
}
