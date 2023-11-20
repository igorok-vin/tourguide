package com.tourGuide.tourGuide.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "${rewards_central-service-name}", url = "${rewards_central-service-url}")
public interface RewardCentralProxy {

    @GetMapping("/getRewards")
    int getAttractionRewardPoints (@RequestParam UUID attractionId, @RequestParam UUID userId);
}
