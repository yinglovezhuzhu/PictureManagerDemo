package com.example.picturemanagerdemo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.AdapterView;

import com.example.picturemanagerdemo.adapter.ImageListAdapter;
import com.example.picturemanagerdemo.db.ImageDatabaseHelper;
import com.example.picturemanagerdemo.db.util.ImageDatabaseUtil;
import com.example.picturemanagerdemo.entity.Image;
import com.example.picturemanagerdemo.widget.TitleBar;

@SuppressWarnings("unused")
public class ImageListActivity extends Activity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

	private String tag = ImageListActivity.class.getSimpleName();
	
	private GridView mGridView;
	
	private ImageListAdapter mAdapter;
	
	private TitleBar mTitleBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_list);
		
		Image folder = (Image) getIntent().getSerializableExtra("image_folder");
		
		mTitleBar = (TitleBar) findViewById(R.id.tb_title_bar);
		
		mTitleBar.setTitleText(folder.getBucketDisplayName());
		
		mGridView = (GridView) findViewById(R.id.gv_image_list);
		mAdapter = new ImageListAdapter(this);
		mGridView.setAdapter(mAdapter);
		mGridView.setNumColumns(3);
		mGridView.setOnItemClickListener(this);
		mGridView.setOnItemLongClickListener(this);
		
		mAdapter.addAll(ImageDatabaseUtil.getImageAsFolder(this,folder.getBucketId()));
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	private void changeToMultiChoiceMode() {
		resetChecked(mGridView);
		mGridView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
	}
	
	private void changeToNoneChoiceMode() {
		resetChecked(mGridView);
		mGridView.setChoiceMode(AbsListView.CHOICE_MODE_NONE);
	}
	
	private void resetChecked(GridView gridView) {
		if(gridView.getChoiceMode() == AbsListView.CHOICE_MODE_NONE) {
			return;
		} else if(gridView.getChoiceMode() == AbsListView.CHOICE_MODE_SINGLE) {
			gridView.setItemChecked(gridView.getCheckedItemPosition(), false);
		} else if(gridView.getChoiceMode() == AbsListView.CHOICE_MODE_MULTIPLE) {
			SparseBooleanArray array = gridView.getCheckedItemPositions();
			if(null != array) {
				for(int i = 0; i < array.size(); i++) {
					if(array.valueAt(i)) {
						gridView.setItemChecked(array.keyAt(i), false);
					}
				}
			}
		}
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(ImageListActivity.this, ImageViewActivity.class);
		intent.putExtra("image_info", mAdapter.getItem(position));
		intent.putExtra("image_index", position);
		startActivity(intent);
	}


	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			final int position, long id) {
		new AlertDialog.Builder(this)
		.setTitle(R.string.operation)
		.setItems(getResources().getStringArray(R.array.image_operation), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					new AlertDialog.Builder(ImageListActivity.this)
					.setTitle(R.string.warming)
					.setMessage(R.string.delete_image_comfirm)
					.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							List<Image> images = new ArrayList<Image>();
							images.add(mAdapter.getItem(position));
							if(ImageDatabaseUtil.deleteImages(ImageListActivity.this, images) > 0) {
								mAdapter.remove(position);
							}
						}
					})
					.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					}).show();
					break;
				case 1:
					// TODO 涓婁紶鐓х墖鎿嶄綔锛屽苟鎶婃湰鍦扮収鐗囩殑鐘舵�鏀逛负宸蹭笂浼�
					List<Image> images = new ArrayList<Image>();
					images.add(mAdapter.getItem(position));
					ImageDatabaseUtil.setUploadState(ImageListActivity.this, images, ImageDatabaseHelper.STATE_UPLOADED);
					break;
				case 2:
					// TODO nothing
					break;
				default:
					break;
				}
			}
		}).show();
		return false;
	}
}
