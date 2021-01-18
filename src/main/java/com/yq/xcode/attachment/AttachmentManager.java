package com.yq.xcode.attachment;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface AttachmentManager {
	
	public Attachment addImage(String directory, String name, byte[] data,
			ImageTransform orginTransform,
			ImageTransform standardTransorm,
			ImageTransform thumbnailTransform);
	
	public Attachment addFile(String directory, String name, byte[] data);
	
	public void removeAttachment(String path);

	public File multipartFileToFile(MultipartFile multiFile) throws Exception;

	public void deleteFile(File... files) throws Exception;
	
	public File getAttachmentAsFile(String path);

}
