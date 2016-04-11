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

import com.joymeter.entity.LevelOfJoy;
import com.joymeter.entity.User;
import com.joymeter.repository.LevelOfJoyRepository;

@Service("levelOfJoyRepository")
public class LevelOfJoyJpaRepository implements LevelOfJoyRepository{

	private Logger log = Logger.getLogger(this.getClass());
	private EntityManager entityManager;
	
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public LevelOfJoy save(LevelOfJoy loj) {
		log.info("Saving: "+loj.toString());
		if (loj.getId() != 0) {
			return update(loj);
		}
		
		LevelOfJoy storedLoj = getByUserDate(loj.getUser(), loj.getDate());
		if (storedLoj != null) {
			loj.setId(storedLoj.getId());
			return update(loj);
		}
		entityManager.persist(loj);
		entityManager.flush();
		return loj;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<LevelOfJoy> getByUser(User user) {
		Query queryFindHistory = entityManager.createNamedQuery("LevelOfJoy.findAllByUser");
		queryFindHistory.setParameter("userID", user.getId());
		return queryFindHistory.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public LevelOfJoy getByUserDate(User user, DateTime date) {
		Query queryFindHistory = entityManager.createNamedQuery("LevelOfJoy.findByDateUser");
		queryFindHistory.setParameter("userID", user.getId());
		queryFindHistory.setParameter("millis", date.withTimeAtStartOfDay().getMillis());
		List<LevelOfJoy> listResult = queryFindHistory.getResultList();
		if(listResult != null && !listResult.isEmpty()) {
			return listResult.get(0);
		}
		return null;
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
		log.info("Updating: "+loj.getId());
		entityManager.merge(loj);
		entityManager.flush();
		return loj;
	}

	@SuppressWarnings("unchecked")
	public LevelOfJoy getLastByUser(User user) {
		Query queryFindLast = entityManager.createNamedQuery("LevelOfJoy.findLastByUser");
		queryFindLast.setParameter("userID", user.getId());
		List<LevelOfJoy> lastListResult = queryFindLast.setMaxResults(1).getResultList();
		if(lastListResult != null && !lastListResult.isEmpty()) {
			return lastListResult.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public LevelOfJoy getPreviousByUser(User user, DateTime date) {
		Query queryFindLast = entityManager.createNamedQuery("LevelOfJoy.findByDateUser");
		queryFindLast.setParameter("userID", user.getId());
		queryFindLast.setParameter("millis", date.withTimeAtStartOfDay().minusDays(1).getMillis());
		List<LevelOfJoy> lastListResult = queryFindLast.getResultList();
		if(lastListResult != null && !lastListResult.isEmpty()) {
			return lastListResult.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<LevelOfJoy> getLastEntriesByUser(User user, int days) {
		
		if (days < 0) {
			return null;
		}
		long today = DateTime.now().withTimeAtStartOfDay().getMillis();
		long millis = DateTime.now().plusDays(-days).withTimeAtStartOfDay().getMillis();//rest the number of days to today
		
		Query queryFindHistory = entityManager.createNamedQuery("LevelOfJoy.findLastEntriesByUser");
		queryFindHistory.setParameter("userID", user.getId());
		queryFindHistory.setParameter("millis", millis);
		queryFindHistory.setParameter("today", today);
		List<LevelOfJoy> historical = queryFindHistory.getResultList();
		return historical;
	}
}
