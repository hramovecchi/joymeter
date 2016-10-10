package com.joymeter.service.imp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joymeter.entity.Activity;
import com.joymeter.service.DefaultAdviceActivityService;

@Service("defaultAdviceActivityService")
public class DefaultAdviceActivityServiceImpl implements DefaultAdviceActivityService{

	private Resource activityToAdviceFile;
	private List<Activity> activityToAdviceList = new ArrayList<Activity>();

	@Autowired
	public DefaultAdviceActivityServiceImpl(
			@Value("classpath:DefaultActivitiesToAdvice.json") Resource activityToAdviceInfoFile) {
		this.activityToAdviceFile = activityToAdviceInfoFile;
		loadActivityToAdviceInfo();
	}

	private void loadActivityToAdviceInfo() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			Activity[] dummyData = mapper.readValue(
					activityToAdviceFile.getInputStream(),
					Activity[].class);
			for (Activity activityToAdvice : dummyData) {
				activityToAdviceList.add(activityToAdvice);
			}
		} catch (Exception e) {
			throw new RuntimeException(
					"Error during activity to advice loading!", e);
		}
	}

	public List<Activity> getActivities() {
		return activityToAdviceList;
	}
}
