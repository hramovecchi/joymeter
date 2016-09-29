package com.joymeter.service.imp;

import java.util.Random;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joymeter.entity.Advice;
import com.joymeter.entity.User;
import com.joymeter.entity.util.ActivityUtils;
import com.joymeter.repository.AdviceRepository;
import com.joymeter.service.RecommendationService;
import com.joymeter.service.recommender.ActivityRecommender;

@Service("recommendationService")
public class RecommendationServiceImpl implements RecommendationService {

	@Autowired
	AdviceRepository adviceRepository;
	
	@Autowired
	ActivityRecommender randomActivityRecommender;
	
	@Autowired
	ActivityRecommender wekaActivityRecommender;

	@Autowired
	ActivityRecommender historyActivityRecommender;//wekaWithFilter

	public Advice suggestActivity(User user) {
		Advice advice = null;

//		Random ran = new Random();
//		switch(AdviceTechnique.values()[ran.nextInt(2)]){
//		case random:
//			advice = randomActivityRecommender.suggestActivity(user);
//			advice.setTechnique(AdviceTechnique.random.name());
//			break;
//		case weka:
//			advice = wekaActivityRecommender.suggestActivity(user);
//			advice.setTechnique(AdviceTechnique.weka.name());
//			break;
//		case wekaWithFilter:
//			advice = historyActivityRecommender.suggestActivity(user);
//			advice.setTechnique(AdviceTechnique.wekaWithFilter.name());
//			break;
//		}

		advice = wekaActivityRecommender.suggestActivity(user);
		advice.setTechnique(AdviceTechnique.weka.name());
		long now = DateTime.now().getMillis();

		advice.getSuggestedActivity().setStartDate(now);
		advice.getSuggestedActivity().setEndDate(now + ActivityUtils.durationToSuggest());

		adviceRepository.save(advice);

		return advice;
	}

	public enum AdviceTechnique{
		random,
		weka,
		wekaWithFilter;
	}

}
