package com.joymeter.service.imp.recommender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Iterables;
import com.joymeter.entity.Activity;
import com.joymeter.entity.Advice;
import com.joymeter.entity.User;
import com.joymeter.service.ActivityService;
import com.joymeter.service.DefaultAdviceActivityService;
import com.joymeter.service.imp.recommender.base.ActivityDayTypePredicate;
import com.joymeter.service.imp.recommender.base.ActivityMomentOfDayPredicate;
import com.joymeter.service.imp.recommender.base.ActivityType;
import com.joymeter.service.imp.recommender.base.ActivityTypePredicate;
import com.joymeter.service.imp.recommender.base.DayType;
import com.joymeter.service.imp.recommender.base.LevelOfSatisfaction;
import com.joymeter.service.imp.recommender.base.MomentOfDay;
import com.joymeter.service.imp.recommender.base.WekaBaseRecommender;
import com.joymeter.service.recommender.ActivityRecommender;

import weka.core.Instance;

@Service("wekaActivityRecommender")
public class WekaActivityRecommender implements ActivityRecommender{

	@Autowired
	ActivityService activityService;

	@Autowired
	WekaBaseRecommender wekaBaseRecommender;

	@Autowired
	DefaultAdviceActivityService defaultAdviceActService;

	public Advice suggestActivity(User user, long instant) throws Exception {
		DateTime now = new DateTime(instant);
		MomentOfDay mod = MomentOfDay.valueOf(now.getMillis());
		DayType dt = DayType.valueOf(now.getMillis());
		
		List<ActivityType> typesToRecommend = new ArrayList<ActivityType>();
		
		for (LevelOfSatisfaction los: LevelOfSatisfaction.values()){
			Instance instance = wekaBaseRecommender.createInstance(
					dt.name(), mod.name(), los.name(), null, wekaBaseRecommender.getUserInstances(user));
			
			ActivityType activityType = wekaBaseRecommender.suggestActivity(user, instance);
			if (!typesToRecommend.contains(activityType)){
				typesToRecommend.add(activityType);
			}
		};
		Random r = new Random();
		int index = r.nextInt(typesToRecommend.size());
		ActivityType activityType = typesToRecommend.get(index);

		List<Activity> activityList = activityService.getActivities(user).getActivities();
		List<Activity> activityListFiltered = filterByCriteria(activityList, activityType, dt, mod);

		index = r.nextInt(activityListFiltered.size());

		Advice advice = new Advice();
		advice.setSuggestedActivity(activityListFiltered.get(index));
		return advice;
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
