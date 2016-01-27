package com.joymeter.service;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joymeter.entity.Activity;
import com.joymeter.entity.LevelOfJoy;
import com.joymeter.entity.User;

@Service("levelOfJoyHistoricalService")
public class LevelOfJoyHistoricalService {
	
	@Autowired
	ActivityService activityService;
	
	@Autowired
	LevelOfJoyService levelOfJoyService;
	
	public LevelOfJoy calculateToday(User user) {
			LevelOfJoy last = levelOfJoyService.getLastByUser(user);
			if (last != null) {
				DateTime today = DateTime.now();
				DateTime actualDate = new DateTime(last.getMilliseconds());
				List<LevelOfJoy> activities = null;
				while(isPreviousDay(actualDate, today)) {
					actualDate = actualDate.plusDays(1).withTimeAtStartOfDay();
					if (isSameDay(actualDate, today)) {
						activities = getActualActivities(user); //OJOOOOO aca estoy usando el millis del levelof joy como duracion en vez de date
					}
					last = createActual(user, actualDate.getMillis(), last.getLevel(), activities);
				}
				return last; //revisar si aca estoy devolviendo el today sin el aplicarle las actividades, si agrego el listado de actividades arriba, creo q se soluciona, pero tengo llamados innecesarios a la base.
			}
			
			return createActual(user,getStartOfDayMillis(DateTime.now()), Double.valueOf(0), getActualActivities(user));
	}
	
	private LevelOfJoy createActual(User user, long actualMillis, Double prevLevel, List<LevelOfJoy> activities) {
		LevelOfJoy actual = new LevelOfJoy();
		actual.setMilliseconds(actualMillis);
		actual.setUser(user);
		actual.setLevel(calculateActualLevel(prevLevel, activities));
		levelOfJoyService.save(actual);
		return actual;
	}
	
	private boolean isSameDay(DateTime day1, DateTime day2) {
		return day1.withTimeAtStartOfDay().isEqual(day2.withTimeAtStartOfDay());
	}
	
	private boolean isLaterDay(DateTime day1, DateTime day2) {
		return day1.withTimeAtStartOfDay().isAfter(day2.withTimeAtStartOfDay());
	}
	
	private boolean isPreviousDay(DateTime day1, DateTime day2) {
		return day1.withTimeAtStartOfDay().isBefore(day2.withTimeAtStartOfDay());
	}

	private List<LevelOfJoy> getActualActivities(User user) {
		return getActualActivities(DateTime.now(), null, user);		
	}
	
	private long getStartOfDayMillis(DateTime date) {
		return date.withTimeAtStartOfDay().getMillis();
	}
	
	private long getEndOfDayMillis(DateTime date) {
		return date.plusDays(1).withTimeAtStartOfDay().getMillis()-1;
	}
	
	private LevelOfJoy extractLevelOfJoy(User user, Activity activity) {
		LevelOfJoy levelOfJoy = new LevelOfJoy();
		levelOfJoy.setUser(user);
		levelOfJoy.setLevel(Double.valueOf(activity.getLevelOfJoy()));
		levelOfJoy.setMilliseconds(activity.getEndDate() - activity.getStartDate());
		return levelOfJoy;
	}
	
	private Double calculateActualLevel(Double prevLevel, List<LevelOfJoy> activities) {
		Double actual = decreseLevel(prevLevel);
		if (activities != null && !activities.isEmpty()) {
			actual = calculateAverage(actual, activities);
		}
		return actual;
	}
	
	private double calculateAverage(Double actual, List<LevelOfJoy> activities) {
		if (!activities.isEmpty()) {
			for (LevelOfJoy mark : activities) {
				actual += mark.getLevel();
			}
			return actual.doubleValue() / activities.size()+1;
		}
		return actual;
	}

	private Double decreseLevel(Double prevLevel) {
		return prevLevel*0.99;
	}
	
	@SuppressWarnings("unchecked")
	public List<LevelOfJoy> getLastEntriesByUser(User user, int days) {
		
		List<LevelOfJoy> historical = levelOfJoyService.getLastEntriesByUser(user, days);
		if( historical != null && days+1 != historical.size()) {
			calculateToday(user); //calculates todays level and update the missing entries - it happens when the last activity entry is older than today.
			historical = levelOfJoyService.getLastEntriesByUser(user, days);
		}
		return historical;
	}

