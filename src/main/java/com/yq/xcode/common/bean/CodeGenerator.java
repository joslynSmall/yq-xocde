package com.yq.xcode.common.bean;

public class CodeGenerator {
	
	private String prefix;
	private String suffix;
	private int numberSize;
	private String sequenceId;
	
	public CodeGenerator() {
		super();
	}
	
	public CodeGenerator(String prefix, String suffix, int numberSize, String sequenceId) {
		super();
		this.prefix = prefix;
		this.suffix = suffix;
		this.numberSize = numberSize;
		this.sequenceId = sequenceId;
	}
	
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	public int getNumberSize() {
		return numberSize;
	}
	public void setNumberSize(int numberSize) {
		this.numberSize = numberSize;
	}
	public String getSequenceId() {
		return sequenceId;
	}
	public void setSequenceId(String sequenceId) {
		this.sequenceId = sequenceId;
	}
	
}
