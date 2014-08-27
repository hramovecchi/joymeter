package com.joymeter.service.jpa;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.joymeter.entity.Session;
import com.joymeter.entity.User;
import com.joymeter.service.SessionService;


@Service("sessionService")
public class SessionServiceJpa implements SessionService {
	private Logger log = Logger.getLogger(this.getClass());
	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Transactional(readOnly = true)
	public Session getById(long id) {
		return entityManager.find(Session.class, id);
	}

	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public boolean save(Session session) {
		log.info("Saving: "+ session.toString());
		entityManager.merge(session);
		entityManager.flush();
		return true;
	}

	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public boolean update(Session session) {
		log.info("Updating: "+ session.toString());
		entityManager.merge(session);
		entityManager.flush();
		return true;
	}
	
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public boolean delete(Session session) {
		session = entityManager.getReference(Session.class, session.getId());
		if (session == null)
			return false;
		entityManager.remove(session);
		entityManager.flush();
		return true;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Session> getByUserId(long userID) {
		Query queryFindSessions = entityManager.createNamedQuery("Session.findAllByUser");
		queryFindSessions.setParameter("userID", userID);
		List<Session> sessions = queryFindSessions.getResultList();
		return sessions;
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Session getBySessionToken(String sessionToken) {
		Query queryFindSession = entityManager.createNamedQuery("Session.findSessionBySessionToken");
		queryFindSession.setParameter(sessionToken, sessionToken);
		List<Session> sessions = queryFindSession.getResultList();
		return (sessions.isEmpty()? null: sessions.get(0));
	}

	public boolean deleteByUserId(User user) {
		for (Session session:this.getByUserId(user.getId())){
			this.delete(session);
			return true;
		}
		return false;
	}
}
