package com.yq.xcode.common.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.yq.xcode.attachment.Attachment;
import com.yq.xcode.attachment.AttachmentManager;
import com.yq.xcode.attachment.ImageFileRequest;
import com.yq.xcode.attachment.ImageTransform;
import com.yq.xcode.common.bean.Result;
import com.yq.xcode.common.service.AttachmentService;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.ResultUtil;

@CrossOrigin
@RestController
@RequestMapping(value = "/attachment")
public class AttachmentController{

	@Resource
	private AttachmentManager attachmentManager;
	@Autowired
	private AttachmentService attachmentService;

	@PostMapping("/uploadImage")
	public Result<Attachment> uploadImage(ImageFileRequest fileRequest, @RequestParam("file") MultipartFile file) {
		try {
			ImageTransform transform = ImageTransform.scale(100, 100, true);
			Attachment attachment = attachmentManager.addImage("/static/",
					file.getOriginalFilename(), file.getBytes(), null, null, transform);
			if (attachment == null) {
				return ResultUtil.error("图片不存在");
			}
			attachment.setImageGroup(fileRequest.getImageGroup());
			//如果原图小于300*300， 则将原图设置为缩略图
			String thumbnailPath = CommonUtil.isNull(attachment.getThumbnailPath()) ?
					attachment.getPath() : attachment.getThumbnailPath();
			attachment.setThumbnailPath(thumbnailPath);
			if(CommonUtil.isNotNull(fileRequest.getModelId())) {
				/** 根据modelid和modelName获取旧数据， 然后删除 **/
				Attachment attachmentByDB = attachmentService.getAttachment(fileRequest.getModelName(), fileRequest.getModelId());
				if (CommonUtil.isNotNull(attachmentByDB)) {
					attachmentService.deleteAttachment(fileRequest.getModelName(), fileRequest.getModelId());
					attachmentManager.removeAttachment(attachmentByDB.getThumbnailPath());
					attachmentManager.removeAttachment(attachmentByDB.getPath());
				}

				/** 保存新的附件 **/
				attachment.setModelId(fileRequest.getModelId());
				attachment.setModelName(fileRequest.getModelName());
				attachmentService.saveAttachment(attachment);
			}
			return ResultUtil.ok(attachment);
		} catch (Exception e) {
			return ResultUtil.error("系统操作异常 异常信息:" + e.getMessage());
		}
	}

	@PostMapping("/uploadFile")
	public Result<Attachment> uploadFile(ImageFileRequest fileRequest, @RequestParam("file") MultipartFile file) {
		try {

			Attachment attachment = attachmentManager.addFile("/static/",
					file.getOriginalFilename(), file.getBytes());
			if (attachment == null) {
				return ResultUtil.error("文件不存在");
			}
			if(CommonUtil.isNotNull(fileRequest.getModelId())) {
				/** 根据modelid和modelName获取旧数据， 然后删除 **/
				Attachment attachmentByDB = attachmentService.getAttachment(fileRequest.getModelName(), fileRequest.getModelId());
				attachmentService.deleteAttachment(fileRequest.getModelName(), fileRequest.getModelId());
				if (attachmentByDB != null){
					attachmentManager.removeAttachment(attachmentByDB.getThumbnailPath());
					attachmentManager.removeAttachment(attachmentByDB.getPath());
				}
				/** 保存新的附件 **/
				attachment.setModelId(fileRequest.getModelId());
				attachment.setModelName(fileRequest.getModelName());
				attachmentService.saveAttachment(attachment);
			}
			return ResultUtil.ok(attachment);
		} catch (Exception e) {
			return ResultUtil.error("系统操作异常 异常信息:" + e.getMessage());
		}
	}

	@PostMapping("/save")
	public Result<Attachment> saveAttachment(@RequestBody Attachment attachment){

		try {
			Attachment attachmentNew = attachmentService.saveAttachment(attachment);
			return ResultUtil.ok(attachmentNew);
		}catch(Exception e) {
			return ResultUtil.error("系统操作异常 异常信息:" + e.getMessage());
		}
	}

	@DeleteMapping("/delete")
	public Result<String> delete(@RequestParam long id) {

		try {
			Attachment attachment = attachmentService.getAttachment(id);
			attachmentService.deleteAttachment(id);
			attachmentManager.removeAttachment(attachment.getThumbnailPath());
			attachmentManager.removeAttachment(attachment.getPath());
			return ResultUtil.ok();
		} catch (Exception e) {
			return ResultUtil.error("系统操作异常 异常信息:" + e.getMessage());
		}
	}
	@GetMapping("/findAll")
	public Result<List<Attachment>> getAttachmentList(@RequestParam long modelId,@RequestParam(required = false) String modelName){
		try {
			List<Attachment> list = attachmentService.findAttachmentList(modelName, modelId);
			return ResultUtil.ok(list);
		}catch(Exception e) {
			return ResultUtil.error("系统操作异常 异常信息:" + e.getMessage());
		}
	}
	@GetMapping("/findListByNames")
	public Result<List<Attachment>> findAttachmentList(@RequestParam long modelId,
													   @RequestParam String ... modelNames ){
		try {
			List<Attachment> list = attachmentService.findAttachmentList(modelId, modelNames);
			return ResultUtil.ok(list);
		}catch(Exception e) {
			return ResultUtil.error("系统操作异常 异常信息:" + e.getMessage());
		}
	}

	@GetMapping("/findListByIds")
	public Result<List<Attachment>> findAttachmentList(@RequestParam String modelName,
													   @RequestParam long ... modelIds ){
		try {
			List<Attachment> list = attachmentService.findAttachmentList(modelName, modelIds);
			return ResultUtil.ok(list);
		}catch(Exception e) {
			return ResultUtil.error("系统操作异常 异常信息:" + e.getMessage());
		}
	}

}
