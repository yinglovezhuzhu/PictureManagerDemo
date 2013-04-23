package com.example.picturemanagerdemo.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.picturemanagerdemo.ImageApplication;
import com.example.picturemanagerdemo.R;
import com.example.picturemanagerdemo.entity.Image;
import com.example.picturemanagerdemo.util.ImageLoaderTask;

public class ImageFolderAdapter extends BaseAdapter {

	private static final int NUM_OF_COLUMN = 2;
	
	private Context mContext;
	private List<Image> mFolders = new ArrayList<Image>();
	
	public ImageFolderAdapter(Context context) {
		this.mContext = context;
	}
	
	public void addAll(List<Image> folders) {
		mFolders.addAll(folders);
		notifyDataSetChanged();
	}
	
	public void add(Image folder) {
		mFolders.add(folder);
		notifyDataSetChanged();
	}
	
	public void remove(int position) {
		mFolders.remove(position);
		notifyDataSetChanged();
	}
	
	public void clear() {
		mFolders.clear();
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return mFolders.size();
	}

	@Override
	public Image getItem(int position) {
		return mFolders.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHoder = null;
		if(convertView == null) {
			viewHoder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_folder_list, null);
			viewHoder.image = (ImageView) convertView.findViewById(R.id.iv_folder_image);
			viewHoder.title = (TextView) convertView.findViewById(R.id.tv_folder_name);
			int columnWidth = ImageApplication.mScreenWidth / NUM_OF_COLUMN;
//			viewHoder.image.setLayoutParams(new LinearLayout.LayoutParams(columnWidth, columnWidth));
			convertView.setLayoutParams(new AbsListView.LayoutParams(columnWidth, columnWidth));
			convertView.setTag(viewHoder);
		} else {
			viewHoder = (ViewHolder) convertView.getTag();
		}
		Image folder = (Image) getItem(position);
		new ImageLoaderTask(viewHoder.image, NUM_OF_COLUMN).execute(folder);
		viewHoder.title.setText(folder.getBucketDisplayName());
		return convertView;
	}

	private class ViewHolder {
		ImageView image;
		TextView title;
	}
}
