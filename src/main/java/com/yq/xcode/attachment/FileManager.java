package com.yq.xcode.attachment;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileManager {
	
	@Value("${static.path.templates}")
    private String templates; 
	
	@Autowired
	private AttachmentManager attachmentManager;
	
	public File getFile(Attachment file) {
		return attachmentManager.getAttachmentAsFile(file.getPath());
	}
	
	public File getFile(String path) {
		return attachmentManager.getAttachmentAsFile(path);
	}
	
	public File downloadExcelTemplate(String fileName) {
		 return new File(templates,fileName);
	 }
	

}
