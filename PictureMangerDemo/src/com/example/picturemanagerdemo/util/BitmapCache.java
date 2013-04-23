package com.example.picturemanagerdemo.util;
/*
 * �ļ�����BitmapCache.java
 * ���ߣ�����ӭ
 * �汾��v1.0
 * ����ʱ�䣺2013-03-22
 * �޸�ʱ��:
 * �޸��ˣ�
 */
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Hashtable;

import com.example.picturemanagerdemo.entity.Image;

import android.graphics.Bitmap;
/**
 * Bitmap�����࣬��ֹ�ڴ����
 *
 */
public class BitmapCache {
	
	private static BitmapCache mCache;
	
	/** ����Chche���ݵĴ洢 */
	private Hashtable<String, BtimapRef> mBitmapRefs;
	
	/** ����Reference�Ķ��У������õĶ����Ѿ������գ��򽫸����ô�������У� */
	private ReferenceQueue<Bitmap> mRefQueue;


	private BitmapCache() {
		mBitmapRefs = new Hashtable<String, BtimapRef>();
		mRefQueue = new ReferenceQueue<Bitmap>();
	}

	/**
	 * ȡ�û�������ʵ��
	 */
	public static BitmapCache getInstance() {
		if (mCache == null) {
			mCache = new BitmapCache();
		}
		return mCache;
	}

	/**
	 * �������õķ�ʽ��һ��Bitmap�����ʵ���������ò����������
	 */
	private void addCacheBitmap(Bitmap bmp, String key) {
		cleanCache();// �����������
		BtimapRef ref = new BtimapRef(bmp, mRefQueue, key);
		mBitmapRefs.put(key, ref);
	}

	/**
	 * ������ָ�����ļ�����ȡͼƬ
	 */
	public Bitmap getBitmap(Image image, int columns) {

		Bitmap bitmapImage = null;
		// �������Ƿ��и�Bitmapʵ���������ã�����У�����������ȡ�á�
		if (mBitmapRefs.containsKey(image)) {
			BtimapRef ref = (BtimapRef) mBitmapRefs.get(image);
			bitmapImage = (Bitmap) ref.get();
		}
		// ���û�������ã����ߴ��������еõ���ʵ����null�����¹���һ��ʵ����
		// �����������½�ʵ����������
		if (bitmapImage == null) {
			bitmapImage = BitmapLoadUtil.loadBitmapFromFile(image, columns);
		}

		this.addCacheBitmap(bitmapImage, image.getData());
		return bitmapImage;
	}

	private void cleanCache() {
		BtimapRef ref = null;
		while ((ref = (BtimapRef) mRefQueue.poll()) != null) {
			mBitmapRefs.remove(ref.mmKey);
		}
	}

	// ���Cache�ڵ�ȫ������
	public void clearCache() {
		cleanCache();
		mBitmapRefs.clear();
		System.gc();
		System.runFinalization();
	}
	
	/**
	 * �̳�SoftReference��ʹ��ÿһ��ʵ�������п�ʶ��ı�ʶ��
	 */
	private class BtimapRef extends SoftReference<Bitmap> {
		private String mmKey = "";

		public BtimapRef(Bitmap bmp, ReferenceQueue<Bitmap> q, String key) {
			super(bmp, q);
			mmKey = key;
		}
	}

}