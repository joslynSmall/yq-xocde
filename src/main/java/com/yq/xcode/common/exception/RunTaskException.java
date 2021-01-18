package com.yq.xcode.common.exception;

import java.util.List;

public class RunTaskException extends RuntimeException {

	private static final long serialVersionUID = -7067506919151673199L;
	private List<String> msgList = null;
	public RunTaskException() {
		super();
	}
	
	public RunTaskException(String msg) {
		super(msg);
	}
	
	public RunTaskException(String msg,List<String> msgList) {
		super(msg);
		this.msgList = msgList;
	}
	
	public RunTaskException(Throwable cause) {
		super(cause);
	}
	
	public RunTaskException(Throwable cause,List<String> msgList) {
		super(cause);
		this.msgList = msgList;
	}
	
	public RunTaskException(String msg, Throwable cause) {
		super(msg, cause);
	}
	private static String changToStr(String qaCode){
		String qaString=new String();
		for(String [] temp:QuestionAndAnswerArray.questionAndAnswerArray){
			if(temp[0].equals(qaCode)){
				qaString="\n请根据下列错误排查:";
				qaString+="\n"+temp[1]+"";
				
			}
		}
		return qaString;
	}
	
	public RunTaskException(String msg, String qaCode) {
		super(msg+changToStr(qaCode)); 
	}

	public List<String> getMsgList() {
		return msgList;
	}

	public void setMsgList(List<String> msgList) {
		this.msgList = msgList;
	} 
	
}
