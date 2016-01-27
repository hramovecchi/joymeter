package com.joymeter.service.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.joymeter.entity.Activity;
import com.joymeter.service.ActivityService;

@Service("activityService")
public class ActivityServiceJpa implements ActivityService {
	private Logger log = Logger.getLogger(this.getClass());
	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public Activity save(Activity activity) {
		log.info("Saving: "+activity.getId());
		entityManager.persist(activity);
		entityManager.flush();
		return activity;
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Activity> getByUserId(long userID) {
		Query queryFindPerson = entityManager.createNamedQuery("Activity.findAllByUser");
		queryFindPerson.setParameter("userID", userID);
		List<Activity> activities = queryFindPerson.getResultList();
		return activities;
	}

	@Transactional(readOnly = true)
	public Activity getById(long id) {
		return entityManager.find(Activity.class, id);
	}

	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public boolean delete(Activity activity) {
		activity = entityManager.getReference(Activity.class, activity.getId());
		if (activity == null){
			return false;
		}
		entityManager.remove(activity);
		entityManager.flush();
		return true;
	}

	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public Activity update(Activity activity) {
		entityManager.merge(activity);
		entityManager.flush();
		return activity;
	}

	public Activity suggestActivity(long userID) {
		List<Activity> activities = getByUserId(userID);
		
		if (activities.isEmpty()){
			return getDefaultActivity();
		}
		
		return activities.get(0);
	}


	private Activity getDefaultActivity() {
		Activity a = new Activity();
		a.setClassified(Boolean.FALSE);
		a.setDescription("Birra con amigos");
		a.setLevelOfJoy(5);
		a.setSummary("Cerveza en Antares");
		a.setType("Recleación, Salida");
		return a;
	}

	@SuppressWarnings("unchecked")
	public List<Activity> getDayActivitiesByUserId(long userId, long startday, long endday) {
		Query queryFindActivities = entityManager.createNamedQuery("Activity.findDayActivitiesByUser");
		queryFindActivities.setParameter("userID", userId);
		queryFindActivities.setParameter("startday", startday);
		queryFindActivities.setParameter("endday", endday);
		List<Activity> activities = queryFindActivities.getResultList();
		return activities;
	}
}
