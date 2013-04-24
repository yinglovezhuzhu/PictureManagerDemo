package com.example.picturemanagerdemo.util;

import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.example.picturemanagerdemo.entity.Image;

public class BitmapLoad extends AsyncTask<Image, Void, WeakReference<Bitmap>>{
private final WeakReference<ImageView> mImageViewReference; // ·ÀÖ¹ÄÚ´æÒç³ö
	
	private int mColumns = 4;
	
	private int mId = 0;
	

	public BitmapLoad(int id, ImageView imageView, int columns) {
		mImageViewReference = new WeakReference<ImageView>(imageView);
		this.mColumns = columns;
		this.mId = id;
	}

	@Override
	protected WeakReference<Bitmap> doInBackground(Image... params) {
//		params[0].pic = new WeakReference<Bitmap>(BitmapCache.getInstance().getBitmap(params[0], mColumns));
		params[0].pic = new WeakReference<Bitmap>(BitmapLoadUtil.loadBitmap(params[0]));
		return params[0].pic;
	}

	@Override
	protected void onPostExecute(WeakReference<Bitmap> bitmap) {
		if (mImageViewReference != null) {
			ImageView imageView = mImageViewReference.get();
			if (imageView != null && bitmap != null) {
				if((Integer) imageView.getTag() == this.mId && bitmap.get() != null) {
					imageView.setImageBitmap(bitmap.get());
				}
			}
		}
	}
}
