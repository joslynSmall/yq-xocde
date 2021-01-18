package com.yq.xcode.common.service;

import java.util.List;

import com.yq.xcode.security.entity.JpaBaseModel;

public interface YqSequenceService {

	public <E extends JpaBaseModel> void genId(E entity);

	public <E extends JpaBaseModel> void genId(List<E> entities);

	public String nextTextSequenceNumber(String prefix, String suffix, int numberSize, String sequenceId);

	public void assignSequenceNumber(List entityList, String numProperty, String prefix, String suffix, int numberSize,
			String sequenceId);

	public <E extends JpaBaseModel> void addIdSequence(Class<E> clazz, int size);

	public <E extends JpaBaseModel> void addIdSequence(String sequenceId, int size);

	public Long idNext(String sequenceId);

	public <E extends JpaBaseModel> Long idNext(Class<E> clazz, int size);

 
	
}
