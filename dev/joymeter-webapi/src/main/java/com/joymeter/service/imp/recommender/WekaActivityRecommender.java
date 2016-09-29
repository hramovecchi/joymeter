package com.joymeter.service.imp.recommender;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.joymeter.entity.Activity;
import com.joymeter.entity.ActivityType;
import com.joymeter.entity.Advice;
import com.joymeter.entity.User;
import com.joymeter.service.ActivityService;
import com.joymeter.service.recommender.ActivityRecommender;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

@Service("wekaActivityRecommender")
public class WekaActivityRecommender implements ActivityRecommender{

	private static final String FILENAME = "data.arff";
	private static final int MONTHSTOEXPIRE = 1;

	private static final Attribute dayTypeAttr = new Attribute("DAYTYPE", DayType.stringValues());
	private static final Attribute momentOfDayAttr = new Attribute("MOMENTOFDAY", MomentOfDay.stringValues());
	private static final Attribute levelOfJoyAttr = new Attribute("LEVELOFJOY", LOJ.getValues());
	private static final Attribute activityTypeAttr = new Attribute("TYPEOFACTIVITY", ActivityType.getValues());

	private static final ArrayList<Attribute> attributeList = new ArrayList<Attribute>() {
	
		private static final long serialVersionUID = 1L;

		{
	        add(dayTypeAttr);
	        add(momentOfDayAttr);
	        add(levelOfJoyAttr);
	        //Must be the last in the list to refer it as size -1
	        add(activityTypeAttr);
	    }
	};

	@Autowired
	ActivityService activityService;

