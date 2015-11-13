package com.joymeter.service.jpa;

import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.joymeter.entity.LevelOfJoy;
import com.joymeter.entity.User;
import com.joymeter.service.LevelOfJoyService;

@Service("levelOfJoyService")
public class LevelOfJoyServiceJpa implements LevelOfJoyService{

	private Logger log = Logger.getLogger(this.getClass());
	private EntityManager entityManager;
	
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public LevelOfJoy save(LevelOfJoy loj) {
		log.info("Saving: "+loj.getId());
		entityManager.persist(loj);
		entityManager.flush();
		return loj;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<LevelOfJoy> getByUser(User user) {
		Query queryFindHistory = entityManager.createNamedQuery("LevelOfJoy.findAllByUser");
		queryFindHistory.setParameter("userID", user.getId());
		List<LevelOfJoy> historical = queryFindHistory.getResultList();
		if( historical == null || historical.isEmpty()) {
			historical = calculateToday(user);
		}
		return historical;
	}
	
	private List<LevelOfJoy> calculateToday(User user) {
		LevelOfJoy last = getLastByUser(user);
		if (last != null) {
			Calendar c = Calendar.getInstance();
			Calendar today = Calendar.getInstance();
			c.setTimeInMillis(last.getMilliseconds());
			while(c.getTimeInMillis() < today.getTimeInMillis()) {
				c.add(Calendar.DAY_OF_MONTH, 1);
				LevelOfJoy actual = new LevelOfJoy();
				actual.setMilliseconds(c.getTimeInMillis());
				actual.setUser(user);
				actual.setLevel(calculateActualLevel(last.getLevel(), null));
				save(actual);
				last = actual;
			}
		}
		return null;
	}

	private Double calculateActualLevel(Double prevLevel, List<Double> activitiesLevels) {
		Double actual = decreseLevel(prevLevel);
		if (activitiesLevels != null && !activitiesLevels.isEmpty()) {
			activitiesLevels.add(actual);
			actual = calculateAverage(activitiesLevels);
		}
		return actual;
	}
	
	private double calculateAverage(List<Double> marks) {
		Double sum = Double.valueOf(0);
		if (!marks.isEmpty()) {
			for (Double mark : marks) {
				sum += mark;
			}
			return sum.doubleValue() / marks.size();
		}
		return sum;
	}

	private Double decreseLevel(Double prevLevel) {
		return prevLevel*0.01;
	}

	@SuppressWarnings("unchecked")
	public List<LevelOfJoy> getLastEntriesByUser(User user, int days) {
		
		if (days < 1) { //Today only
			return getByUser(user);
		}
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, days*-1);
		Query queryFindHistory = entityManager.createNamedQuery("LevelOfJoy.findLastEntriesByUser");
		queryFindHistory.setParameter("userID", user.getId());
		queryFindHistory.setParameter("millis", c.getTimeInMillis());
		List<LevelOfJoy> historical = queryFindHistory.getResultList();
		if( historical != null && days+1 != historical.size()) {
			calculateMissingDays(user, historical, days);
		}
		return historical;
	}

	private void calculateMissingDays(User user, List<LevelOfJoy> historical, int days) {
		// TODO Auto-generated method stub
		
	}

	@Transactional(readOnly = true)
	public LevelOfJoy getById(long id) {
		return entityManager.find(LevelOfJoy.class, id);
	}

	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public boolean delete(LevelOfJoy loj) {
		loj = entityManager.getReference(LevelOfJoy.class, loj.getId());
		if (loj == null){
			return false;
		}
		entityManager.remove(loj);
		entityManager.flush();
		return true;
	}

	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public LevelOfJoy update(LevelOfJoy loj) {
		entityManager.merge(loj);
		entityManager.flush();
		return loj;
	}

	@SuppressWarnings("unchecked")
	public LevelOfJoy getLastByUser(User user) {
		Query queryFindLast = entityManager.createNamedQuery("LevelOfJoy.findLastByUser");
		queryFindLast.setParameter("userID", user.getId());
		List<LevelOfJoy> lastListResult = queryFindLast.getResultList();
		if(lastListResult != null && !lastListResult.isEmpty()) {
			return lastListResult.get(0);
		}
		return null;
	}
}
