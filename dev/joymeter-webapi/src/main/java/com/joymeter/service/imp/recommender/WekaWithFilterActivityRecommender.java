package com.joymeter.service.imp.recommender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Iterables;
import com.joymeter.entity.Activity;
import com.joymeter.entity.Advice;
import com.joymeter.entity.User;
import com.joymeter.repository.AdviceRepository;
import com.joymeter.service.ActivityService;
import com.joymeter.service.DefaultAdviceActivityService;
import com.joymeter.service.imp.recommender.base.ActivityTypePredicate;
import com.joymeter.service.imp.recommender.base.ActivityDayTypePredicate;
import com.joymeter.service.imp.recommender.base.ActivityMomentOfDayPredicate;
import com.joymeter.service.imp.recommender.base.ActivityType;
import com.joymeter.service.imp.recommender.base.ActivityTypeProbability;
import com.joymeter.service.imp.recommender.base.DayType;
import com.joymeter.service.imp.recommender.base.LevelOfSatisfaction;
import com.joymeter.service.imp.recommender.base.MomentOfDay;
import com.joymeter.service.imp.recommender.base.WekaBaseRecommender;
import com.joymeter.service.recommender.ActivityRecommender;

import weka.core.Instance;

@Service("wekaWithFilterActivityRecommender")
public class WekaWithFilterActivityRecommender implements ActivityRecommender {

	private final Double abcThreshold = 0.7;

	@Autowired
	ActivityService activityService;

	@Autowired
	WekaBaseRecommender wekaBaseRecommender;

	@Autowired
	AdviceRepository adviceRepository;

	@Autowired
	DefaultAdviceActivityService defaultAdviceActService;

	public Advice suggestActivity(User user, long instant) throws Exception {
		DateTime now = new DateTime(instant);
		MomentOfDay mod = MomentOfDay.valueOf(now.getMillis());
		DayType dt = DayType.valueOf(now.getMillis());

		LevelOfSatisfaction[] lvlOfSatisfactionTarget = getLevelOfSatisfactionTarget(user);
		List<ActivityTypeProbability> sortedActivityTypeDistribution = 
				getActivitytypeDistribution(user, mod, dt, lvlOfSatisfactionTarget);

		//BEGIN ABC
		List<ActivityTypeProbability> listFiltered = abcFilterList(sortedActivityTypeDistribution);
		//END ABC

		//BEGIN Feedback FILTER
		ActivityType activityType = filterByFeedback(user, listFiltered);
		//END Feedback FILTER
		List<Activity> activityList = activityService.getActivities(user).getActivities();
		List<Activity> activityListFiltered = filterByCriteria(activityList, activityType, dt, mod);

		Random r = new Random();
		int index = r.nextInt(activityListFiltered.size());

		Advice advice = new Advice();
		advice.setSuggestedActivity(activityListFiltered.get(index));
		return advice;
	}

	private List<ActivityTypeProbability> abcFilterList(List<ActivityTypeProbability> sortedActivityTypeDistribution) {
		List<ActivityTypeProbability> filteredList = new ArrayList<ActivityTypeProbability>();
		Double probSum = 0.0;
		int index = 0;
		while (probSum < abcThreshold){
			filteredList.add(sortedActivityTypeDistribution.get(index));
			probSum += sortedActivityTypeDistribution.get(index).getProbability();
			index ++;
		}

		return filteredList;
	}

	private ActivityType filterByFeedback(User user, List<ActivityTypeProbability> sortedActivityTypeDistribution) {
		Map<ActivityType, Integer> suggestedMap = new HashMap<ActivityType, Integer>();
		Map<ActivityType, Integer> acceptedMap = new HashMap<ActivityType, Integer>();

		for (ActivityTypeProbability at: sortedActivityTypeDistribution){
			List<Advice> ala = adviceRepository.getAcceptedAdvicesByType(user.getId(), at.getType().name());
			List<Advice> al = adviceRepository.getAdvicesByType(user.getId(), at.getType().name());
			suggestedMap.put(at.getType(), al.size());
			acceptedMap.put(at.getType(), ala.size());
		}

		double epsilon = 0;
		Map<ActivityType, Double> probDistribMap = new HashMap<ActivityType, Double>();
		for (ActivityTypeProbability atp: sortedActivityTypeDistribution){
			double suggestedByType = suggestedMap.get(atp.getType()) != 0 ? (double)suggestedMap.get(atp.getType()): (double)1;
			double distribByTypeProb = acceptedMap.get(atp.getType())/suggestedByType;

			distribByTypeProb = (distribByTypeProb * atp.getProbability()) + atp.getProbability();
			epsilon += distribByTypeProb;

			probDistribMap.put(atp.getType(), distribByTypeProb);
		}

		for (ActivityType at : probDistribMap.keySet()){
			double value = probDistribMap.get(at) / epsilon;
			probDistribMap.put(at, value);
		}

		double typeRandom = Math.random();
		
		double acumulativeValue = 0;
		for (ActivityType at: probDistribMap.keySet()){
			acumulativeValue += probDistribMap.get(at);
			if (acumulativeValue >= typeRandom){
				return at;
			}			
		}

		return null;
	}

