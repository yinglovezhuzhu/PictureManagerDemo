/*
 * �ļ�����GalleryAdapter.java
 * ��Ȩ��<��Ȩ>
 * ������<����>
 * �����ˣ�xiaoying
 * ����ʱ�䣺2013-5-28
 * �޸��ˣ�xiaoying
 * �޸�ʱ�䣺2013-5-28
 * �汾��v1.0
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
 * ���ܣ�
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
