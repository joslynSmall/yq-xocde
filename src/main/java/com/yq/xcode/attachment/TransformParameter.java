package com.yq.xcode.attachment;

import javax.swing.SwingConstants;

public class TransformParameter {
	
	public static enum ScaleFitType {
		NONE,FILL,CUT
	}
	
	public static TransformParameter cut(int x,int y,int width,int height,int maxImageWidth) {
		return new Cut(x,y,width,height,maxImageWidth);
	}
	
	public static TransformParameter scale(int w,int h,ScaleFitType scaleFitType) {
		return new Scale(w,h,scaleFitType);
	}
	
	public static TransformParameter.Scale scale(int w,int h,ScaleFitType scaleFitType,int cutAlignment) {
		return new Scale(w,h,scaleFitType,cutAlignment,true,false);
	}
	
	public static TransformParameter.Scale scale(int w,int h,ScaleFitType scaleFitType,int cutAlignment,boolean shrinkOnly,boolean alwaysUseDestSize) {
		return new Scale(w,h,scaleFitType,cutAlignment,shrinkOnly,alwaysUseDestSize);
	}
	
	public static class Cut extends TransformParameter{
		private int x,y,width,height,maxImageWidth;
		public Cut(int x,int y,int width,int height,int maxImageWidth) {
			this.x = x;
			this.y = y;
			this.height = height;
			this.width = width;
			this.maxImageWidth = maxImageWidth;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}

		public int getMaxImageWidth() {
			return maxImageWidth;
		}
	}

	
	public static class Scale extends TransformParameter{
		private boolean shrinkOnly = true;
		private boolean alwaysUseDestSize = false;
		private int width,height;
		private ScaleFitType fitType = ScaleFitType.FILL;
		private int cutAlignment = SwingConstants.CENTER;
		
		public Scale(int width,int height,ScaleFitType fitType) {
			this(width, height, fitType,SwingConstants.LEFT,true,false);
		}
		public Scale(int width,int height,ScaleFitType fitType,int cutAlignment,boolean shrinkOnly,boolean alwaysUseDestSize) {
			this.height = height;
			this.width = width;
			this.fitType = fitType;
			this.cutAlignment = cutAlignment;
			this.shrinkOnly =  shrinkOnly;
			this.alwaysUseDestSize = alwaysUseDestSize;
		}
		public int getWidth() {
			return width;
		}
		public int getHeight() {
			return height;
		}
		public ScaleFitType getFitType() {
			return fitType;
		}
		public int getCutAlignment() {
			return cutAlignment;
		}
		public boolean isShrinkOnly() {
			return shrinkOnly;
		}
		public boolean isAlwaysUseDestSize() {
			return alwaysUseDestSize;
		}
	}
	
}