	public void updateHistoryNewActivity(Activity activity) {
		
		LevelOfJoy last = levelOfJoyService.getLastByUser(activity.getUser());
		DateTime lastDate = (last != null) ? new DateTime(last.getMilliseconds()).withTimeAtStartOfDay() : null;//revisar si se necesita el with time
		Double lastLevel = (last != null) ? last.getLevel() : Double.valueOf(0);
		DateTime today = DateTime.now();
		DateTime startActivityDate = new DateTime(activity.getStartDate());
		DateTime endActivityDate = new DateTime(activity.getEndDate());
		DateTime endDate = null;
		if (lastDate == null ) {
			endDate = isLaterDay(today, endActivityDate) ? today : endActivityDate;
		} else {
			endDate = isLaterDay(today, endActivityDate) && isLaterDay(today, lastDate) ? today : isLaterDay(lastDate, endActivityDate) ? lastDate : endActivityDate;
		}
		DateTime actualDate = (lastDate != null && isPreviousDay(lastDate, startActivityDate)) ? lastDate : startActivityDate;
		
		while(!isPreviousDay(endDate, actualDate)) {
			List<LevelOfJoy> activities = null;
			if (intoDayRange(actualDate, startActivityDate, endActivityDate)) {
				activities = getActualActivities(actualDate, activity, activity.getUser());
			}
			lastLevel = createActual(activity.getUser(), actualDate.getMillis(), lastLevel, activities).getLevel();
			actualDate  = actualDate.plusDays(1).withTimeAtStartOfDay();
		}
	}
	
	private boolean intoDayRange(DateTime actualDate, DateTime startActivityDate, DateTime endActivityDate) {
		return !isPreviousDay(endActivityDate, actualDate) && !isPreviousDay(actualDate, startActivityDate);
	}

	private List<LevelOfJoy> getActualActivities(DateTime actualDate, Activity newActivity, User user) {

		long startOfDay = getStartOfDayMillis(actualDate);
		long endOfDay = getEndOfDayMillis(actualDate);
		
		List<LevelOfJoy> actualActivities = new ArrayList<LevelOfJoy>();
		List<Activity> activities = activityService.getDayActivitiesByUserId(user.getId(), startOfDay, endOfDay);
		if(newActivity != null) {
			DateTime startActivityDate = new DateTime(newActivity.getStartDate());
			DateTime endActivityDate = new DateTime(newActivity.getEndDate());
			if (intoDayRange(actualDate, startActivityDate, endActivityDate)) {
				activities.add(newActivity);
			}
		}
		for (Activity activity : activities) {
			
			DateTime startDate = new DateTime(activity.getStartDate());
			DateTime endDate = new DateTime(activity.getEndDate());

			LevelOfJoy todaysActivity = extractLevelOfJoy(user, activity);
			if(!isSameDay(startDate, endDate)) {
				long endTime = isSameDay(endDate, actualDate) ? activity.getEndDate() : endOfDay;
				long startTime = isSameDay(startDate, actualDate) ? activity.getStartDate() : startOfDay;
				todaysActivity.setMilliseconds(endTime - startTime);
			}
			actualActivities.add(todaysActivity);
		}
		return actualActivities;
	}

	public void updateHistoryDeleteActivity(Activity activity) {
		DateTime startActivityDate = new DateTime(activity.getStartDate());
		DateTime endActivityDate = new DateTime(activity.getEndDate());
		LevelOfJoy prev = levelOfJoyService.getPreviousByUser(activity.getUser(), startActivityDate); //REVISAR PUEDE Q YO NECESITE UN ACTIVITY Y NO UN LEVEL
		Double prevLevel = (prev != null) ? prev.getLevel() : Double.valueOf(0);
		
		LevelOfJoy last = levelOfJoyService.getLastByUser(activity.getUser());
		DateTime lastDate = (last != null) ? new DateTime(last.getMilliseconds()).withTimeAtStartOfDay() : null;
		
		DateTime today = DateTime.now();
		
		DateTime endDate = isLaterDay(today, lastDate) ? today : lastDate;
		DateTime actualDate = startActivityDate;
		while(!isPreviousDay(endDate, actualDate)) {
			List<LevelOfJoy> activities = null;
			if (intoDayRange(actualDate, startActivityDate, endActivityDate)) {
				activities = getActualActivities(actualDate, null, activity.getUser());
			}
			prevLevel = createActual(activity.getUser(), actualDate.getMillis(), prevLevel, activities).getLevel();
			actualDate  = actualDate.plusDays(1).withTimeAtStartOfDay();
		}
	}
	
	public void updateHistoryUpdateActivity(Activity activity) {
		DateTime startActivityDate = new DateTime(activity.getStartDate());
		LevelOfJoy last = levelOfJoyService.getPreviousByUser(activity.getUser(), startActivityDate);
		Double lastLevel = Double.valueOf(0);
		if(last != null) {
			lastLevel = last.getLevel() ;
		}
		DateTime today = DateTime.now();
		
		DateTime endActivityDate = new DateTime(activity.getEndDate());
		DateTime endDate = isLaterDay(today, endActivityDate) ? today : endActivityDate;
		DateTime actualDate = startActivityDate;
		while(!isPreviousDay(endDate, actualDate)) {
			List<LevelOfJoy> activities = null;
			if (intoDayRange(actualDate, startActivityDate, endActivityDate)) {
				activities = getActualActivities(actualDate, null, activity.getUser());
			}
			lastLevel = createActual(activity.getUser(), actualDate.getMillis(), lastLevel, activities).getLevel();
			actualDate  = actualDate.plusDays(1).withTimeAtStartOfDay();
		}
	}
}
