package com.yq.xcode.attachment;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.swing.SwingConstants;

public class ImageProcessor {
	
	
	public final static String IMAGE_TYPE_PNG = "png";
	public final static String IMAGE_TYPE_JPG = "jpg";
	
	public static Dimension getImageSize(File file) throws IOException{
		Image image = ImageIO.read(file);  
		return new Dimension(image.getWidth(null),image.getHeight(null));
	}
	
	public static boolean compressImage(File src,File dest,int width,int height,boolean proportion) throws IOException{
		return compressImage(src,dest,width,height,proportion,true);
	}
	
	public static boolean compressImage(InputStream in,OutputStream out,int width,int height,boolean proportion) throws IOException{
		return compressImage(in,out,width,height,proportion,true);
	}

	public static boolean compressImage(File src,File dest,int width,int height,boolean proportion,boolean shrinkOnly) throws IOException{
		return compressImage(new FileInputStream(src),new FileOutputStream(dest),width,height,proportion,shrinkOnly);
	}
	
	public static boolean compressImage(InputStream in,OutputStream out,int width,int height,boolean proportion,boolean shrinkOnly) throws IOException{
		return compressImage(in,out, width, height, proportion, shrinkOnly,IMAGE_TYPE_PNG);
	}
	
	public static boolean compressImage(InputStream in,OutputStream out,int width,int height,boolean proportion,boolean shrinkOnly,String formatName) throws IOException{
		    try {  
	            Image image = ImageIO.read(in);  
	            if(image == null) {
	            	throw new IOException("Read Image from in Failed");
	            }
	            int imageWidth = image.getWidth(null);
	            int imageHeight = image.getHeight(null);
	            int newWidth = 0;  
	            int newHeight = 0;  
	            if (imageWidth != width || imageHeight != height) {  
	                if (proportion) {  
	                    double rate1 = width==0?0:(imageWidth *1f / width);  
	                    double rate2 = height==0?0:(imageHeight * 1f / height);  
	                    double rate = rate1 > rate2 ? rate1 : rate2;  
	                    if(rate < 1 && shrinkOnly) {
	                    	rate = 1;
	                    }
	                    newWidth = (int)(Math.round(imageWidth / rate));  
	                    newHeight = (int)(Math.round(imageHeight / rate));  
	                } else if(width == 0 || height == 0){
	                	newWidth = imageWidth;  
	                    newHeight = imageHeight; 
	                } else {
	                    newWidth = width;  
	                    newHeight = height;  
	                }  
	            } else {  
	                newWidth = imageWidth;  
	                newHeight = imageHeight;  
	            }  
	            if(newWidth == imageWidth && newHeight == imageHeight) {
	            	return false;
	            }else {
	            	 BufferedImage bufferedImage = new BufferedImage(newWidth,  
	 	                    newHeight, IMAGE_TYPE_PNG.equalsIgnoreCase(formatName)?BufferedImage.TYPE_INT_ARGB: BufferedImage.TYPE_INT_RGB);  
	 	            bufferedImage.getGraphics().drawImage(  
	 	                    image.getScaledInstance(newWidth, newHeight,  
	 	                            Image.SCALE_SMOOTH), 0, 0, null);  
	 	            ImageIO.write(bufferedImage, formatName==null?IMAGE_TYPE_PNG:formatName, out);
	 	           out.flush();
	 	            return true;
	            }
	        } finally {  
	            if (out != null) {  
	                try {  
	                	out.close();  
	                } catch (Exception e) {  
	                    e.printStackTrace();  
	                }  
	            }  
	        }  
	}
	
	public static boolean compressImage(File in,File out,TransformParameter.Scale sp) throws IOException{
		return compressImage(new FileInputStream(in),new FileOutputStream(out),sp,getImageFormatNameOfFile(out));
	}
	
