package com.yq.xcode.common.service;

import java.util.List;

import com.yq.xcode.attachment.Attachment;

/**
 * @Auther ycwang
 * @Date 2020/4/8
 */
public interface AttachmentService {

	/**
	 * 保存附件
	 * @param entityAttachment
	 */
	public Attachment saveAttachment(Attachment attachment);
	/**
	 * 批量保存附件
	 * @param attachmentList
	 * @return List<Attachment>
	 */
	public List<Attachment> saveAttachmentList(List<Attachment> attachmentList);
	/**
	 * 删除附件
	 * @param id 附件id
	 */
	public void deleteAttachment(long id);
	/**
	 * 删除附件
	 * @param modelName 
	 * @param modelId 
	 */
	public void deleteAttachment(String modelName, long modelId);

	/**
	 * 根据id查询附件
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Attachment getAttachment(long id);
	/**
	 * 获取单个Attachment
	 * @param modelName
	 * @param modelId
	 * @return
	 * @throws Exception
	 */
	public Attachment getAttachment(String modelName, long modelId);
	/**
	 * 获取AttachmentView
	 * @param modelName
	 * @param modelId
	 * @return
	 * @throws Exception
	 */
	public List<Attachment> findAttachmentList(String modelName, long modelId);

	/**
	 * 适用一个实体多附件
	 * @param modelId
	 * modelName 不固定参数，多参数获取
	 * @param modelName
	 * @return List<Attachment>
	 */
	public List<Attachment> findAttachmentList(long modelId, String ...  modelNames);

	/**
	 * 适用同类别多modelId 的附件列表
	 * @param modelId
	 * modelName 不固定参数，多参数获取
	 * @param modelName
	 * @return List<Attachment>
	 */
	public List<Attachment> findAttachmentList(String modelName,long ...  modelIds);
	

}
