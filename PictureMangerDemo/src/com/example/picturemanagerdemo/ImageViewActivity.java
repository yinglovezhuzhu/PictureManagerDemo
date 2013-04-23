package com.example.picturemanagerdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.picturemanagerdemo.entity.Image;
import com.example.picturemanagerdemo.util.ImageLoaderTask;

public class ImageViewActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		Image image = (Image) intent.getSerializableExtra("image_info");
		setTitle(image.getDisplayName());
				
		ImageView iv = new ImageView(this);
		new ImageLoaderTask(iv, 1).execute(image);
		setContentView(iv);
	}
}
