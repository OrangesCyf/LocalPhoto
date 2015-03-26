package com.example.photopicker.managers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * @author chengyanfang
 * 
 *         对系统图库进行文件或文件夹的获取
 * */
public class ImagePahManager {
	/**
	 * 获取系统图库文件夹路径
	 * */
	public static ArrayList<String> getImageMkdirs(Context context) {
		List<String> tempPath = new ArrayList<String>();
		Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		String projection[] = new String[] { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(uri, projection,
				null, null, null);
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			String filepath = cursor.getString(cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
			File file = new File(filepath);
			String parentPath = file.getParentFile().getAbsolutePath()
					.toLowerCase();
			tempPath.add(parentPath);
		}
		HashSet<String> setPath = new HashSet<String>(tempPath);
		return new ArrayList<String>(setPath);
	}

	/**
	 * 根据文件夹路径获取文件夹下的所有图片资源路径
	 * */
	public static ArrayList<String> getImagePaths(String mkdirPath) {
		ArrayList<String> imagesPathArray = new ArrayList<String>();
		File dir = new File(mkdirPath);
		if (dir.exists()) {
			File file[] = dir.listFiles();
			for (int i = 0; i < file.length; i++) {
				if (file[i].isFile()) {
					if (isImage(file[i].getAbsolutePath()))
						imagesPathArray.add(file[i].getAbsolutePath());
				}
			}
		}
		return imagesPathArray;
	}

	/**
	 * 根据文件夹路径获取文件夹下首张图片资源路径
	 * */
	public static String getFirstImagePathInFolder(String mkdirPath) {
		String firstImagePath = "";
		File dir = new File(mkdirPath);
		if (dir.exists()) {
			File file[] = dir.listFiles();
			for (int i = 0; i < file.length; i++) {
				if (file[i].isFile()) {
					if (isImage(file[i].getAbsolutePath())) {
						firstImagePath = file[i].getAbsolutePath();
						break;
					}

				}
			}
		}
		return firstImagePath;
	}

	/**
	 * 根据文件夹路径获取该路径下的图片数目
	 * */
	public static int getImageNumsWithFilepath(String mkdirPath) {
		int count = 0;
		File dir = new File(mkdirPath);
		if (dir.exists()) {
			File file[] = dir.listFiles();
			for (int i = 0; i < file.length; i++) {
				if (file[i].isFile()) {
					if (isImage(file[i].getAbsolutePath())) {
						count++;
					}
				}
			}
		}
		return count;
	}

	// 判断文件是否是图片类型
	private static boolean isImage(String imagePath) {
		boolean isImage = false;
		if (imagePath.endsWith(".png") || imagePath.endsWith(".PNG")
				|| imagePath.endsWith(".bmp") || imagePath.endsWith(".BMP")
				|| imagePath.endsWith(".jpg") || imagePath.endsWith(".JPG")
				|| imagePath.endsWith(".gif") || imagePath.endsWith(".GIF")
				|| imagePath.endsWith(".jpeg") || imagePath.endsWith(".JPEG"))
			isImage = true;
		return isImage;
	}
}
