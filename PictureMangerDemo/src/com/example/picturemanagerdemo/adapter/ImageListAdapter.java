package com.example.picturemanagerdemo.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.example.picturemanagerdemo.ImageApplication;
import com.example.picturemanagerdemo.R;
import com.example.picturemanagerdemo.entity.Image;
import com.example.picturemanagerdemo.util.ImageLoaderTask;
import com.example.picturemanagerdemo.util.LogUtil;

public class ImageListAdapter extends BaseAdapter {
	
	private Context mContext;
	
	private List<Image> mImages = new ArrayList<Image>();
	
	private static final int NUM_OF_COLUMN = 3;

	public ImageListAdapter(Context context) {
		this.mContext = context;
	}
	
	public void add(Image image) {
		mImages.add(image);
		notifyDataSetChanged();
	}
	
	public void addAll(List<Image> images) {
		mImages.addAll(images);
		notifyDataSetChanged();
	}
	
	public void remove(int position) {
		mImages.remove(position);
		notifyDataSetChanged();
	}
	
	public void clear() {
		mImages.clear();
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return mImages.size();
	}

	@Override
	public Image getItem(int position) {
		return mImages.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LogUtil.w(ImageListAdapter.class.getSimpleName(), "position" + position + "=>>>" + parent.getChildCount());
				ImageView imageView = null;
		if(null == convertView) {
			imageView = new ImageView(mContext);
			int columnWidth = ImageApplication.mScreenWidth / NUM_OF_COLUMN;
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageView.setLayoutParams(new AbsListView.LayoutParams(columnWidth, columnWidth));
			convertView = imageView;
			convertView.setTag(imageView);
		} else {
			imageView = (ImageView) convertView.getTag();
		}
		imageView.setImageResource(R.drawable.ic_launcher);
		Image tmp = getItem(position);
		new ImageLoaderTask(imageView, NUM_OF_COLUMN).execute(tmp);
		return convertView;
	}
}
