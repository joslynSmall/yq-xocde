package com.yq.xcode.attachment;


public interface AttachmentPathResolver {
	
	public String resolvePath(String name) ;
	
	public String getRootPath();
	
	public boolean isAttachmentPath(String path);
}
