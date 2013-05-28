package com.example.picturemanagerdemo.util;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.example.picturemanagerdemo.adapter.GalleryAdapter;
import com.example.picturemanagerdemo.entity.Image;

public class LoadBitmapTask extends AsyncTask<Image, Void, Bitmap>{
	
	
	private GalleryAdapter mAdapter;
	private int mColumns = 1;
	
	private int mPosition = 0;

	public LoadBitmapTask(GalleryAdapter adapter, int position, int columns) {
		this.mAdapter = adapter;
		this.mColumns = columns;
		this.mPosition = position;
	}

	@Override
	protected Bitmap doInBackground(Image... params) {
		return BitmapLoadUtil.loadBitmapFromFile(params[0], mColumns);
	}

	@Override
	protected void onPostExecute(Bitmap bitmap) {
			if (mAdapter != null && bitmap != null) {
				mAdapter.set(mPosition, bitmap);
			}
	}
}
