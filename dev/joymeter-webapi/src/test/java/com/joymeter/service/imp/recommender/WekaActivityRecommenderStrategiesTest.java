package com.joymeter.service.imp.recommender;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.joymeter.entity.Activity;
import com.joymeter.entity.Advice;
import com.joymeter.repository.AdviceRepository;
import com.joymeter.service.ActivityService;
import com.joymeter.service.imp.recommender.base.ActivityType;
import com.joymeter.service.imp.recommender.base.WekaBaseRecommender;

import weka.core.Instance;
import weka.core.Instances;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class WekaActivityRecommenderStrategiesTest {
	
	@Autowired WekaWithFilterActivityRecommender filterRecommender;
	@Autowired WekaActivityRecommender simpleRecommender;
	@Autowired AdviceRepository adviceRepository;
	@Autowired WekaBaseRecommender wekaBaseRecommender;
	@Autowired ActivityService activityService;
	
	Long userId = 6L;

	@Test
	public void testClassifier() throws Exception{

		List<Activity> activities = activityService.getActivities(userId.intValue()).getActivities();
		Collections.shuffle(activities);
		
		Integer pibot = Double.valueOf(activities.size()*0.7).intValue();
		List<Activity> trainingList = activities.subList(0, pibot);
		Instances trainingSet = wekaBaseRecommender.createTrainingSet(trainingList);
		trainingSet.setClassIndex(trainingSet.numAttributes() - 1);
		
		List<Activity> testingList = activities.subList(pibot, activities.size());
		Instances dataSet = wekaBaseRecommender.createTrainingSet(testingList);
		dataSet.setClassIndex(dataSet.numAttributes() - 1);
		
		Map<ActivityType, Integer> suggestedMap = new HashMap<ActivityType, Integer>();
		Map<ActivityType, Integer> acceptedMap = new HashMap<ActivityType, Integer>();
		populateSuggestionMaps(suggestedMap, acceptedMap);
		
		Map<String, Map<String, Integer>> filterConfusionMatrix = new HashMap<String, Map<String, Integer>>();
		Map<String, Map<String, Integer>> simpleConfusionMatrix = new HashMap<String, Map<String, Integer>>();
		initConfusionMatrixs(filterConfusionMatrix, simpleConfusionMatrix);
		
		Integer aciertosFilter = 0;
		Integer aciertosSimple = 0;
		 
		for(Instance instance : dataSet){
			String dayType = instance.stringValue(WekaBaseRecommender.dayTypeAttr);
			String momentOfDay = instance.stringValue(WekaBaseRecommender.momentOfDayAttr);
			String instanceType = instance.stringValue(WekaBaseRecommender.activityTypeAttr);
			String filterType = filterRecommender.suggestTesteableActivity(dayType, momentOfDay, trainingSet, suggestedMap, acceptedMap);
			String simpleType = simpleRecommender.suggestTesteableActivity(dayType, momentOfDay, trainingSet);
			
			//System.out.println("FILTER - Predicted: " + filterType + "and was: " + instanceType);
			//System.out.println("SIMPLE - Predicted: " + simpleType + "and was: " + instanceType);
			
			if(filterType.equalsIgnoreCase(instanceType)){
				aciertosFilter++;				
			}
			Integer clasifiedFilter = filterConfusionMatrix.get(instanceType).get(filterType);
			filterConfusionMatrix.get(instanceType).put(filterType, clasifiedFilter + 1);
			
			if(simpleType.equalsIgnoreCase(instanceType)){
				aciertosSimple++;				
			}
			Integer clasifiedSimple = simpleConfusionMatrix.get(instanceType).get(simpleType);
			simpleConfusionMatrix.get(instanceType).put(simpleType, clasifiedSimple + 1);
		}
		printResults(filterConfusionMatrix, simpleConfusionMatrix, aciertosFilter, aciertosSimple, dataSet.size());
		
	}

	private void printResults(Map<String, Map<String, Integer>> filterConfusionMatrix,
			Map<String, Map<String, Integer>> simpleConfusionMatrix, Integer aciertosFilter, Integer aciertosSimple, Integer totalTests) {
		
		System.out.println("Aciertos FILTER: " + aciertosFilter + "/" + totalTests);
		System.out.println("Aciertos SIMPLE: " + aciertosSimple + "/" + totalTests);
		
		StringBuffer headerBuffer = new StringBuffer("Types: ");
		StringBuffer header2Buffer = new StringBuffer("    | ");
		int index = 1;
		for(String type : ActivityType.getTypeValues()){
			headerBuffer.append(index + "-" + type + " | ");
			header2Buffer.append(index + " | ");
			index++;
		}
		System.out.println("FilterConfusionMatrix");
		System.out.println(headerBuffer.toString());
		System.out.println(header2Buffer.toString());
		System.out.println("-----------------------");
		
		index = 1;
		for(String type : ActivityType.getTypeValues()){
			StringBuffer row = new StringBuffer(index + "-> | ");
			for(String type2 : ActivityType.getTypeValues()){
				row.append(filterConfusionMatrix.get(type2).get(type) + " | ");
			}
			System.out.println(row.toString());
			index++;
		}
		System.out.println();
		System.out.println("SimpleConfusionMatrix");
		System.out.println(headerBuffer.toString());
		System.out.println(header2Buffer.toString());
		System.out.println("-----------------------");
		
		index = 1;
		for(String type : ActivityType.getTypeValues()){
			StringBuffer row = new StringBuffer(index + "-> | ");
			for(String type2 : ActivityType.getTypeValues()){
				row.append(simpleConfusionMatrix.get(type2).get(type) + " | ");
			}
			System.out.println(row.toString());
			index++;
		}
		
	}

	private void initConfusionMatrixs(Map<String, Map<String, Integer>> filterConfusionMatrix,
			Map<String, Map<String, Integer>> simpleConfusionMatrix) {
		for(String type : ActivityType.getTypeValues()){
			filterConfusionMatrix.put(type, new HashMap<String, Integer>());
			simpleConfusionMatrix.put(type, new HashMap<String, Integer>());
		}
		
		for(String type : ActivityType.getTypeValues()){
			Map<String, Integer> filterHash = filterConfusionMatrix.get(type);
			Map<String, Integer> simpleHash = simpleConfusionMatrix.get(type);
			for(String type2 : ActivityType.getTypeValues()){
				filterHash.put(type2, 0);
				simpleHash.put(type2, 0);
			}
		}
		
	}
	
	private void populateSuggestionMaps(Map<ActivityType, Integer> suggestedMap, Map<ActivityType, Integer> acceptedMap){

		for (ActivityType at: ActivityType.values()){
			List<Advice> ala = adviceRepository.getAcceptedAdvicesByType(userId, at.name());
			List<Advice> al = adviceRepository.getAdvicesByType(userId, at.name());
			suggestedMap.put(at, al.size());
			acceptedMap.put(at, ala.size());
		}
	}
}
