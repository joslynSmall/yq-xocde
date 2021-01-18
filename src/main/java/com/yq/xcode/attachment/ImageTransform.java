package com.yq.xcode.attachment;


public class ImageTransform {
	
	public static ImageTransform cut(float x,float y,float width,float height,float maxImageWidth) {
		return new ImageCut(x,y,width,height,maxImageWidth);
	}
	
	public static ImageTransform scale(int w,int h,boolean keepProportion) {
		return new ImageScale(w,h,keepProportion);
	}
	public static class ImageCut extends ImageTransform{
		private float x,y,width,height,maxImageWidth;
		public ImageCut(float x,float y,float width,float height,float maxImageWidth) {
			this.x = x;
			this.y = y;
			this.height = height;
			this.width = width;
			this.maxImageWidth = maxImageWidth;
		}

		public float getX() {
			return x;
		}

		public float getY() {
			return y;
		}

		public float getWidth() {
			return width;
		}

		public float getHeight() {
			return height;
		}

		public float getMaxImageWidth() {
			return maxImageWidth;
		}
	}

	
	public static class ImageScale extends ImageTransform{
		private int width,height;
		private boolean keepProportion;
		public ImageScale(int width,int height,boolean keepProportion) {
			this.height = height;
			this.width = width;
			this.keepProportion = keepProportion;
		}
		public int getWidth() {
			return width;
		}
		public int getHeight() {
			return height;
		}
		public boolean isKeepProportion() {
			return keepProportion;
		}
		
		
	}
	
}
