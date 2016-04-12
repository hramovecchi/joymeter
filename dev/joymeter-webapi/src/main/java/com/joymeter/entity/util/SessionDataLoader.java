package com.joymeter.entity.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.joymeter.entity.Session;
import com.joymeter.repository.SessionRepository;

public class SessionDataLoader {
	private List<Session> sessions = new ArrayList<Session>();
	private SessionRepository sessionService;
	
	public void loadData() {
		for (Session session : sessions) {
			sessionService.save(session);
		}
		sessions.clear();
		sessions = null;
	}

	public void setSessions(List<Session> sessions) {
		this.sessions = sessions;
	}
	@Autowired
	public void setSessionService(SessionRepository sessionService) {
		this.sessionService = sessionService;
	}
	
	public void init() {
		loadData();
	}

}
