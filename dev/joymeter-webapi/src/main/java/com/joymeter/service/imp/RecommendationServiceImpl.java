package com.joymeter.service.imp;

import java.util.HashMap;
import java.util.Map;

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
	ActivityRecommender wekaActivityRecommender;

	@Autowired
	ActivityRecommender wekaWithFilterActivityRecommender;

	private Map<Long, AdviceTechnique> userLastRecommendationTechnique = new HashMap<Long, AdviceTechnique>();

	public Advice suggestActivity(User user) throws Exception {
		Advice advice = null;

		switch(getRecommendationTechnique(user)){
		case weka:
			advice = wekaActivityRecommender.suggestActivity(user);
			advice.setTechnique(AdviceTechnique.weka.name());
			break;
		case wekaWithFilter:
			advice = wekaWithFilterActivityRecommender.suggestActivity(user);
			advice.setTechnique(AdviceTechnique.wekaWithFilter.name());
			break;
		}

		long now = DateTime.now().getMillis();

		advice.setUser(user);
		advice.getSuggestedActivity().setStartDate(now);
		advice.getSuggestedActivity().setEndDate(now + ActivityUtils.durationToSuggest());

		adviceRepository.save(advice);

		return advice;
	}

	private AdviceTechnique getRecommendationTechnique(User user) {
		if (userLastRecommendationTechnique.containsKey(user.getId())){
			AdviceTechnique at = userLastRecommendationTechnique.get(user.getId());
			userLastRecommendationTechnique.put(user.getId(), at.nextTechnique());
			return at.nextTechnique();
		} else {
			AdviceTechnique at = AdviceTechnique.weka;
			userLastRecommendationTechnique.put(user.getId(), at);
			return at;
		}
	}

	private enum AdviceTechnique{
		weka,
		wekaWithFilter;

		public AdviceTechnique nextTechnique(){
			switch (this){
			case weka:
				return wekaWithFilter;
			case wekaWithFilter:
				return weka;
			}
			return weka;
		}
	}
}
