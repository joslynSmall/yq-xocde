package com.yq.xcode.attachment;


import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.swing.SwingConstants;

import com.yq.xcode.common.exception.ValidateException;
import com.yq.xcode.common.utils.CommonUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.yq.xcode.attachment.TransformParameter.ScaleFitType;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileSystemAttachmentManager implements AttachmentManager, InitializingBean {

	private static Log LOG = LogFactory.getLog(FileSystemAttachmentManager.class);
	@Resource
	private AttachmentPathResolver attachmentPathResolver;
	
	@Value("${static.path}")
    private String pattern; 
	
	private File rootDirectory;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		rootDirectory = new File(pattern);
	}
	@Override
    public Attachment addFile(String directory, String name, byte[] data) {
            String noExtName = name;
            String extension = null;
            int lastDotIndex = name.lastIndexOf('.');
            if (lastDotIndex > 0) {
                    noExtName = name.substring(0, lastDotIndex);
                    extension = name.substring(lastDotIndex + 1);
            }
            String noExtPath = mergePath(directory, attachmentPathResolver.resolvePath(noExtName));
            File dest = null;
            try {
                    LOG.info(rootDirectory);
                    dest = new File(rootDirectory, noExtPath + "." + extension);
                    foreCreateNewFile(dest);
                    FileUtils.writeByteArrayToFile(dest, data);
            } catch (Exception e) {
                    LOG.error(e);
                    return null;
            }
            ImageAttachment attachment = new ImageAttachment();
            attachment.setName(noExtName + "." + extension);
            attachment.setOriginalSize(dest.length());
            attachment.setType(extension);
            attachment.setOrginalPath(noExtPath.replace(directory, "/") + "." + extension);
            if (attachment.getPath() == null) {
                    attachment.setPath(attachment.getOrginalPath().replace(directory, "/"));
                    attachment.setSize(attachment.getOriginalSize());
            }
            return attachment;
    }
	@Override
	public Attachment addImage(String directory, String name, byte[] data, ImageTransform orginTransform,
			ImageTransform standardTransorm, ImageTransform thumbnailTransform) {
		String noExtName = name;
		String extension = null;
		int lastDotIndex = name.lastIndexOf('.');
		if (lastDotIndex > 0) {
			noExtName = name.substring(0, lastDotIndex);
			extension = name.substring(lastDotIndex + 1);
		}
		String noExtPath = mergePath(directory, attachmentPathResolver.resolvePath(noExtName));
		File dest = null;
		try {
			LOG.info(rootDirectory);
			dest = new File(rootDirectory, noExtPath + "." + extension);
			foreCreateNewFile(dest);
			FileUtils.writeByteArrayToFile(dest, data);
		} catch (Exception e) {
			LOG.error(e);
			return null;
		}
		ImageAttachment attachment = new ImageAttachment();
		attachment.setName(noExtName + "." + extension);
		attachment.setOriginalSize(dest.length());
		attachment.setType(extension);
		attachment.setOrginalPath(noExtPath.replace(directory, "/") + "." + extension);
		try {
			Dimension dimension = getImageSize(dest);
			attachment.setOriginalWidth(dimension.width);
			attachment.setOriginalHeight(dimension.height);
		} catch (IOException e) {
			LOG.error(e);
			return null;
		}

		if (standardTransorm != null) {
			File standardDest = new File(rootDirectory, noExtPath + ".standard.png");
			try {
				if (transformImage(dest, standardDest, standardTransorm)) {
					attachment.setPath(noExtPath + ".standard.png");
					Dimension dimension = getImageSize(dest);
					attachment.setWidth(dimension.width);
					attachment.setHeight(dimension.height);
					attachment.setSize(dest.length());
				} else {
					standardDest.delete();
				}
			} catch (Exception e) {
				standardDest.delete();
				LOG.error(e);
			}
		}
		if (thumbnailTransform != null) {
			File thumbnailDest = new File(rootDirectory, noExtPath + ".thumbnail.png");
			try {
				if (transformImage(dest, thumbnailDest, thumbnailTransform)) {
					attachment.setThumbnailPath(noExtPath.replace(directory, "/") + ".thumbnail.png");
					Dimension dimension = getImageSize(dest);
					attachment.setThumbnailWidth(dimension.width);
					attachment.setThumbnailHeight(dimension.height);
					attachment.setThumbnailSize(dest.length());
				} else {
					thumbnailDest.delete();
				}
			} catch (Exception e) {
				thumbnailDest.delete();
				LOG.error(e);
			}
		}
		if (attachment.getPath() == null) {
			attachment.setPath(attachment.getOrginalPath().replace(directory, "/"));
			attachment.setWidth(attachment.getOriginalWidth());
			attachment.setHeight(attachment.getOriginalHeight());
			attachment.setSize(attachment.getOriginalSize());
		}
		return attachment;
	}
	
	@Override
	public void removeAttachment(String path) {
		if(!StringUtils.hasText(path) || !attachmentPathResolver.isAttachmentPath(path)) {
			return;
		}
		if(path.indexOf("..") >= 0) {
			throw new IllegalArgumentException("Cannot Delete attachment. Illegal Path: "+path);
		}
		try{
			
			File deleteFile = new File(rootDirectory,path);
			if(!deleteFile.exists() || deleteFile.isDirectory()) {
				return;
			}
			FileUtils.forceDelete(new File(rootDirectory,path));
		}catch(IOException e) {
			throw new RuntimeException(e);
		}
	}

	private String mergePath(String path1, String path2) {
		if (path1 == null) {
			return path2;
		}
		StringBuffer buffer = new StringBuffer(path1);
		if (buffer.charAt(buffer.length() - 1) == '/') {
			if (path2.charAt(0) == '/') {
				buffer.append(path2.substring(1));
			} else {
				buffer.append(path2);
			}
		} else {
			if (path2.charAt(0) == '/') {
				buffer.append(path2);
			} else {
				buffer.append('/').append(path2);
			}
		}
		return buffer.toString();
	}

	private static void foreCreateNewFile(File file) throws IOException {
		if (!file.exists()) {
			FileUtils.forceMkdir(file.getParentFile());
			file.createNewFile();
		}
	}

	private boolean transformImage(byte[] src, File dest, ImageTransform imageTransform) throws IOException {
		if (imageTransform instanceof ImageTransform.ImageCut) {
			cutImage(new ByteArrayInputStream(src), dest, (ImageTransform.ImageCut) imageTransform);
			return true;
		} else {
			ImageTransform.ImageScale scale = (ImageTransform.ImageScale) imageTransform;
			return ImageProcessor.compressImage(new ByteArrayInputStream(src), FileUtils.openOutputStream(dest),
					scale.getWidth(), scale.getHeight(), scale.isKeepProportion());
		}
	}

	private boolean transformImage(File src, File dest, ImageTransform imageTransform) throws IOException {
		if (imageTransform instanceof ImageTransform.ImageCut) {
			cutImage(src, dest, (ImageTransform.ImageCut) imageTransform);
			return true;
		} else {
			ImageTransform.ImageScale scale = (ImageTransform.ImageScale) imageTransform;
			// return ImageProcessor.compressImage(src, dest, scale.getWidth(),
			// scale.getHeight(), scale.isKeepProportion());
			TransformParameter.Scale tp = TransformParameter.scale(scale.getWidth(), scale.getHeight(),
					scale.isKeepProportion() ? TransformParameter.ScaleFitType.NONE : ScaleFitType.CUT,
					SwingConstants.CENTER, true, false);
			return ImageProcessor.transformImage(src, dest, tp);
		}
	}

	private void cutImage(File src, File dest, ImageTransform.ImageCut imageTransform) throws IOException {
		cutImage(FileUtils.openInputStream(src), dest, imageTransform);
	}

	private void cutImage(InputStream in, File dest, ImageTransform.ImageCut imageTransform) throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(dest);
		try {
			Image image = ImageIO.read(in);
//			int imageWidth = image.getWidth(null);
//			int imageHeight = image.getHeight(null);
			float x = imageTransform.getX();
			float y = imageTransform.getY();
			float w = imageTransform.getWidth();
			float h = imageTransform.getHeight();
			float destW = w;
			float destH = h;
			if (w > imageTransform.getMaxImageWidth()) {
				double factor = imageTransform.getMaxImageWidth() * 1d / w;
				destW = imageTransform.getMaxImageWidth();
				destH = (int) (Math.round(h * factor));
			}
			BufferedImage bufferedImage = new BufferedImage((int) destW, (int) destH, BufferedImage.TYPE_INT_RGB);
			bufferedImage.getGraphics().drawImage(image, 0, 0, (int) destW, (int) destH, (int) x, (int) y,
					(int) (w + x), (int) (h + y), null);
			ImageIO.write(bufferedImage, "png", fileOutputStream);

		} catch (Exception e) {
			e.printStackTrace();
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

	private Dimension getImageSize(File src) throws IOException {
		Image image = ImageIO.read(src);
		int imageWidth = image.getWidth(null);
		int imageHeight = image.getHeight(null);
		return new Dimension(imageWidth, imageHeight);

	}

	/**
	 * 转换接收文件( multipartFileFile)为临时文件(file)
	 * @param  multipartFileFile
	 * @return File
	 */
	@Override
	public File multipartFileToFile(MultipartFile multipartFileFile) throws Exception{

		if (CommonUtil.isNull( multipartFileFile)) {
			throw new ValidateException("请上传有效文件！");
		}
		String fileName =  multipartFileFile.getOriginalFilename();
		String prefix = fileName.substring(fileName.lastIndexOf("."));
		File file = File.createTempFile(System.currentTimeMillis() + "", prefix);
		multipartFileFile.transferTo(file);
		return file;
	}

	/**
	 * 删除文件
	 * @param files
	 */
	@Override
	public void deleteFile(File... files) throws Exception{

		for (File file : files) {
			if (file.exists()) {
				file.delete();
			}
		}
	}
	@Override
	public File getAttachmentAsFile(String path) {
		return new File(rootDirectory,"/static/"+path);
	}
 
}
