package com.example.picturemanagerdemo.util;

import java.io.File;

public class FileUtil {

	private static String tag = FileUtil.class.getSimpleName();
	
	public static boolean deleteFile(String path) {
		File file = new File(path);
		if(file.exists()) {
			return file.delete();
		}
		LogUtil.e(tag, "File " + path + "dos not exits");
		return false;
	}
	
}
