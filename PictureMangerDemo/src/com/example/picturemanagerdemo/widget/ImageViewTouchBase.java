package com.example.picturemanagerdemo.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.ImageView;

/**
 * 功能：该类仿照gallery源码中的ImageViewTouchBase实现,可以作为支持放大缩小矩阵平移的增强型ImageView
 * @author xiaoying
 *
 */
public class ImageViewTouchBase extends ImageView {
	@SuppressWarnings("unused")
	private static final String TAG = "ImageViewTouchBase";

	protected Matrix mBaseMatrix = new Matrix();

	protected Matrix mSuppMatrix = new Matrix();

	private final Matrix mDisplayMatrix = new Matrix();

	private final float[] mMatrixValues = new float[9];

	protected Bitmap image = null;

	int mThisWidth = -1, mThisHeight = -1;

	float mMaxZoom = 6.0f;// 图片最大缩放比例
	float mMinZoom;// 最小缩放比例

	private int imageWidth;// 图片的原始宽
	private int imageHeight;// 图片的原始高

	private float scaleRate;// 图片适应屏幕的缩放比
	private int physicsWidth;// 控件宽度
	private int physicsHeight;// 控件高度
	private int viewWidthUWant, viewHeightUWant;// 你想设定初始的高度和宽度

	public ImageViewTouchBase(Context context) {
		super(context);
		init();
	}

	public ImageViewTouchBase(Context context, int viewWidthUWant,
			int viewHeightUWant) {
		super(context);
		this.viewWidthUWant = viewWidthUWant;
		this.viewHeightUWant = viewHeightUWant;
		init();
	}

	public ImageViewTouchBase(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public ImageViewTouchBase(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ImageViewTouchBase(Context context, AttributeSet attrs,
			int imageWidth, int imageHeight) {
		super(context, attrs);
		init();
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		physicsHeight = getHeight();
		physicsWidth = getWidth();
		arithScaleRate();
		// 缩放到屏幕大�?
		zoomTo(scaleRate, physicsWidth / 2f, physicsHeight / 2f);
	}

	/**
	 * 计算图片要�?应屏幕需要缩放的比例
	 */
	private void arithScaleRate() {
		float scaleWidth = physicsWidth / (float) imageWidth;
		float scaleHeight = physicsHeight / (float) imageHeight;
		scaleRate = Math.min(scaleWidth, scaleHeight);
	}

	public float getScaleRate() {
		return scaleRate;
	}

	public int getImageWidth() {
		return imageWidth;
	}

	public void setImageWidth(int imageWidth) {
		this.imageWidth = imageWidth;
	}

	public int getImageHeight() {
		return imageHeight;
	}

	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			event.startTracking();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.isTracking()
				&& !event.isCanceled()) {
			if (getScale() > 1.0f) {
				zoomTo(1.0f);
				return true;
			}
		}
		return super.onKeyUp(keyCode, event);
	}

	protected Handler mHandler = new Handler();

	@Override
	public void setImageResource(int resId) {
		super.setImageResource(resId);
		Bitmap bitmap = BitmapFactory.decodeResource(getContext()
				.getResources(), resId);
		this.imageHeight = bitmap.getHeight();
		this.imageWidth = bitmap.getWidth();
		image = bitmap;
	}

