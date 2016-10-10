package com.joymeter.service.imp.recommender.base;

import java.util.ArrayList;
import java.util.List;

public enum LevelOfSatisfaction {
	ONE(1),
	TWO(2),
	THREE(3),
	FOUR(4),
	FIVE(5);
	
	int value;
	
	LevelOfSatisfaction(int value){
		this.value = value;
	}
	
	public int getValue(){
		return value;
	}
	
	public static List<String> getValues(){
		List<String> list = new ArrayList<String>();
		for (LevelOfSatisfaction value: values()){
			list.add(value.name());
		}
		return list;
	}
	
	public static LevelOfSatisfaction valueOf(int i){
		for (LevelOfSatisfaction value: values()){
			if (value.getValue() == i){
				return value;
			}
		}
		return null;
	}
}
