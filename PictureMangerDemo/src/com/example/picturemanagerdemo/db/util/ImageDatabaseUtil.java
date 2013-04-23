package com.example.picturemanagerdemo.db.util;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.provider.MediaStore;

import com.example.picturemanagerdemo.db.ImageDatabaseHelper;
import com.example.picturemanagerdemo.entity.Image;
import com.example.picturemanagerdemo.util.FileUtil;
import com.example.picturemanagerdemo.util.LogUtil;

public class ImageDatabaseUtil {
	
	private static String tag = ImageDatabaseUtil.class.getSimpleName();

	/**
	 * 刷新本地图片数据库，与系统图片数据库进行更新，如果这里用时较长，给以加载框提示
	 * @param context
	 */
	public static void refreshData(Context context) {
		SQLiteDatabase db = ImageDatabaseHelper.getInstanse(context).getWritableDatabase();
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, 
				null, null, null, MediaStore.Images.Media.DEFAULT_SORT_ORDER);
		if(cursor != null && cursor.moveToFirst()) {
			int id = cursor.getColumnIndex("_id");
			int data = cursor.getColumnIndex("_data");
			int size = cursor.getColumnIndex("_size");
			int displayName = cursor.getColumnIndex("_display_name");
			int mimeType = cursor.getColumnIndex("mime_type");
			int title = cursor.getColumnIndex("title");
			int dateAdded = cursor.getColumnIndex("date_added");
			int dateModified = cursor.getColumnIndex("date_modified");
			int dateTaken = cursor.getColumnIndex("datetaken");
			int bucketId = cursor.getColumnIndex("bucket_id");
			int bucketDisplayName = cursor.getColumnIndex("bucket_display_name");
			int width = cursor.getColumnIndex("width");
			int height = cursor.getColumnIndex("height");
			ContentValues values = new ContentValues();
			db.beginTransaction();
			values.clear();
			values.put("flag", ImageDatabaseHelper.FLAG_UN_REFLESH);
			db.update("images", values, null, null);
			do {
				if(!containsData(db, cursor.getString(id))) {
					values.clear();
					values.put("_id", cursor.getString(id));
					values.put("_data", cursor.getString(data));
					values.put("_size", cursor.getString(size));
					values.put("_display_name", cursor.getString(displayName));
					values.put("mime_type", cursor.getString(mimeType));
					values.put("title", cursor.getString(title));
					values.put("date_added", cursor.getString(dateAdded));
					values.put("date_modified", cursor.getString(dateModified));
					values.put("date_taken", cursor.getString(dateTaken));
					values.put("bucket_id", cursor.getString(bucketId));
					values.put("bucket_display_name", cursor.getString(bucketDisplayName));
					values.put("width", cursor.getString(width));
					values.put("height", cursor.getString(height));
					values.put("state", 0);
					values.put("flag", ImageDatabaseHelper.FLAG_REFRESHED);
					db.insert("images", null, values);
				}
			} while(cursor.moveToNext());
			cursor.close();
			db.delete("images", "flag = ?", new String [] {String.valueOf(0), });
			db.setTransactionSuccessful();
			db.endTransaction();
			db.close();
		}
	}
	
	public static boolean containsData(SQLiteDatabase db, String id) {
		ContentValues values = new ContentValues();
		values.put("flag", ImageDatabaseHelper.FLAG_REFRESHED);
		try {
			int culumn = db.update("images", values, "_id = ?", new String [] {id, });
			LogUtil.e(tag, "Update success =====>>> id = " + id);
			return culumn > 0;
		} catch (SQLiteException ex){
			ex.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 用group by语句可以找出所有相册目录，并且可以得到目录下某一张图片
	 * @param context
	 * @return
	 */
	public static List<Image> getFolders(Context context) {
		List<Image> folders = new ArrayList<Image>();
		SQLiteDatabase db = ImageDatabaseHelper.getInstanse(context).getReadableDatabase();
		Cursor cursor = db.query("images", null, null, null, "bucket_id", null, null);
		folders.addAll(getResultSet(cursor));
		cursor.close();
		db.close();
		return folders;
	}
	
	/**
	 * 获取某个目录下的所有图片信息
	 * @param context
	 * @param folderId
	 * @return
	 */
	public static List<Image> getImageAsFolder(Context context, String folderId) {
		List<Image> images = new ArrayList<Image>();
		SQLiteDatabase db = ImageDatabaseHelper.getInstanse(context).getReadableDatabase();
		Cursor cursor = db.query("images", null, "bucket_id = ?", new String [] {folderId, }, null, null, null);
		images.addAll(getResultSet(cursor));
		cursor.close();
		db.close();
		return images;
	}
	
	/**
	 * 更新照片的上传状态
	 * @param context
	 * @param id
	 * @param state
	 * @return
	 */
	public static int setUploadState(Context context, List<Image> images, int state) {
		SQLiteDatabase db = ImageDatabaseHelper.getInstanse(context).getWritableDatabase();
		ContentValues values = new ContentValues();
		int column = 0;
		for (Image image : images) {
			values.put("state", state);
			column += db.update("images", values, "_id = ?", new String [] {image.getId(), });
			LogUtil.i(tag, "Upload " + image.getData());
		}
		db.close();
		return column;
	}
	
	/**
	 * 查找图片结果集
	 * @param cursor
	 * @return
	 */
	private static List<Image> getResultSet(Cursor cursor) {
		List<Image> results = new ArrayList<Image>();
		if(cursor != null && cursor.moveToFirst()) {
			int id = cursor.getColumnIndex("_id");
			int data = cursor.getColumnIndex("_data");
			int size = cursor.getColumnIndex("_size");
			int displayName = cursor.getColumnIndex("_display_name");
			int mimeType = cursor.getColumnIndex("mime_type");
			int title = cursor.getColumnIndex("title");
			int dateAdded = cursor.getColumnIndex("date_added");
			int dateModified = cursor.getColumnIndex("date_modified");
			int dateTaken = cursor.getColumnIndex("date_taken");
			int bucketId = cursor.getColumnIndex("bucket_id");
			int bucketDisplayName = cursor.getColumnIndex("bucket_display_name");
			int width = cursor.getColumnIndex("width");
			int height = cursor.getColumnIndex("height");
			int state = cursor.getColumnIndex("state");
			int flag = cursor.getColumnIndex("flag");
			Image image = null;
			do {
				image = new Image();
				image.setId(cursor.getString(id));
				image.setData(cursor.getString(data));
				image.setSize(cursor.getInt(size));
				image.setDisplayName(cursor.getString(displayName));
				image.setMimeType(cursor.getString(mimeType));
				image.setTitle(cursor.getString(title));
				image.setDateAdded(cursor.getString(dateAdded));
				image.setDateModified(cursor.getString(dateModified));
				image.setDateTaken(cursor.getString(dateTaken));
				image.setBucketId(cursor.getString(bucketId));
				image.setBucketDisplayName(cursor.getString(bucketDisplayName));
				image.setWidth(cursor.getInt(width));
				image.setHeight(cursor.getInt(height));
				image.setState(cursor.getInt(state));
				image.setFlag(cursor.getInt(flag) == 1);
				results.add(image);
			} while (cursor.moveToNext());
		}
		return results;
	}
	
	public static int deleteImages(Context context, List<Image> images) {
		SQLiteDatabase db = ImageDatabaseHelper.getInstanse(context).getWritableDatabase();
		ContentResolver resolver = context.getContentResolver();
		int rows = 0;
		for (Image image : images) {
			if(FileUtil.deleteFile(image.getData())) {
				LogUtil.i(tag, "Delete Image File succes" + image.getData());
				rows += db.delete("images", "_id = ?", new String [] {image.getId()});
				LogUtil.i(tag, "Delete Image data from Database succes" + image.getData());
				resolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, 
						MediaStore.Images.Media._ID + " = ?", new String [] {image.getId()});
				LogUtil.i(tag, "Delete Image data from ContentProvider succes" + image.getData());
			}
			
		}
		db.close();
		return rows;
	}
}
