package com.joymeter.service.imp.recommender.base;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.joymeter.entity.Activity;
import com.joymeter.entity.User;
import com.joymeter.service.ActivityService;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

@Service("wekaBaseRecommender")
public class WekaBaseRecommender {

	private Logger log = Logger.getLogger(this.getClass());
	private static final String FILENAME = "data.arff";
	private static final int MONTHSTOEXPIRE = 1;

	public static final Attribute dayTypeAttr = new Attribute("DAYTYPE", DayType.stringValues());
	public static final Attribute momentOfDayAttr = new Attribute("MOMENTOFDAY", MomentOfDay.stringValues());
	public static final Attribute levelOfSatisfactionAttr = new Attribute("LEVELOFSATISFACTION", LevelOfSatisfaction.getValues());
	public static final Attribute activityTypeAttr = new Attribute("TYPEOFACTIVITY", ActivityType.getValues());

	public static final ArrayList<Attribute> attributeList = new ArrayList<Attribute>() {
	
		private static final long serialVersionUID = 1L;

		{
	        add(dayTypeAttr);
	        add(momentOfDayAttr);
	        add(levelOfSatisfactionAttr);
	        //Must be the last in the list to refer it as size -1
	        add(activityTypeAttr);
	    }
	};

	@Autowired
	private ActivityService activityService;

	public ActivityType suggestActivity(User user, Instance instance) throws Exception {
		Instances trainingSet = getUserInstances(user);		

		Classifier classifier = new NaiveBayes();
		FilteredClassifier fc = new FilteredClassifier();
		fc.setClassifier(classifier);
		fc.buildClassifier(trainingSet);

		//Make the prediction here.
		double predictionIndex = -1;
		try {
			predictionIndex = fc.classifyInstance(instance);
		} catch (Exception e) {
			
		}

//		Get the predicted class label from the predictionIndex.
		String predictedType =
			trainingSet.classAttribute().value((int) predictionIndex);

		log.info("Predicted activity: "+predictedType);

		return ActivityType.valueOf(predictedType);
	}

	public List<ActivityTypeProbability> getActivityTypeDistribution(User user, Instance instance) throws Exception {
		Instances trainingSet = getUserInstances(user);		

		Classifier classifier = new NaiveBayes();
		FilteredClassifier fc = new FilteredClassifier();
		fc.setClassifier(classifier);
		fc.buildClassifier(trainingSet);
		
		// Get the prediction probability distribution.
		double[] predictionDistribution = classifier.distributionForInstance(instance);
		
		List<ActivityTypeProbability> list = new ArrayList<ActivityTypeProbability>();

		// Loop over all the prediction labels in the distribution.
		for (int predictionDistributionIndex = 0;
				predictionDistributionIndex < predictionDistribution.length; predictionDistributionIndex++) {

			// Get this distribution index's class label.
			String predictionDistributionIndexAsClassLabel = trainingSet.classAttribute()
					.value(predictionDistributionIndex);

			// Get the probability.
			double predictionProbability = predictionDistribution[predictionDistributionIndex];
			
			list.add(new ActivityTypeProbability(
					ActivityType.valueOf(predictionDistributionIndexAsClassLabel), predictionProbability));

		}

		return list;
	}

	public Instance createInstance(String dayType, String momentOfDay, String levelOfJoy, String activityType, Instances trainingSet) {
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

	public Instances getUserInstances(User user) {
		Instances trainingSet = getInstances(user.getId());
		if (trainingSet == null){
			trainingSet = createTrainingSet(activityService.getActivities(user).getActivities());
			try {
				saveToFile(trainingSet, user.getId());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		trainingSet.setClassIndex(trainingSet.numAttributes() - 1);
		return trainingSet;
	}

	private Instances getInstances(long userId) {
		Instances data = null;
		BufferedReader reader = null;
		File file = null;
		try {
			file = new File(userId + FILENAME);
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e2) {
			return null;
		}

		DateTime fileExpirationDate = new DateTime(file.lastModified()).plusMonths(MONTHSTOEXPIRE);
		@SuppressWarnings("static-access")
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

	private void saveToFile(Instances data, long userId) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(userId + FILENAME));
		writer.write(data.toString());
		writer.newLine();
		writer.flush();
		writer.close();
	}

	private Instances createTrainingSet(List<Activity> activities) {
		//Create an empty training set
		Instances trainSet = new Instances("DataSet", attributeList, attributeList.size());

		for (Activity activity: activities){
			DayType dayType = DayType.valueOf(activity.getStartDate());
			MomentOfDay momentOfDay = MomentOfDay.valueOf(activity.getStartDate());
			ActivityType activityType = ActivityType.valueOfType(activity.getType());
			LevelOfSatisfaction levelOfSatisfaction = LevelOfSatisfaction.valueOf(activity.getLevelOfJoy());

			// Create the instance
			Instance instance = createInstance(dayType.name(), momentOfDay.name(), levelOfSatisfaction.name(), activityType.name(), trainSet);

			trainSet.add(instance);
		}
		return trainSet;
	}
}
