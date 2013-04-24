package com.example.picturemanagerdemo.entity;

import java.io.Serializable;
import java.lang.ref.WeakReference;

import android.graphics.Bitmap;

/**
 * ÕºœÒ µÃÂ¿‡
 * @author zhangyunying
 *
 */
public class Image implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4688278962480416180L;
	private String id;
	private String data;
	private int size;
	private String displayName;
	private String mimeType;
	private String title;
	private String dateAdded;
	private String dateModified;
	private String dateTaken;
	private String bucketId;
	private String bucketDisplayName;
	private int width;
	private int height;
	private int state;
	private boolean flag;
	
	public WeakReference<Bitmap> pic;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDateAdded() {
		return dateAdded;
	}
	public void setDateAdded(String dateAdded) {
		this.dateAdded = dateAdded;
	}
	public String getDateModified() {
		return dateModified;
	}
	public void setDateModified(String dateModified) {
		this.dateModified = dateModified;
	}
	public String getDateTaken() {
		return dateTaken;
	}
	public void setDateTaken(String dateTaken) {
		this.dateTaken = dateTaken;
	}
	public String getBucketId() {
		return bucketId;
	}
	public void setBucketId(String bucketId) {
		this.bucketId = bucketId;
	}
	public String getBucketDisplayName() {
		return bucketDisplayName;
	}
	public void setBucketDisplayName(String bucketDisplayName) {
		this.bucketDisplayName = bucketDisplayName;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	@Override
	public String toString() {
		return "Image [id=" + id + ", data=" + data + ", size=" + size
				+ ", displayName=" + displayName + ", mimeType=" + mimeType
				+ ", dateAdded=" + dateAdded + ", dateModified=" + dateModified
				+ ", dateTaken=" + dateTaken + ", bucketId=" + bucketId
				+ ", bucketDisplayName=" + bucketDisplayName + ", width="
				+ width + ", height=" + height + ", state=" + state + ", flag="
				+ flag + "]";
	}
	
}
