package com.example.picturemanagerdemo.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.picturemanagerdemo.ImageApplication;
import com.example.picturemanagerdemo.entity.Image;

public class BitmapLoadUtil {
	
	public static Bitmap loadBitmapFromFile(Image image, int columns) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inTempStorage = new byte[16 * 1024];
		int max = image.getWidth() > image.getHeight() ? image.getWidth() : image.getHeight();
		options.inSampleSize = max / (ImageApplication.mScreenWidth / columns) + 1;
		return BitmapFactory.decodeFile(image.getData(), options);
	}
}
