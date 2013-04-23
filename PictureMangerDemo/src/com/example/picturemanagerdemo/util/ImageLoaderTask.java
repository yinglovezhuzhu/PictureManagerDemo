package com.example.picturemanagerdemo.util;

import java.lang.ref.WeakReference;

import com.example.picturemanagerdemo.entity.Image;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

public class ImageLoaderTask extends AsyncTask<Image, Void, Bitmap> {

	private final WeakReference<ImageView> mImageViewReference; // ·ÀÖ¹ÄÚ´æÒç³ö
	
	private int mColumns = 4;
	

	public ImageLoaderTask(ImageView imageView, int columns) {
		mImageViewReference = new WeakReference<ImageView>(imageView);
		this.mColumns = columns;
	}

	@Override
	protected Bitmap doInBackground(Image... params) {
		return BitmapCache.getInstance().getBitmap(params[0], mColumns);
	}

	@Override
	protected void onPostExecute(Bitmap bitmap) {
		if (isCancelled()) {
			bitmap = null;
		}

		if (mImageViewReference != null) {
			ImageView imageView = mImageViewReference.get();
			if (imageView != null && bitmap != null) {
				imageView.setImageBitmap(bitmap);
			}
		}
	}
}