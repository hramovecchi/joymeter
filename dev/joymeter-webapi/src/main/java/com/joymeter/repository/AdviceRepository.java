package com.joymeter.repository;

import java.util.List;

import com.joymeter.entity.Advice;

public interface AdviceRepository {
	Advice save(Advice advice);
	List<Advice> getAllAdvicesByUserId(long userId);
	Advice getById(long id);
	boolean delete(Advice advice);
	Advice update(Advice advice);
}
