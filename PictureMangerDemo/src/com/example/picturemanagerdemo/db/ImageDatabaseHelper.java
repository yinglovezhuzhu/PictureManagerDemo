package com.example.picturemanagerdemo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ImageDatabaseHelper extends SQLiteOpenHelper {
	
	public static final int STATE_UN_UPLOAD = 0;
	public static final int STATE_UPLOADING = 1;
	public static final int STATE_UPLOADED = 2;
	
	public static final int FLAG_UN_REFLESH = 0;
	public static final int FLAG_REFRESHED = 1;
	
	private static final int DB_VERSION = 1;
	
	private static final String DB_NAME = "picture.db";
	
	private static ImageDatabaseHelper sDatabaseHelper = null;
	
	public static ImageDatabaseHelper getInstanse(Context context) {
		if(null == sDatabaseHelper) {
			sDatabaseHelper = new ImageDatabaseHelper(context);
		}
		return sDatabaseHelper;
	}
	
	public ImageDatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//ͼƬ�����images
		//�ֶ�˵����_id:id��_data:ͼƬ·����_size:ͼƬ��С��_display_name:��ƣ����׺����
		//mime_type:MIME TYPE��title:���⣨�����׺����date_added:���ʱ�䣬date_modified:�޸�ʱ�䣬
		//date_taken:����ʱ�䣬bucket_id:Ŀ¼id��bucket_display_name:Ŀ¼��ƣ�width:ͼƬ���
		//height:ͼƬ�߶ȣ�extension:ͼƬ��׺��state:ͼƬ״̬��flag:��ʶ�����ڱ�ʶ�Ƿ���ϵͳͼƬ��ݿ�ͬ����
		//ÿ��ͬ��ǰȫ����Ϊ0��ͬ�����Ϊ1��Ȼ�� ��0��ȫ��ɾ���ʾϵͳͼƬ��ݿ���û�е�,�ļ���ɾ��
		db.execSQL("create table if not exists images(_id INTEGER PRIMARY KEY, _data TEXT, _size INTEGER, " +
				"_display_name TEXT, mime_type TEXT, title TEXT, date_added INTEGER, date_modified INTEGER, " +
				"date_taken INTEGER, bucket_id INTEGER, bucket_display_name TEXT, width INTEGER, height INTEGER, " +
				"state INTEGER, flag INTEGER)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Some thing should be done when database version upgrade
		
	}

}
