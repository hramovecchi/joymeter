package com.joymeter.service.imp.recommender;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joymeter.entity.Activity;
import com.joymeter.entity.Advice;
import com.joymeter.entity.User;
import com.joymeter.service.ActivityService;
import com.joymeter.service.DefaultAdviceActivityService;
import com.joymeter.service.imp.recommender.base.ActivityPredicate;
import com.joymeter.service.imp.recommender.base.ActivityType;
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

	public Advice suggestActivity(User user) throws Exception {
		DateTime now = DateTime.now();
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
		activityList.removeIf(new ActivityPredicate(activityType.getType()));
		
		//In case there is no activity for the selected type, return a preset one
		if (activityList.isEmpty()){
			activityList = defaultAdviceActService.getActivities();
			activityList.removeIf(new ActivityPredicate(activityType.getType()));
		}
		index = r.nextInt(activityList.size());

		Advice advice = new Advice();
		advice.setSuggestedActivity(activityList.get(index));
		return advice;
	}
}
