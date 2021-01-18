package com.yq.xcode.common.exception;

public class CanNotRunTaskException extends RuntimeException {

	private static final long serialVersionUID = -7067506919151673199L;
	public CanNotRunTaskException() {
		super();
	}
	
	public CanNotRunTaskException(String msg) {
		super(msg);
	}
	
	public CanNotRunTaskException(Throwable cause) {
		super(cause);
	}
	
	public CanNotRunTaskException(String msg, Throwable cause) {
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
	public CanNotRunTaskException(String msg, String qaCode) {
		super(msg+changToStr(qaCode));
		
		
		
		
	}
	
}
