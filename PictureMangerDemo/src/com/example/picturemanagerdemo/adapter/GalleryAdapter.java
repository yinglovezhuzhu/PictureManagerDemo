/*
 * 文件名：GalleryAdapter.java
 * 版权：<版权>
 * 描述：<描述>
 * 创建人：xiaoying
 * 创建时间：2013-5-28
 * 修改人：xiaoying
 * 修改时间：2013-5-28
 * 版本：v1.0
 */

package com.example.picturemanagerdemo.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;

import com.example.picturemanagerdemo.widget.ImageViewTouchBase;

/**
 * 功能：
 * @author xiaoying
 *
 */
@SuppressWarnings("deprecation")
public class GalleryAdapter extends BaseAdapter {

	private Context mmContext;
	
	private List<Bitmap> mmBitmaps = new ArrayList<Bitmap>();
	
	public GalleryAdapter(Context context) {
		this.mmContext = context;
	}

	public void addAll(List<Bitmap> bitmaps) {
		mmBitmaps.addAll(bitmaps);
		notifyDataSetChanged();
	}
	
	public void add(Bitmap bitmap) {
		mmBitmaps.add(bitmap);
	}
	
	public void set(int position, Bitmap bitmap) {
		mmBitmaps.set(position, bitmap);
	}
	
	@Override
	public int getCount() {
		return mmBitmaps.size();
	}
	@Override
	public Bitmap getItem(int position) {
		return mmBitmaps.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageViewTouchBase view = new ImageViewTouchBase(mmContext, 540, 960);
		Gallery.LayoutParams lp = new Gallery.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		view.setLayoutParams(lp);
		view.setImageBitmap(mmBitmaps.get(position));
		return view;
	}
}
