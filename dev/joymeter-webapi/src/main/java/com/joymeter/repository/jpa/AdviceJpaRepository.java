package com.joymeter.repository.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.joymeter.entity.Advice;
import com.joymeter.repository.AdviceRepository;

@Service("adviceRepository")
public class AdviceJpaRepository implements AdviceRepository{
	
	private Logger log = Logger.getLogger(this.getClass());
	private EntityManager entityManager;
	
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public Advice save(Advice advice) {
		log.info("Saving: "+advice.getId());
		entityManager.persist(advice);
		entityManager.flush();
		return advice;
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Advice> getAllAdvicesByUserId(long userId) {
		Query queryfetchByUserId = entityManager.createNamedQuery("Advice.findAllByUser");
		queryfetchByUserId.setParameter("userID", userId);
		List<Advice> advices = queryfetchByUserId.getResultList();
		return advices;
	}

	@Transactional(readOnly = true)
	public Advice getById(long id) {
		return entityManager.find(Advice.class, id);
	}

	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public boolean delete(Advice advice) {
		Advice adviceToDelete = entityManager
				.getReference(Advice.class, advice.getId());
		if (adviceToDelete == null)
			return false;
		entityManager.remove(adviceToDelete);
		entityManager.flush();
		return true;
	}

	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public Advice update(Advice advice) {
		log.info("Updating: " + "advice " + advice.getId());
		entityManager.merge(advice);
		entityManager.flush();
		return advice;
	}

}
