package com.joymeter.repository;

import java.util.List;

import com.joymeter.entity.Session;

public interface SessionRepository {
	boolean save(Session session);
	Session getById(long id); //hopefully we never gona use it
	Session getBySessionToken(String sessionToken);
	List<Session> getByUserId(long userID);
	boolean delete(Session session);
	boolean update(Session session);
	boolean deleteByUserId(long userID, String gcmToken);
	boolean deleteByDeviceId(String deviceId);
}