	public static boolean compressImage(InputStream in,OutputStream out,TransformParameter.Scale sp,String formatName) throws IOException{
	    if(formatName == null || formatName.equals("")) {
	    	formatName = IMAGE_TYPE_PNG;
	    }
		try {  
            Image image = ImageIO.read(in);  
            if(image == null) {
            	throw new IOException("Read Image from in Failed");
            }
            int dWidth = sp.getWidth();
            int dHeight = sp.getHeight();
            int dImgWidth = dWidth;
            int dImgHeight = dHeight;
            int oWidth = image.getWidth(null);
            int oHeight = image.getHeight(null);
            if(oWidth == dWidth && oHeight == dHeight){
            	return false;
            }
            if(sp.isShrinkOnly() && dWidth >= oWidth  && dHeight >= oHeight) {
            	return false;
            }
            if(dWidth == 0) {
    			dWidth = dHeight;
    		}else if(dHeight == 0) {
    			dHeight = dWidth;
    		}
            int dX = 0,dY = 0;
            int sX = 0,sY = 0,sWidth = oWidth,sHeight = oHeight;
           
        	switch(sp.getFitType()) {
        	case CUT:
        		double scaleX = ((double)oWidth)/dWidth;
        		double scaleY = ((double)oHeight)/dHeight;
        		double scale = scaleX==0?scaleY:(scaleY==0?scaleX:Math.min(scaleX, scaleY));
        		if(scale < 1 && sp.isShrinkOnly()) {
        			scale = 1;
        		}
//        		System.out.println("ScaleX:"+scaleX+", scaleY: "+scaleY+", scale: "+scale);
        		dWidth = (!sp.isShrinkOnly()||scaleX>=1)?dWidth:(int)(dWidth * scaleX);
        		dHeight = (!sp.isShrinkOnly()||scaleY>=1)?dHeight:(int)(dHeight * scaleY);
        		sWidth = Math.min((int)(dWidth*scale),oWidth);
        		sHeight = Math.min((int)(dHeight*scale),oHeight);
        		int xDif = oWidth - dWidth;
        		int yDif = oHeight - dHeight;
        		if(SwingConstants.CENTER == sp.getCutAlignment()) {
        			if(xDif > 0) {
        				sX = (int)((oWidth - dWidth* scale)/2);
        			}else if(xDif < 0) {
        				dX = -xDif/2;
        			}
        			if(yDif > 0) {
        				sY = (int)((oHeight - dHeight* scale)/2);
        			}else if(xDif < 0) {
        				dY = -yDif/2;
        			}
        		}else if(SwingConstants.RIGHT == sp.getCutAlignment()) {
        			if(xDif > 0) {
        				sX = (int)((oWidth - dWidth* scale));
        			}else if(xDif < 0) {
        				dX = -xDif;
        			}
        			if(yDif > 0) {
        				sY = (int)((oHeight - dHeight* scale));
        			}else if(xDif < 0) {
        				dY = -yDif;
        			}
        		}
        		break;
        	case FILL:
        		break;
        	case NONE:
        		scaleX = ((double)oWidth)/dWidth;
        		scaleY = ((double)oHeight)/dHeight;
        		scale = scaleX==0?scaleY:(scaleY==0?scaleX:Math.max(scaleX, scaleY));
        		if(scale < 1 && sp.isShrinkOnly()) {
        			scale = 1;
        		}
//        		System.out.println("ScaleX:"+scaleX+", scaleY: "+scaleY+", scale: "+scale);
        		dWidth = (int)(sWidth / scale);
        		dHeight = (int)(sHeight / scale);
        		if(sp.isAlwaysUseDestSize()) {
        			xDif = (int)(dImgWidth - oWidth / scale);
            		yDif = (int)(dImgHeight - oHeight / scale);
//            		System.out.println("xDif:"+xDif+", yDif: "+yDif);
            		if(SwingConstants.CENTER == sp.getCutAlignment()) {
            			if(xDif > 0) {
            				dX = xDif/2;
            			}
            			if(yDif > 0) {
            				dY = yDif/2;
            			}
            		}else if(SwingConstants.RIGHT == sp.getCutAlignment()) {
            			if(xDif > 0) {
            				dX = xDif;
            			}
            			if(yDif > 0) {
            				dY = yDif;
            			}
            		}
        		}else {
        			dImgWidth = dWidth;
        			dImgHeight = dHeight;
        		}
        		
        	}
        	int imgColorType = IMAGE_TYPE_PNG.equalsIgnoreCase(formatName)?BufferedImage.TYPE_INT_ARGB: BufferedImage.TYPE_INT_RGB;
            BufferedImage bufferedImage = new BufferedImage(dImgWidth,  dImgHeight, imgColorType);  
            if(BufferedImage.TYPE_INT_RGB == imgColorType) {
            	bufferedImage.getGraphics().setColor(Color.WHITE);
            	bufferedImage.getGraphics().fillRect(0, 0, dImgWidth, dImgHeight);
            }
//            System.out.print("imageSize:("+dImgWidth+","+dImgHeight+"),src("+sX+","+sY+","+sWidth+","+sHeight+") dest("+dX+","+dY+","+dWidth+","+dHeight+")");
            bufferedImage.getGraphics().drawImage(image, dX, dY, dX+dWidth, dY+dHeight, sX, sY, sX+sWidth, sY+sHeight, null);
            ImageIO.write(bufferedImage, formatName==null?IMAGE_TYPE_PNG:formatName, out);
            out.flush();
            return true;
            
        } finally {  
            if (out != null) {  
                try {  
                	out.close();  
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
}
	
	public static void cutImage(File src,File dest,float x,float y,float w,float h,int maxWidth ) throws IOException{
		 FileOutputStream fileOutputStream = new FileOutputStream(dest);  
	        try {  
	            Image image = ImageIO.read(src);  
	            int imageWidth = image.getWidth(null);
	            int imageHeight = image.getHeight(null);
	            if(imageWidth > maxWidth) {
	            	double factor = imageWidth * 1d/maxWidth ;
	            	x = (int)(Math.round(x * factor));
	            	y = (int)(Math.round(y * factor));
	            	w = (int)(Math.round(w * factor));
	            	h = (int)(Math.round(h * factor));
	            }
	            String formatName = getImageFormatNameOfFile(dest);
	            BufferedImage bufferedImage = new BufferedImage((int)w, (int)h,  IMAGE_TYPE_PNG.equals(formatName)?BufferedImage.TYPE_INT_ARGB: BufferedImage.TYPE_INT_RGB);  
	            bufferedImage.getGraphics().drawImage( image, -(int)x, -(int)y, null);  
	            ImageIO.write(bufferedImage,IMAGE_TYPE_JPG.equals(formatName)?IMAGE_TYPE_JPG:IMAGE_TYPE_PNG, fileOutputStream);
	        } catch (IOException e) {  
	            throw e;
	        } finally {  
	            if (fileOutputStream != null) {  
	                try {  
	                    fileOutputStream.close();  
	                } catch (Exception e) {  
	                    e.printStackTrace();  
	                }  
	            }  
	        }  
	}
	
	public static boolean transformImage(File src,File dest,TransformParameter tp) throws IOException{
		 if(tp instanceof TransformParameter.Cut) {
			 TransformParameter.Cut cut = (TransformParameter.Cut)tp;
			 cutImage(src,dest,cut.getX(),cut.getY(),cut.getWidth(),cut.getHeight(),cut.getMaxImageWidth());
			 return true;
		 }else {
			 TransformParameter.Scale sp = (TransformParameter.Scale)tp;
			 return compressImage(src,dest,sp);
		 }
	}
	
	public static String getImageFormatNameByExtension(String extension) {
		return ("jpg".equalsIgnoreCase(extension)||"jpeg".equalsIgnoreCase(extension))?IMAGE_TYPE_JPG:(IMAGE_TYPE_PNG.equalsIgnoreCase(extension)?IMAGE_TYPE_PNG:null);
	}
	
	public static String getImageFormatNameOfFile(File file) {
		return getImageFormatNameByExtension(extensionOfFile(file));
	}
	
	public static String extensionOfFile(File file) {
		String name = file.getName();
		String extension = null;
		int lastDotIndex = name.lastIndexOf('.');
		if(lastDotIndex > 0) {
			extension = name.substring(lastDotIndex + 1);
		}
		return extension;
	}
	
	
	
	public static void main(String[] args) throws Exception {
		try{
			File src = new File("c:/dev/temp/banner-bg-v.jpg");
			TransformParameter.Scale sp = null;
//			sp = TransformParameter.scale(200, 200, TransformParameter.ScaleFitType.NONE, SwingConstants.RIGHT,true,true);
//			compressImage(src,new File("c:/dev/temp/banner-bg-200.jpg"),sp);
			sp = TransformParameter.scale(400, 400, TransformParameter.ScaleFitType.CUT, SwingConstants.CENTER,true,false);
			compressImage(src,new File("c:/dev/temp/banner-bg-200.jpg"),sp);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
