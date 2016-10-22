package com.joymeter.service;

import java.util.List;

public interface AdminService {
	void suggestUsers(List<String> users, long instant);
	void generateWekaModels();
}
