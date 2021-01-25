package com.ztgg.ecommerce.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

public class ImageUtil {
	
	private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
	private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final Random r = new Random();

//	public static void main(String[] args) throws IOException {
//		Thumbnails.of(new File("/Users/youzhedou/Desktop/workspace/online-store/src/main/resources/test-images/bookstoreimg.png")).size(800, 600)
//				.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.png")), 0.25f)
//				.outputQuality(0.8f).toFile("/Users/youzhedou/Desktop/workspace/online-store/src/main/resources/test-images/newbookstoreimg.png");
//	}
//	
	/**
	 * process image input, return the path for processed image
	 * 
	 * @param thumbnailInputStream
	 * @param targetAddr
	 * @return 
	 */
	public static String generateThumbnail(InputStream thumbnailInputStream, String fileName, String targetAddr) {
		String realFileName = getRandomFileName();
		String extension = getFileExtension(fileName); //XXX.jpg
		makeDirPath(targetAddr);

		String relativeAddr = targetAddr + realFileName + extension;
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
		try {
			Thumbnails.of(thumbnailInputStream).size(800, 600)
					.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.png")), 0.25f)
					.outputQuality(0.8f).toFile(dest);
		} catch (IOException e) {
			throw new RuntimeException("generateThumbnail failed：" + e.toString());
		}
		return relativeAddr;
	}
	
	/**
	 * if storePath is a file, remove that file
	 * if storePath is a folder, remove entire folder
	 * 
	 * @param storePath
	 */
	public static void deleteFileOrPath(String storePath) {
		File fileOrPath = new File(PathUtil.getImgBasePath() + storePath);
		if (fileOrPath.exists()) {
			if (fileOrPath.isDirectory()) {
				File files[] = fileOrPath.listFiles();
				for (int i = 0; i < files.length; i++) {
					files[i].delete();
				}
			}
			fileOrPath.delete();
		}
	}
	
	/**
	 * private helper methods
	 */
	
	/**
	 * convert CommonsMultipartFile to File
	 * 
	 * @param cFile
	 * @return
	 */
	public static File transferCommonsMultipartFileToFile(CommonsMultipartFile cFile) {
		File newFile = new File(cFile.getOriginalFilename());
		try {
			cFile.transferTo(newFile);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return newFile;
	}
	
	private static String getRandomFileName() {
		int rannum = r.nextInt(89999) + 10000;
		String nowTimeStr = sDateFormat.format(new Date());
		return nowTimeStr + rannum;
	}
	
	private static String getFileExtension(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}
	
	private static void makeDirPath(String targetAddr) {
		String realFileParentPath = PathUtil.getImgBasePath() + targetAddr;
		File dirPath = new File(realFileParentPath);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}
	}
}