	@Override
	public void setImageBitmap(Bitmap bitmap) {
		if(bitmap == null) {
			return;
		}
		super.setImageBitmap(bitmap);
		this.imageHeight = bitmap.getHeight();
		this.imageWidth = bitmap.getWidth();
		image = bitmap;

		physicsWidth = getWidth();
		physicsHeight = getHeight();
		if (physicsWidth <= 0) {
			physicsWidth = viewWidthUWant;
		}
		if (physicsHeight <= 0) {
			physicsHeight = viewHeightUWant;
		}
		arithScaleRate();
		// 缩放到屏幕大�?
		zoomTo(scaleRate, physicsWidth / 2f, physicsHeight / 2f);
	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		super.setImageDrawable(drawable);
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		if(bitmap.isRecycled()) {
			return;
		}
		Canvas canvas = new Canvas(bitmap);
//		canvas.setBitmap(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		if(!bitmap.isRecycled()) {
			drawable.draw(canvas);
		}
		image = bitmap;
		this.imageHeight = bitmap.getHeight();
		this.imageWidth = bitmap.getWidth();
	}

	protected void center(boolean horizontal, boolean vertical) {
		if (image == null) {
			return;
		}

		Matrix m = getImageViewMatrix();

		RectF rect = new RectF(0, 0, image.getWidth(), image.getHeight());

		m.mapRect(rect);

		float height = rect.height();
		float width = rect.width();

		float deltaX = 0, deltaY = 0;

		if (vertical) {
			int viewHeight = getHeight();
			if (height < viewHeight) {
				deltaY = (viewHeight - height) / 2 - rect.top;
			} else if (rect.top > 0) {
				deltaY = -rect.top;
			} else if (rect.bottom < viewHeight) {
				deltaY = getHeight() - rect.bottom;
			}
		}

		if (horizontal) {
			int viewWidth = getWidth();
			if (width < viewWidth) {
				deltaX = (viewWidth - width) / 2 - rect.left;
			} else if (rect.left > 0) {
				deltaX = -rect.left;
			} else if (rect.right < viewWidth) {
				deltaX = viewWidth - rect.right;
			}
		}

		postTranslate(deltaX, deltaY);
		setImageMatrix(getImageViewMatrix());
	}

	private void init() {
		setScaleType(ImageView.ScaleType.MATRIX);
	}

	protected float getValue(Matrix matrix, int whichValue) {
		matrix.getValues(mMatrixValues);
		mMinZoom = (physicsWidth / 2f) / imageWidth;

		return mMatrixValues[whichValue];
	}

	// Get the scale factor out of the matrix.
	protected float getScale(Matrix matrix) {
		return getValue(matrix, Matrix.MSCALE_X);
	}

	public float getScale() {
		return getScale(mSuppMatrix);
	}

	protected Matrix getImageViewMatrix() {
		mDisplayMatrix.set(mBaseMatrix);
		mDisplayMatrix.postConcat(mSuppMatrix);
		return mDisplayMatrix;
	}

	static final float SCALE_RATE = 1.25F;

	protected float maxZoom() {
		if (image == null) {
			return 1F;
		}

		float fw = (float) image.getWidth() / (float) mThisWidth;
		float fh = (float) image.getHeight() / (float) mThisHeight;
		float max = Math.max(fw, fh) * 4;
		return max;
	}

	protected void zoomTo(float scale, float centerX, float centerY) {
		if (scale > mMaxZoom) {
			scale = mMaxZoom;
		} else if (scale < mMinZoom) {
			scale = mMinZoom;
		}

		float oldScale = getScale();
		float deltaScale = scale / oldScale;

		mSuppMatrix.postScale(deltaScale, deltaScale, centerX, centerY);
		setImageMatrix(getImageViewMatrix());
		center(true, true);
	}

	public void zoomTo(final float scale, final float centerX,
			final float centerY, final float durationMs) {
		final float incrementPerMs = (scale - getScale()) / durationMs;
		final float oldScale = getScale();
		final long startTime = System.currentTimeMillis();

		mHandler.post(new Runnable() {
			public void run() {
				long now = System.currentTimeMillis();
				float currentMs = Math.min(durationMs, now - startTime);
				float target = oldScale + (incrementPerMs * currentMs);
				zoomTo(target, centerX, centerY);
				if (currentMs < durationMs) {
					mHandler.post(this);
				}
			}
		});
	}

	public void zoomTo(float scale) {
		float cx = getWidth() / 2F;
		float cy = getHeight() / 2F;

		zoomTo(scale, cx, cy);
	}

	protected void zoomToPoint(float scale, float pointX, float pointY) {
		float cx = getWidth() / 2F;
		float cy = getHeight() / 2F;

		panBy(cx - pointX, cy - pointY);
		zoomTo(scale, cx, cy);
	}

	protected void zoomIn() {
		zoomIn(SCALE_RATE);
	}

	protected void zoomOut() {
		zoomOut(SCALE_RATE);
	}

	protected void zoomIn(float rate) {
		if (getScale() >= mMaxZoom) {
			return; // Don't let the user zoom into the molecular level.
		} else if (getScale() <= mMinZoom) {
			return;
		}
		if (image == null) {
			return;
		}

		float cx = getWidth() / 2F;
		float cy = getHeight() / 2F;

		mSuppMatrix.postScale(rate, rate, cx, cy);
		setImageMatrix(getImageViewMatrix());
	}

	protected void zoomOut(float rate) {
		if (image == null) {
			return;
		}

		float cx = getWidth() / 2F;
		float cy = getHeight() / 2F;

		Matrix tmp = new Matrix(mSuppMatrix);
		tmp.postScale(1F / rate, 1F / rate, cx, cy);

		if (getScale(tmp) < 1F) {
			mSuppMatrix.setScale(1F, 1F, cx, cy);
		} else {
			mSuppMatrix.postScale(1F / rate, 1F / rate, cx, cy);
		}
		setImageMatrix(getImageViewMatrix());
		center(true, true);
	}

	public void postTranslate(float dx, float dy) {
		mSuppMatrix.postTranslate(dx, dy);
		setImageMatrix(getImageViewMatrix());
	}

	float _dy = 0.0f;

	public void postTranslateDur(final float dy, final float durationMs) {
		_dy = 0.0f;
		final float incrementPerMs = dy / durationMs;
		final long startTime = System.currentTimeMillis();
		mHandler.post(new Runnable() {
			public void run() {
				long now = System.currentTimeMillis();
				float currentMs = Math.min(durationMs, now - startTime);

				postTranslate(0, incrementPerMs * currentMs - _dy);
				_dy = incrementPerMs * currentMs;

				if (currentMs < durationMs) {
					mHandler.post(this);
				}
			}
		});
	}

	protected void panBy(float dx, float dy) {
		postTranslate(dx, dy);
		setImageMatrix(getImageViewMatrix());
	}

	public void setViewWidthUWant(int viewWidthUWant) {
		this.viewWidthUWant = viewWidthUWant;
	}

	public void setViewHeightUWant(int viewHeightUWant) {
		this.viewHeightUWant = viewHeightUWant;
	}

}