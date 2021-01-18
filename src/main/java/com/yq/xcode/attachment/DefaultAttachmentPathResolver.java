package com.yq.xcode.attachment;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class DefaultAttachmentPathResolver implements AttachmentPathResolver {

	public static enum PathStrategy {
		DAILY, HOURLY
	}

	private static PathStrategy strategy = PathStrategy.DAILY;

	private DateFormat timeFormat = new SimpleDateFormat("yyyyMMdd");

	private String rootPath = "/attachments";

	private boolean showRealNameAsPrefix = false;

	@Override
	public String resolvePath(String name) {
		return rootPath + "/" + timePath() + "/" + uniqueName(name);
	}

	@Override
	public boolean isAttachmentPath(String path) {
		if (!StringUtils.hasText(path)) {
			return false;
		}
		return path.startsWith(rootPath);
	}

	protected String timePath() {
		return timeFormat.format(new Date());
	}

	protected String uniqueName(String fileName) {
		String uuid = UUID.randomUUID().toString();
		uuid = uuid.replaceAll("-", "");
		String name = fileName;
		int lastDotIndex = name.lastIndexOf('.');
		if (lastDotIndex > 0) {
			name = (showRealNameAsPrefix ? (name.substring(0, lastDotIndex) + "-") : "") + uuid
					+ name.substring(lastDotIndex);
		} else if (lastDotIndex == 0) {
			name = uuid + name.substring(lastDotIndex);
		} else {
			name = (showRealNameAsPrefix ? (name + "-") : "") + uuid;
		}

		return name;
	}

	public PathStrategy getStrategy() {
		return strategy;
	}

	public void setStrategy(PathStrategy strategy) {
		DefaultAttachmentPathResolver.strategy = strategy;
		if (PathStrategy.DAILY.equals(strategy)) {
			timeFormat = new SimpleDateFormat("yyyyMMdd");
		} else {
			timeFormat = new SimpleDateFormat("yyyyMMddHH");
		}
	}

	public String getRootPath() {
		return rootPath;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public boolean isShowRealNameAsPrefix() {
		return showRealNameAsPrefix;
	}

	public void setShowRealNameAsPrefix(boolean showRealNameAsPrefix) {
		this.showRealNameAsPrefix = showRealNameAsPrefix;
	}

	public static void main(String[] args) {
		DefaultAttachmentPathResolver resolver = new DefaultAttachmentPathResolver();
		long startMs = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {
			System.out.println(resolver.resolvePath("test-bbb.ttt.png"));
		}
		System.out.println((System.currentTimeMillis() - startMs) + "ms");
	}
}
