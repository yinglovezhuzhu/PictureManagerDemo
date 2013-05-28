package com.example.picturemanagerdemo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.example.picturemanagerdemo.adapter.GalleryAdapter;
import com.example.picturemanagerdemo.db.util.ImageDatabaseUtil;
import com.example.picturemanagerdemo.entity.Image;
import com.example.picturemanagerdemo.util.BitmapLoadUtil;
import com.example.picturemanagerdemo.util.LoadBitmapTask;
import com.example.picturemanagerdemo.util.LogUtil;
import com.example.picturemanagerdemo.widget.ZoomableGallery;

public class ImageViewActivity extends Activity {
	
	private String tag = ImageViewActivity.class.getSimpleName();
	
	private List<Image> mImages = new ArrayList<Image>();
	
	private ZoomableGallery mGallery = null;
	
	private GalleryAdapter mAdapter;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_view);
		
		Intent intent = getIntent();
		Image image = (Image) intent.getSerializableExtra("image_info");
		int imageIndex = intent.getIntExtra("image_index", 0);
		setTitle(image.getDisplayName());
		
		mGallery = (ZoomableGallery) findViewById(R.id.gallery);
		mGallery.setSpacing(20);
		mAdapter = new GalleryAdapter(this);
		mGallery.setAdapter(mAdapter);
		
		
		mImages.addAll(ImageDatabaseUtil.getImageAsFolder(this, image.getBucketId()));
		
		for (int i = 0; i < mImages.size(); i++) {
			if(i > imageIndex - 2 && i < imageIndex + 2) {
				mAdapter.add(BitmapLoadUtil.loadBitmapFromFile(mImages.get(i), 1));
			} else {
				mAdapter.add(null);
			}
		}
		mGallery.setOnItemSelectedListener(mOnItemSelectedListener);
		mAdapter.notifyDataSetChanged();
		mGallery.setSelection(imageIndex);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return false;
	}
	
	private OnItemSelectedListener mOnItemSelectedListener = new OnItemSelectedListener() {

		private int mmLastPosition = 0;
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			LogUtil.w(tag, "Page " + position + " is selected++++++++++>>>>LastPosition" + mmLastPosition);
			if(mmLastPosition < position) {
				if(mmLastPosition > 0  && mAdapter.getItem(mmLastPosition - 1) != null) {
					mAdapter.getItem(mmLastPosition - 1).recycle();
					mAdapter.set(mmLastPosition - 1, null);
				}
				if(position < mImages.size() - 1) {
					new LoadBitmapTask(mAdapter, position + 1, 1).execute(mImages.get(position + 1));
				}
			} else if(mmLastPosition > position){
				if(mmLastPosition < mImages.size() - 1 && mAdapter.getItem(mmLastPosition) != null) {
					mAdapter.getItem(mmLastPosition + 1).recycle();
					mAdapter.set(mmLastPosition + 1, null);
				}
				if(position > 0) {
					new LoadBitmapTask(mAdapter, position - 1, 1).execute(mImages.get(position - 1));
				}
			}
			mmLastPosition = position;
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			Log.w("AAAAAAAAAA", "Nothing selected");
		}
	};
	
	
	
//	public Bitmap loadPhoto(String path){
//		Options options = new Options();
//		
//		//以最省内存的方式读取本地资源的图片
//		options.inPreferredConfig = Bitmap.Config.RGB_565;   
//		options.inPurgeable = true;  
//		options.inInputShareable = true;  
//					
//		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
//		mCache.put(path, bitmap);
//		return bitmap;
//	}
////		再加一个图片额度，超过就释放它
//	private int mCacheCapacity = 1;
//	
//	private Map<String, Object> mCache = Collections.synchronizedMap(
//			new LinkedHashMap<String, Object>() {
//				@Override
//				protected boolean removeEldestEntry(java.util.Map.Entry<String, Object> eldest) {
//					return size() > mCacheCapacity;
//				}
//	});
	
}
