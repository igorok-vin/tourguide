package com.tourGuide.tourGuide;

import com.tourGuide.tourGuide.clients.GpsUtilProxy;
import com.tourGuide.tourGuide.clients.RewardCentralProxy;
import com.tourGuide.tourGuide.service.RewardsService;
//import gpsUtil.GpsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class TourGuideModule {
	
	@Autowired
	GpsUtilProxy getGpsUtil;

	@Autowired
	RewardCentralProxy rewardCentralProxy;
	
	@Bean
	public RewardsService getRewardsService() {
		return new RewardsService(getGpsUtil, rewardCentralProxy);
	}

}
