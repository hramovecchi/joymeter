package com.joymeter.repository.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.joymeter.entity.Activity;
import com.joymeter.repository.ActivityRepository;

@Service("activityRepository")
public class ActivityJpaRepository implements ActivityRepository {
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
	public List<Activity> getAllActivitiesByUserId(long userID) {
		Query queryFindPerson = entityManager.createNamedQuery("Activity.findAllByUser");
		queryFindPerson.setParameter("userID", userID);
		List<Activity> activities = queryFindPerson.getResultList();
		return activities;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Activity> fetchActiveActivitiesByUserId(long userID){
		Query fetchByUserAndDeleteState = entityManager.createNamedQuery("Activity.fetchByUserAndDeleteState");
		fetchByUserAndDeleteState.setParameter("userID", userID);
		fetchByUserAndDeleteState.setParameter("deletestate", false);
		List<Activity> activities = fetchByUserAndDeleteState.getResultList();
		return activities;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Activity> fetchDeletedActivitiesByUserId(long userID){
		Query fetchByUserAndDeleteState = entityManager.createNamedQuery("Activity.fetchByUserAndDeleteState");
		fetchByUserAndDeleteState.setParameter("userID", userID);
		fetchByUserAndDeleteState.setParameter("deletestate", true);
		List<Activity> activities = fetchByUserAndDeleteState.getResultList();
		return activities;
	}

	@Transactional(readOnly = true)
	public Activity getById(long id) {
		return entityManager.find(Activity.class, id);
	}

	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public boolean hardDelete(Activity activity) {
		activity = entityManager.getReference(Activity.class, activity.getId());
		if (activity == null){
			return false;
		}
		entityManager.remove(activity);
		entityManager.flush();
		return true;
	}
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public boolean delete(Activity activity) {
		activity.setDeleted(true);
		entityManager.merge(activity);
		entityManager.flush();
		return true;
	}

	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public Activity update(Activity activity) {
		entityManager.merge(activity);
		entityManager.flush();
		return activity;
	}

	@SuppressWarnings("unchecked")
	public List<Activity> getDayActivitiesByUserId(long userId, DateTime date) {
		
		Query queryFindActivities = entityManager.createNamedQuery("Activity.findDayActivitiesByUser");
		queryFindActivities.setParameter("userID", userId);
		queryFindActivities.setParameter("startday", date.withTimeAtStartOfDay().getMillis());
		queryFindActivities.setParameter("endday", date.plusDays(1).withTimeAtStartOfDay().getMillis()-1);
		List<Activity> activities = queryFindActivities.getResultList();
		return activities;
	}
	
	
}
