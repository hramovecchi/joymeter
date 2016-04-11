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
		int size = (actual == 0) ? activities.size() : activities.size() + 1;
		if (!activities.isEmpty()) {
			for (LevelOfJoy mark : activities) {
				actual += mark.getLevel();
			}
			return actual.doubleValue() / size;
		}
		return actual;
	}

}
