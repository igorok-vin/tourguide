package com.tourGuide.rewardCentral.IT;

import com.tourGuide.rewardCentral.service.RewardsCentralService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RewardsCentralIT {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    RewardsCentralService rewardCentralService;

    @Autowired
    WebApplicationContext context;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    public void indexTest() throws Exception {
        mockMvc.perform(get("/")).
                andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Greetings from RewardCentral!")));
    }

    @Test
    public void getUserLocationTest() throws Exception {
        UUID attractionId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        int result = rewardCentralService.getAttractionRewardPoints(attractionId,userId);

        mockMvc.perform(get("/getRewards").param("attractionId", String.valueOf(attractionId)).param("userId", String.valueOf(userId)))
                .andDo(print())
                .andExpect(status().isOk());
        assertTrue(result>=0);
    }
}
