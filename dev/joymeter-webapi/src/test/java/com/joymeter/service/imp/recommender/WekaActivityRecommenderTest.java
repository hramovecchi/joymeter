package com.joymeter.service.imp.recommender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;

public class WekaActivityRecommenderTest {

	private Instances instances; 

	@Before
	public void prepareObjects(){
		BufferedReader reader = null;
		File file = null;
		try {
			file = new File("1data.arff");
			reader = new BufferedReader(new FileReader(file));
			instances = new Instances(reader);
			// Mark the last attribute in each instance as the true class.
			instances.setClassIndex(instances.numAttributes()-1);
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testClassifier() throws Exception{
		// Deserialize the classifier.
	    Classifier classifier = new NaiveBayes();
	    classifier.buildClassifier(instances);

	    int numTestInstances = instances.numInstances();
	    System.out.printf("There are %d test instances\n", numTestInstances);

	    // Loop over each test instance.
	    for (int i = 0; i < numTestInstances; i++)
	    {
	        // Get the true class label from the instance's own classIndex.
	        String trueClassLabel = 
	        		instances.instance(i).toString(instances.classIndex());

	        // Make the prediction here.
	        double predictionIndex = 
	            classifier.classifyInstance(instances.instance(i)); 

	        // Get the predicted class label from the predictionIndex.
	        String predictedClassLabel =
	        		instances.classAttribute().value((int) predictionIndex);

	        // Get the prediction probability distribution.
	        double[] predictionDistribution = 
	            classifier.distributionForInstance(instances.instance(i));
	        
	        // Print out the true label, predicted label, and the distribution.
	        System.out.printf("%5d: true=%-10s, predicted=%-10s, distribution=", 
	                          i, trueClassLabel, predictedClassLabel); 

	        // Loop over all the prediction labels in the distribution.
	        for (int predictionDistributionIndex = 0; 
	             predictionDistributionIndex < predictionDistribution.length; 
	             predictionDistributionIndex++)
	        {
	            // Get this distribution index's class label.
	            String predictionDistributionIndexAsClassLabel = 
	            		instances.classAttribute().value(
	                    predictionDistributionIndex);

	            // Get the probability.
	            double predictionProbability = 
	                predictionDistribution[predictionDistributionIndex];

	            System.out.printf("[%10s : %6.3f]", 
	                              predictionDistributionIndexAsClassLabel, 
	                              predictionProbability );
	        }

	        System.out.printf("\n");
	    }
	}
}
