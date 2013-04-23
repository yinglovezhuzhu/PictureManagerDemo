package com.example.picturemanagerdemo.util;
/*
 * 文件名：BitmapCache.java
 * 作者：张运迎
 * 版本：v1.0
 * 创建时间：2013-03-22
 * 修改时间:
 * 修改人：
 */
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Hashtable;

import com.example.picturemanagerdemo.entity.Image;

import android.graphics.Bitmap;
/**
 * Bitmap缓存类，防止内存溢出
 *
 */
public class BitmapCache {
	
	private static BitmapCache mCache;
	
	/** 用于Chche内容的存储 */
	private Hashtable<String, BtimapRef> mBitmapRefs;
	
	/** 垃圾Reference的队列（所引用的对象已经被回收，则将该引用存入队列中） */
	private ReferenceQueue<Bitmap> mRefQueue;


	private BitmapCache() {
		mBitmapRefs = new Hashtable<String, BtimapRef>();
		mRefQueue = new ReferenceQueue<Bitmap>();
	}

	/**
	 * 取得缓存器单实例
	 */
	public static BitmapCache getInstance() {
		if (mCache == null) {
			mCache = new BitmapCache();
		}
		return mCache;
	}

	/**
	 * 以软引用的方式对一个Bitmap对象的实例进行引用并保存该引用
	 */
	private void addCacheBitmap(Bitmap bmp, String key) {
		cleanCache();// 清除垃圾引用
		BtimapRef ref = new BtimapRef(bmp, mRefQueue, key);
		mBitmapRefs.put(key, ref);
	}

	/**
	 * 依据所指定的文件名获取图片
	 */
	public Bitmap getBitmap(Image image, int columns) {

		Bitmap bitmapImage = null;
		// 缓存中是否有该Bitmap实例的软引用，如果有，从软引用中取得。
		if (mBitmapRefs.containsKey(image)) {
			BtimapRef ref = (BtimapRef) mBitmapRefs.get(image);
			bitmapImage = (Bitmap) ref.get();
		}
		// 如果没有软引用，或者从软引用中得到的实例是null，重新构建一个实例，
		// 并保存对这个新建实例的软引用
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

	// 清除Cache内的全部内容
	public void clearCache() {
		cleanCache();
		mBitmapRefs.clear();
		System.gc();
		System.runFinalization();
	}
	
	/**
	 * 继承SoftReference，使得每一个实例都具有可识别的标识。
	 */
	private class BtimapRef extends SoftReference<Bitmap> {
		private String mmKey = "";

		public BtimapRef(Bitmap bmp, ReferenceQueue<Bitmap> q, String key) {
			super(bmp, q);
			mmKey = key;
		}
	}

}