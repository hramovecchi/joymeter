package com.joymeter.service.jpa;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.joymeter.entity.User;
import com.joymeter.service.UserService;


@Service("userService")
public class UserServiceJpa implements UserService {
	private Logger log = Logger.getLogger(this.getClass());
	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Transactional(readOnly = true)
	public User getById(int id) {
		return entityManager.find(User.class, id);
	}
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<User> getAll() {
		Query query = entityManager.createNamedQuery("User.findAll");
		List<User> users = null;
		users = query.getResultList();
		return users;
	}

	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public boolean save(User user) {
		log.info("Saving: "+user.getId());
		entityManager.persist(user);
		entityManager.flush();
		return true;
	}

	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public boolean update(User user) {
		entityManager.merge(user);
		entityManager.flush();
		return true;
	}
	
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public boolean delete(User user) {
		user = entityManager.getReference(User.class, user.getId());
		if (user == null)
			return false;
		entityManager.remove(user);
		entityManager.flush();
		return true;
	}
	
}
