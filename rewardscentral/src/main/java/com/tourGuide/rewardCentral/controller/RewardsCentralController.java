package com.tourGuide.rewardCentral.controller;

import com.tourGuide.rewardCentral.service.RewardsCentralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class RewardsCentralController {
    @Autowired
    RewardsCentralService rewardCentralService;

    @RequestMapping("/")
    public String index() {
        return "Greetings from RewardCentral!";
    }

    @RequestMapping("/getRewards")
    public int getAttractionRewardPoints(@RequestParam UUID attractionId, @RequestParam UUID userId) {
        return rewardCentralService.getAttractionRewardPoints(attractionId, userId);
    }
}
