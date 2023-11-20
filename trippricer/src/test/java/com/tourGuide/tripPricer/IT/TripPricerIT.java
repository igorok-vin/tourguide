package com.tourGuide.tripPricer.IT;

import com.tourGuide.tripPricer.model.Provider;
import com.tourGuide.tripPricer.service.TripPricerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TripPricerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TripPricerService tripPricerService;

    @Autowired
    WebApplicationContext context;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    public void getTripDealsTest() throws Exception {
        String api = "stringApi";
        UUID attractionId = UUID.randomUUID();
        int adults = 2;
        int children = 2;
        int nightsStay = 7;
        int rewardsPoints = 200;

        List<Provider> result = tripPricerService.getPrice(api, attractionId,adults,children,nightsStay,rewardsPoints);

        mockMvc.perform(get("/getTripDeals")
                        .param("apiKey", api)
                        .param("attractionId", String.valueOf(attractionId))
                        .param("adults", String.valueOf(adults))
                        .param("children", String.valueOf(children))
                        .param("nightsStay", String.valueOf(nightsStay))
                        .param("rewardsPoints", String.valueOf(rewardsPoints)))
                .andDo(print())
                .andExpect(status().isOk());
        assertTrue(result.size()>0);
    }
}
