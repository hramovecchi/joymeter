package com.joymeter.service.imp.recommender.base;

public class ActivityTypeProbability {

	private ActivityType type;
	private double probability;
	
	public ActivityTypeProbability(ActivityType type, double probability){
		this.setType(type);
		this.setProbability(probability);
	}

	public ActivityType getType() {
		return type;
	}

	public void setType(ActivityType type) {
		this.type = type;
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}
	
	@Override
	public String toString() {
		return "{type: "+type.name() +", probability:" + probability+"}";
	}
}
