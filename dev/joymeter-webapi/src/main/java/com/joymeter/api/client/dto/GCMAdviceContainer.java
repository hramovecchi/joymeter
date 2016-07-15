package com.joymeter.api.client.dto;

import com.joymeter.entity.Advice;

public class GCMAdviceContainer {

	private Advice advice;
	
	public GCMAdviceContainer(Advice advice){
		this.setAdvice(advice);
	}

	public Advice getAdvice() {
		return advice;
	}

	public void setAdvice(Advice advice) {
		this.advice = advice;
	}
}
