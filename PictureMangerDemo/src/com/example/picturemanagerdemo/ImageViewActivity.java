package com.example.picturemanagerdemo;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.spec.PSource;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.example.picturemanagerdemo.adapter.ViewPageAdapter;
import com.example.picturemanagerdemo.db.util.ImageDatabaseUtil;
import com.example.picturemanagerdemo.entity.Image;
import com.example.picturemanagerdemo.util.BitmapLoad;
import com.example.picturemanagerdemo.util.ImageLoaderTask;
import com.example.picturemanagerdemo.util.LogUtil;

public class ImageViewActivity extends Activity {
	
	private String tag = ImageViewActivity.class.getSimpleName();
	
	private ViewPager mViewPage = null;
	
	private List<View> mViews = new ArrayList<View>();
	
	private ViewPageAdapter mAdapter;
	
	private List<Image> mImages = new ArrayList<Image>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_view);
		
		Intent intent = getIntent();
		Image image = (Image) intent.getSerializableExtra("image_info");
		int imageIndex = intent.getIntExtra("image_index", 0);
		setTitle(image.getDisplayName());
		
		mViewPage = (ViewPager) findViewById(R.id.view_pager);
		
		mViewPage.setOnPageChangeListener(mPageChangeListener);
		
		mImages.addAll(ImageDatabaseUtil.getImageAsFolder(this, image.getBucketId()));
		
		for (int i = 0; i < mImages.size(); i++) {
			ImageView iv = new ImageView(this);
			iv.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			iv.setScaleType(ScaleType.CENTER_INSIDE);
			iv.setBackgroundColor(getResources().getColor(R.color.white));
			iv.setTag(i);
			if(i > imageIndex - 2 && i < imageIndex + 2) {
				new BitmapLoad(i, iv, 1).execute(mImages.get(i));
			}
			mViews.add(iv);
		}
		mAdapter = new ViewPageAdapter(this, mViews);
		mViewPage.setAdapter(mAdapter);
		mViewPage.setCurrentItem(imageIndex);
	}
	
	private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
		
		private int mmLastPosition = 0;
		
		@Override
		public void onPageSelected(int position) {
			LogUtil.w(tag, "Page " + position + " is selected++++++++++>>>>LastPosition" + mmLastPosition);
			if(mmLastPosition < position) {
				if(mmLastPosition > 0 && mImages.get(mmLastPosition - 1).pic != null) {
					if(mImages.get(mmLastPosition - 1).pic.get() != null) {
						mImages.get(mmLastPosition - 1).pic.get().recycle();
					}
				}
				if(position < mImages.size() - 1) {
					new BitmapLoad(position + 1,  (ImageView) mViews.get(position + 1), 1).execute(mImages.get(position + 1));
				}
			} else {
				if(mmLastPosition < mImages.size() - 1 && mImages.get(mmLastPosition + 1).pic != null) {
					if(mImages.get(mmLastPosition + 1).pic.get() != null) {
						mImages.get(mmLastPosition + 1).pic.get().recycle();
					}
				}
				if(position > 0) {
					new BitmapLoad(position - 1,  (ImageView) mViews.get(position - 1), 1).execute(mImages.get(position - 1));
				}
			}
			mmLastPosition = position;
			
			mAdapter.notifyDataSetChanged();
		}
		
		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onPageScrollStateChanged(int state) {
			// TODO Auto-generated method stub
			
		}
	};
}
