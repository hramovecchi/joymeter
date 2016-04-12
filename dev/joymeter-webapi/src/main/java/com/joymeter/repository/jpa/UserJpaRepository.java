package com.joymeter.repository.jpa;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.joymeter.entity.User;
import com.joymeter.repository.UserRepository;


@Service("userRepository")
public class UserJpaRepository implements UserRepository {
	private Logger log = Logger.getLogger(this.getClass());
	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Transactional(readOnly = true)
	public User getById(long id) {
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
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public User getByFacebookAccessToken(String facebookAccessToken) {
		Query query = entityManager.createNamedQuery("User.findByFacebookAccessToken");
		query.setParameter("facebookAccessToken", facebookAccessToken);
		List<User> users = null;
		users = query.getResultList();
		return users.isEmpty()?null:users.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public User getByEmail(String email) {
		Query query = entityManager.createNamedQuery("User.findByEmail");
		query.setParameter("email", email);
		List<User> users = null;
		users = query.getResultList();
		return users.isEmpty()?null:users.get(0);
	}

	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public boolean save(User user) {
		log.info("Saving: "+user.toString());
		entityManager.merge(user);
		entityManager.flush();
		return true;
	}

	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public boolean update(User user) {
		log.info("Updating: "+user.toString());
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
