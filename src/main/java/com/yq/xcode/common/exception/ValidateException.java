package com.yq.xcode.common.exception;

public class ValidateException extends RuntimeException {

	private static final long serialVersionUID = -7067506919151673199L;
	public ValidateException() {
		super();
	}
	
	public ValidateException(String msg) {
		super(msg);
	}
	
	public ValidateException(Throwable cause) {
		super(cause);
	}
	
	public ValidateException(String msg, Throwable cause) {
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
	public ValidateException(String msg, String qaCode) {
		super(msg+changToStr(qaCode));
		
		
		
		
	}
	
}
