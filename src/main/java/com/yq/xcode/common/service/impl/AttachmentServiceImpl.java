package com.yq.xcode.common.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yq.xcode.attachment.Attachment;
import com.yq.xcode.common.model.EntityAttachment;
import com.yq.xcode.common.service.AttachmentService;
import com.yq.xcode.common.service.SqlToModelService;
import com.yq.xcode.common.utils.CommonUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @Auther ycwang
 * @Date 2020/4/8
 */
@Transactional
@Slf4j
@Service
public class AttachmentServiceImpl extends YqJpaDataAccessObject implements AttachmentService {
	
	@Autowired
    private SqlToModelService sqlToModelService;
	
	private EntityAttachment view2Entity(Attachment attachment){
		EntityAttachment entity = new EntityAttachment();
		entity.setId(attachment.getId());
		entity.setImageGroup(attachment.getImageGroup());

		if (CommonUtil.isNull(attachment.getName())){
			String path = attachment.getPath();
			String name = path.substring(path.lastIndexOf("/") - 1);
			entity.setName(name);
		}else{
			entity.setName(attachment.getName());
		}
		entity.setModelId(attachment.getModelId());
		entity.setModelName(attachment.getModelName());
		entity.setDisplayName(attachment.getName());
		entity.setSize(attachment.getSize());
		entity.setThumbnailPath(attachment.getThumbnailPath());
		entity.setPath(attachment.getOrginalPath());
		entity.setImageGroup(attachment.getImageGroup());
		entity.setLabel(attachment.getLabel());
		entity.setType(attachment.getType());
		return entity;
	}

	@Override
	public Attachment saveAttachment(Attachment attachment){

		EntityAttachment entity = this.save(view2Entity(attachment));
		attachment.setId(entity.getId());
		return attachment;
	}

	@Override
	public List<Attachment> saveAttachmentList(List<Attachment> attachmentList){
		for(Attachment attachment : attachmentList){
			saveAttachment(attachment);
		}
		return attachmentList;
	}

	@Override
	public void deleteAttachment(long id){
		this.deleteById(EntityAttachment.class, id);	
	}
	@Override
	public void deleteAttachment(String modelName, long modelId) {
		  this.execute(" DELETE FROM EntityAttachment "
		  		+ " WHERE modelName = ?1 and modelId = ?2 ",modelName,modelId);		
	}
	@Override
	public Attachment getAttachment(long id){
		String sql = " select *  from attachment "
				+ "where id =" + id ;
		Attachment attachment = this.sqlToModelService.getSingleRecord(sql,null, Attachment.class);
		return attachment;
	}

	@Override
	public Attachment getAttachment(String modelName, long modelId) {
		List<Attachment> list = findAttachmentList(modelName, modelId);
		 if(CommonUtil.isNotNull(list)) {
			 return list.get(0);
		 }
		return null;
	}

	@Override
	public List<Attachment> findAttachmentList(String modelName, long modelId) {
		String sql = " select id," +
				" display_name displayName, image_group imageGroup,"
				+ " label, model_id modelId, model_name modelName,"
				+ " size, type, path,"
				+ " thumbnail_path thumbnailPath"
				+ " from attachment "
				+ " where model_id =" + modelId
				+ " and  model_name ='" + modelName + "'";
		List<Attachment> list = this.sqlToModelService.executeNativeQuery(sql,null, Attachment.class);
		return list;
	}

	@Override
	public List<Attachment> findAttachmentList(long modelId, String... modelName){
		String sql = " select id, display_name displayName, "
				+ "image_group imageGroup, label, "
				+ " model_id modelId, model_name modelName,"
				+ " size, status, thumbnail, type, path,"
				+ " thumbnail_path thumbnailPath"
				+ " from attachment "
				+ " where model_id =" + modelId
				+ " and " + CommonUtil.genInStrByList(" model_name ", modelName);
		List<Attachment> list = this.sqlToModelService.executeNativeQuery(sql,null, Attachment.class);
		return list;
	}

	@Override
	public List<Attachment> findAttachmentList(String modelName, long ... modelId){
		String sql =" select id, display_name displayName, "
				+ "image_group imageGroup, label, "
				+ " model_id modelId, model_name modelName,"
				+ " size, status, thumbnail, type, path,"
				+ " thumbnail_path thumbnailPath"
				+ " from attachment "
				+ " where model_name ='" + modelName + "'"
				+ " and " + CommonUtil.genIdInCause(" model_id ", modelId);
		List<Attachment> list = this.sqlToModelService.executeNativeQuery(sql,null, Attachment.class);
		return list;
	}





}
