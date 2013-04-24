package com.example.picturemanagerdemo.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class ViewPageAdapter extends PagerAdapter {
	private List<View> views;

	public ViewPageAdapter(Context context, List<View> views){
		this.views = views;
	}
	
	@Override
	public int getCount() {
		return views.size();
	}
	
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	public View getItem(int position){
		return views.get(position);
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager)container).removeView(views.get(position));
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		((ViewPager)container).addView(views.get(position));
		return views.get(position);
	}

}
