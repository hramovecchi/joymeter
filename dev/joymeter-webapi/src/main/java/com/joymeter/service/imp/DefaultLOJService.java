package com.joymeter.service.imp;

import java.util.List;

import org.springframework.stereotype.Service;

import com.joymeter.entity.LevelOfJoy;
import com.joymeter.service.LevelOfJoyService;

@Service("levelOfJoyService")
public class DefaultLOJService extends LevelOfJoyService{
	
	@Override
	protected Double decreseLevel(Double prevLevel) {
		return prevLevel*0.99;
	}
	
	@Override
	protected double calculateAverage(Double actual, List<LevelOfJoy> activities) {

		Double activitiesAvr = Double.valueOf(0);
		if (!activities.isEmpty()) {
			for (LevelOfJoy mark : activities) {
				activitiesAvr += mark.getLevel();
			}
			return (activitiesAvr / activities.size()) + actual;
		}
		return actual;
	}

}