	/**Returns the sorted Activity distribution for a given user, a levelOfSatisfaction to target
	 * 
	 * @param user, the User to obtain the Activity distribution.
	 * @param mod, the MomentOfDay to obtain the Activity distribution.
	 * @param dt, the DayType to obtain the Activity distribution.
	 * @param lvlost, the LevelOfSatisfaction[] to obtain the Activity distribution.
	 * @return
	 * @throws Exception
	 */
	private List<ActivityTypeProbability> getActivitytypeDistribution(User user, MomentOfDay mod, DayType dt, LevelOfSatisfaction[] lvlost) throws Exception {
		Map<ActivityType, Double> distributionMap = new HashMap<ActivityType, Double>();

		for (LevelOfSatisfaction los : lvlost) {
			Instance instance = wekaBaseRecommender.createInstance(dt.name(), mod.name(), los.name(), null,
					wekaBaseRecommender.getUserInstances(user));

			List<ActivityTypeProbability> probDistribution = wekaBaseRecommender.getActivityTypeDistribution(user, instance);
			for (ActivityTypeProbability typeAndProb: probDistribution) {
				if (distributionMap.containsKey(typeAndProb.getType())) {
					Double value = distributionMap.get(typeAndProb.getType());
					value += typeAndProb.getProbability();
					distributionMap.put(typeAndProb.getType(), value);
				} else {
					distributionMap.put(typeAndProb.getType(), typeAndProb.getProbability());
				}
			}
		};

		List<ActivityTypeProbability> distributionList = new ArrayList<ActivityTypeProbability>();
		for (ActivityType type :distributionMap.keySet()) {
			Double value = distributionMap.get(type);
			value = value / getLevelOfSatisfactionTarget(user).length;
			distributionMap.put(type, value);
			distributionList.add(new ActivityTypeProbability(type, value));
		}

		Collections.sort(distributionList, new Comparator<ActivityTypeProbability>() {

			public int compare(ActivityTypeProbability o1, ActivityTypeProbability o2) {
				if (o1.getProbability() < o2.getProbability()){
					return 1;
				} else if (o1.getProbability() > o2.getProbability()) {
					return -1;
				}
				return 0;
			}
		});
		return distributionList;
	}

	/**
	 * returns a Satisfaction Target for a given user
	 * @param user
	 * @return LevelOfSatisfaction[] of the satisfaction target
	 */
	private LevelOfSatisfaction[] getLevelOfSatisfactionTarget(User user) {
		return LevelOfSatisfaction.values();
	}

	private List<Activity> filterByCriteria(List<Activity> activityList, ActivityType activityType,
			DayType dt, MomentOfDay mod) {

		Iterables.removeIf(activityList, new ActivityTypePredicate(activityType.getType()));

		//In case there is no activity for the selected type, return a preset one
		if (activityList.isEmpty()){
			activityList = defaultAdviceActService.getActivities();
			Iterables.removeIf(activityList, new ActivityTypePredicate(activityType.getType()));
		} else {
			//doing a backup
			List<Activity> backupList = new ArrayList<Activity>(activityList.size());
			Collections.copy(backupList, activityList);
			//lets try filter by momentOfDay
			Iterables.removeIf(activityList, new ActivityMomentOfDayPredicate(mod));

			if (!activityList.isEmpty()){
				List<Activity> backupList2 = new ArrayList<Activity>(activityList.size());
				//lets try filter by dayType
				Iterables.removeIf(activityList, new ActivityDayTypePredicate(dt));
				if (activityList.isEmpty()){
					return backupList2;
				}
			} else {
				return backupList;
			}
		}
		return activityList;
	}
}
