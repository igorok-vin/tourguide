package com.tourGuide.rewardCentral.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


public class RewardsCentralServiceTest {

    RewardsCentralService rewardsCentralService;

    @BeforeEach
    void setup() {
        rewardsCentralService = new RewardsCentralService();
    }

    @Test
    public void getAttractionRewardPointsTest() {
        UUID attractionId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        int result = rewardsCentralService.getAttractionRewardPoints(attractionId,userId);
        System.out.println(result);
        assertThat(result).isGreaterThan(0);
    }
}
