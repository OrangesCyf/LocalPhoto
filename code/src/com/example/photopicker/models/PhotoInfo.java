package com.example.photopicker.models;

import java.io.Serializable;

public class PhotoInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2544840988196483133L;
	private String FileName;
	private String FilePath;
	private boolean isSelected;

	public PhotoInfo() {
	}

	public PhotoInfo(String fileName, String filePath, int imageId,
			String pathFile, String pathAbsolute) {
		super();
		FileName = fileName;
		FilePath = filePath;
		ImageId = imageId;
		PathFile = pathFile;
		PathAbsolute = pathAbsolute;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public boolean getIsSelected() {
		return isSelected;
	}

	public String getFileName() {
		return FileName;
	}

	public void setFileName(String fileName) {
		FileName = fileName;
	}

	public String getFilePath() {
		return FilePath;
	}

	public void setFilePath(String filePath) {
		FilePath = filePath;
	}

	// original PhotoInfo class declaration;
	/**
	 * 
	 * 本地相册图片bean<br>
	 * {@link #ImageId}图片id<br>
	 * {@link #PathAbsolute} 绝对路径: /sdcard/xxx/xxx.jpg<br>
	 * {@link #file_path} 用于显示的路径 file://#PathAbsolute<br>
	 */
	private int ImageId;
	private String PathFile;
	private String PathAbsolute;

	public int getImageId() {
		return ImageId;
	}

	public void setImageId(int image_id) {
		this.ImageId = image_id;
	}

	public String getPathFile() {
		return PathFile;
	}

	public void setPathFile(String path_file) {
		this.PathFile = path_file;
	}

	public String getPathAbsolute() {
		return PathAbsolute;
	}

	public void setPathAbsolute(String path_absolute) {
		this.PathAbsolute = path_absolute;
	}
}
