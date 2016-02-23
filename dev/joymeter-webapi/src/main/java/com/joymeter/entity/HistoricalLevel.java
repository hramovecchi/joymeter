package com.joymeter.entity;

import java.util.List;

/**
 * Simple POJO that represents the level of joy of a given user.
 * 
 */
public class HistoricalLevel {

	private List<LevelOfJoy> history;
	
	public HistoricalLevel() {}
	
	public HistoricalLevel(List<LevelOfJoy> history) {
		super();
		this.history = history;
	}

	public List<LevelOfJoy> getHistory() {
		return history;
	}
}