	public Advice suggestActivity(User user) {
		Instances trainingSet = getUserInstances(user);		
		trainingSet.setClassIndex(trainingSet.numAttributes() - 1);

		Classifier classifier = new NaiveBayes();
		
		// filter
//		 Remove rm = new Remove();
//		 rm.setAttributeIndices("4");  // remove 1st attribute
		 
		// meta-classifier
		 FilteredClassifier fc = new FilteredClassifier();
//		 fc.setFilter(rm);
		 fc.setClassifier(classifier);
		 
		 try {
			fc.buildClassifier(trainingSet);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		DateTime now = DateTime.now();
		MomentOfDay mod = MomentOfDay.valueOf(now.getMillis());
		DayType dt = DayType.valueOf(now.getMillis());
		

		for (LOJ loj: LOJ.values()){
			loj.name();
			Instance instance = createInstance(dt.name(), mod.name(), loj.name(), null, trainingSet);

			// Make the prediction here.
			double predictionIndex = -1;
			try {
				predictionIndex = fc.classifyInstance(instance);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 

	        // Get the predicted class label from the predictionIndex.
	        String predictedClassLabel =
	        		trainingSet.classAttribute().value((int) predictionIndex);
	        
	        System.out.println("Predicted activity "+predictedClassLabel);
		}

		return null;
	}

	private Instances getUserInstances(User user) {
		Instances trainingSet = getInstances(user);
		if (trainingSet == null){
			trainingSet = createTrainingSet(user);
			try {
				saveToFile(trainingSet, user);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return trainingSet;
	}
	
	private void saveToFile(Instances data, User user) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(+user.getId()+FILENAME));
		writer.write(data.toString());
		writer.newLine();
		writer.flush();
		writer.close();
	}

	private Instances createTrainingSet(User user) {
		//Create an empty training set
		Instances trainSet = new Instances("DataSet", attributeList, attributeList.size());
 
		List<Activity> activities = new ArrayList<Activity>();
		activities.addAll(activityService.getActivities(user).getActivities());

		for (Activity activity: activities){
			DayType dayType = DayType.valueOf(activity.getStartDate());
			MomentOfDay momentOfDay = MomentOfDay.valueOf(activity.getStartDate());
			ActivityType activityType = ActivityType.valueOfType(activity.getType());
			LOJ levelOfJoy = LOJ.valueOf(activity.getLevelOfJoy());

			// Create the instance
			Instance instance = createInstance(dayType.name(), momentOfDay.name(), levelOfJoy.name(), activityType.name(), trainSet);

			trainSet.add(instance);
		}
		return trainSet;
	}

	private Instance createInstance(String dayType, String momentOfDay, String levelOfJoy, String activityType, Instances trainingSet) {
		Instance instance = new DenseInstance(attributeList.size());
		if (trainingSet != null){
			instance.setDataset(trainingSet);
		}

		if (!StringUtils.isEmpty(dayType)){
			instance.setValue(trainingSet.attribute(0), dayType);
		}

		if (!StringUtils.isEmpty(momentOfDay)){
			instance.setValue(trainingSet.attribute(1), momentOfDay);
		}

		if (!StringUtils.isEmpty(levelOfJoy)){
			instance.setValue(trainingSet.attribute(2), levelOfJoy);
		}

		if (!StringUtils.isEmpty(activityType)){
			instance.setValue(trainingSet.attribute(3), activityType);
		}
		return instance;
	}

	private Instances getInstances(User user) {
		Instances data = null;
		BufferedReader reader = null;
		File file = null;
		try {
			file = new File(user.getId()+FILENAME);
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e2) {
			return null;
		}

		DateTime fileExpirationDate = new DateTime(file.lastModified()).plusMonths(MONTHSTOEXPIRE);
		DateTime currentDate = new DateTime().now();
		if (fileExpirationDate.getMillis() > currentDate.getMillis()){
			try {
				data = new Instances(reader);
			} catch (IOException e1) {
				return null;
			}
		}
		return data;
	}

	private enum DayType{
		WEEKEND,
		WORKINGDAY;
		
		static List<String> stringValues() {
			List<String> list = new ArrayList<String>();
			for (DayType value: values()){
				list.add(value.name());
			}
			return list;
		}
		
		static DayType valueOf(long timeInMillis){
			DateTime date = new DateTime(timeInMillis);
		    int dow = date.getDayOfWeek();
		    
			switch (dow) {
			case Calendar.SATURDAY:
				return DayType.WEEKEND;
			case Calendar.SUNDAY:
				return DayType.WEEKEND;
			default:
				return DayType.WORKINGDAY;
			}
		    
		}
	}
	
	private enum MomentOfDay{
		MORNING(6,14),
		EVENING(14,22),
		NIGHT(22,6);
		
		int min;
		int max;
		
		static List<String> stringValues() {
			List<String> list = new ArrayList<String>();
			for (MomentOfDay value: values()){
				list.add(value.name());
			}
			return list;
		}
		
		MomentOfDay(int min, int max) {
			this.min = min;
			this.max = max;
		};

		static MomentOfDay valueOf(long timeInMillis) {
			DateTime date = new DateTime(timeInMillis);
			
			int hod = date.getHourOfDay();
				
			if (hod >= MomentOfDay.MORNING.min && hod < MomentOfDay.MORNING.max){
				return MomentOfDay.MORNING;
			} else if (hod >= MomentOfDay.EVENING.min && hod < MomentOfDay.EVENING.max) {
				return MomentOfDay.EVENING;
			}
			
			return MomentOfDay.NIGHT;
		}
	}
	
	private enum LOJ{
		ONE(1),
		TWO(2),
		THREE(3),
		FOUR(4),
		FIVE(5);
		
		int value;
		
		LOJ(int value){
			this.value = value;
		}
		
		public int getValue(){
			return value;
		}
		
		static List<String> getValues(){
			List<String> list = new ArrayList<String>();
			for (LOJ value: values()){
				list.add(value.name());
			}
			return list;
		}
		
		static LOJ valueOf(int i){
			for (LOJ value: values()){
				if (value.getValue() == i){
					return value;
				}
			}
			return null;
		}
	}
}
