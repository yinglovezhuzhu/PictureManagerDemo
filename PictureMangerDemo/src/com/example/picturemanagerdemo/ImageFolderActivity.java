package com.example.picturemanagerdemo;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.AdapterView;

import com.example.picturemanagerdemo.adapter.ImageFolderAdapter;
import com.example.picturemanagerdemo.db.util.ImageDatabaseUtil;
import com.example.picturemanagerdemo.entity.Image;
import com.example.picturemanagerdemo.util.LogUtil;

public class ImageFolderActivity extends Activity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
	
	private String tag = ImageFolderActivity.class.getSimpleName();
	
	private GridView mGridView;
	
	private ImageFolderAdapter mFolderAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_folder);
		
		setTitle(R.string.native_image);
		
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		ImageApplication.mScreenWidth = metrics.widthPixels;
		
		mGridView = (GridView) findViewById(R.id.gv_folder_list);
		mFolderAdapter = new ImageFolderAdapter(this);
		mGridView.setAdapter(mFolderAdapter);
		mGridView.setOnItemClickListener(this);
		mGridView.setOnItemLongClickListener(this);
		mGridView.setNumColumns(2);
		ImageDatabaseUtil.refreshData(this);//本地数据库与系统图片数据库同步操作
		
		mFolderAdapter.addAll(ImageDatabaseUtil.getFolders(this));
		
	}
	
	@Override
	protected void onStart() {
		Log.i(tag, Environment.getExternalStorageDirectory().getPath());
		Log.i(tag, Environment.getExternalStorageState());
		if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			new AlertDialog.Builder(this)
			.setTitle(R.string.warming)
			.setMessage(R.string.msg_no_sdcard)
			.setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			}).show();
		}
		super.onStart();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(ImageFolderActivity.this, ImageListActivity.class);
		intent.putExtra("image_folder", mFolderAdapter.getItem(position));
		startActivity(intent);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			final int position, long id) {
		new AlertDialog.Builder(this)
		.setTitle(R.string.operation)
		.setItems(getResources().getStringArray(R.array.image_folder_operation), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					
					break;
				case 1:
					new AlertDialog.Builder(ImageFolderActivity.this)
					.setTitle(R.string.warming)
					.setMessage(R.string.delete_folder_comfirm)
					.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Image folder = mFolderAdapter.getItem(position);
							File folderFile = new File(folder.getData()).getParentFile();
							List<Image> images = ImageDatabaseUtil.getImageAsFolder(ImageFolderActivity.this,
									folder.getBucketId());
							if(ImageDatabaseUtil.deleteImages(ImageFolderActivity.this, images) >= images.size()) {
								LogUtil.i(tag, "Delete folder succes");
								if(folderFile.isDirectory() && folderFile.list().length < 1) {
									folderFile.delete();
								}
								mFolderAdapter.remove(position);
							} else {
								mFolderAdapter.clear();
								mFolderAdapter.addAll(ImageDatabaseUtil.getFolders(ImageFolderActivity.this));
							}
							
						}
					})
					.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
						}
					}).show();
					break;
				case 2:
					break;
				default:
					break;
				}
			}
		}).show();
		return false;
	}
}
