package com.joymeter.service.imp.recommender.base;

import java.util.ArrayList;
import java.util.List;

public enum ActivityType {    
    Deporte("Deporte"),
    Profesional("Profesional"),
    Estudio("Estudio"),
    Ocio("Ocio");

	public static List<String> getValues(){
		List<String> list = new ArrayList<String>();
		for (ActivityType at: values()){
			list.add(at.name());
		}

		return list;
	}

	public static List<String> getTypeValues(){
		List<String> list = new ArrayList<String>();
		for (ActivityType at: values()){
			list.add(at.getType());
		}

		return list;
	}

	public static ActivityType valueOfType(String type){
		for (ActivityType at: values()){
			if (at.getType().equals(type)){
				return at;
			}
		}
		return null;
	}

    private String type;

    ActivityType(String type){
		this.type = type;
	}

    public String getType(){
    	return this.type;
    }

    @Override
    public String toString() {
    	return type;
    }
}
