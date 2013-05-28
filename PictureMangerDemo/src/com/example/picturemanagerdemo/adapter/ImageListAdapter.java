package com.example.picturemanagerdemo.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.picturemanagerdemo.ImageApplication;
import com.example.picturemanagerdemo.R;
import com.example.picturemanagerdemo.entity.Image;
import com.example.picturemanagerdemo.util.ImageLoaderTask;

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
		ViewHoder viewHoder = null;
		if(null == convertView) {
			viewHoder = new ViewHoder();
			convertView = View.inflate(mContext, R.layout.item_image_list, null);
			viewHoder.image = (ImageView) convertView.findViewById(R.id.iv_image);
			int columnWidth = ImageApplication.mScreenWidth / NUM_OF_COLUMN;
			convertView.setLayoutParams(new AbsListView.LayoutParams(columnWidth, columnWidth));
			convertView.setTag(viewHoder);
		} else {
			viewHoder = (ViewHoder) convertView.getTag();
		}
		viewHoder.image.setTag(position);
		viewHoder.image.setImageResource(R.drawable.ic_launcher);
		Image tmp = getItem(position);
		new ImageLoaderTask(position, viewHoder.image, NUM_OF_COLUMN).execute(tmp);
		return convertView;
	}
	
	private class ViewHoder {
		ImageView image;
	}
}
