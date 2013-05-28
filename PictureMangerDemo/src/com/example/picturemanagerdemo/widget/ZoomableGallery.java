package com.example.picturemanagerdemo.widget;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Gallery;

import com.example.picturemanagerdemo.util.LogUtil;


/**
 * 功能：该类作为支持放大、缩小和图片上下边界回弹的大图浏览gallery，必须要结合ImageViewTouchBase使用
 * @author xiaoying
 *
 */
@SuppressWarnings("deprecation")
public class ZoomableGallery extends Gallery {
	
	private final String tag = ZoomableGallery.class.getSimpleName();
    private int physicsWidth;// 控件宽度
    private int physicsHeight;// 控件高度
    private GestureDetector gestureScanner;
    private ImageViewTouchBase imageView;
    float baseValue;
    float originalScale;
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    int mode = NONE;

    public ZoomableGallery(Context context) {
	super(context);
    }

    public ZoomableGallery(Context context, AttributeSet attrs, int defStyle) {
	super(context, attrs, defStyle);
    }

    public ZoomableGallery(Context context, AttributeSet attrs) {
	super(context, attrs);
	gestureScanner = new GestureDetector(new MySimpleGesture());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
	super.onLayout(changed, l, t, r, b);
	this.physicsHeight = getHeight();
	this.physicsWidth = getWidth();
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
    	LogUtil.e(tag, "distance<<<>>>(" + distanceX + ", " + distanceY + ")");
	View view = ZoomableGallery.this.getSelectedView();
	if (view instanceof ImageViewTouchBase) {
	    imageView = (ImageViewTouchBase) view;

	    float v[] = new float[9];
	    Matrix m = imageView.getImageMatrix();
	    m.getValues(v);
	    // 图片实时的上下左右坐�?
	    float left, right;
	    // 图片的实时宽，高
	    float width, height;
	    width = imageView.getScale() * imageView.getImageWidth();
	    height = imageView.getScale() * imageView.getImageHeight();
	    // 下面的代码时移动放大的图片和到达图片左右边缘时�?滑动到下�?��的关键代码，如果不是对整个框架和流程非常了解，请勿改�?
	    if ((int) width <= physicsWidth && (int) height <= physicsHeight)// 如果图片当前大小<屏幕大小，直接处理滑屏事�?
	    {
		super.onScroll(e1, e2, distanceX, distanceY);
	    } else {
		if (mode == DRAG) {
		    left = v[Matrix.MTRANS_X];
		    right = left + width;
		    Rect r = new Rect();
		    imageView.getGlobalVisibleRect(r);

		    if (distanceX > 0)// 向左滑动
		    {
			if (r.left > 0) {// 判断当前ImageView是否显示完全
			    super.onScroll(e1, e2, distanceX, distanceY);
			} else if (right < physicsWidth) {
			    super.onScroll(e1, e2, distanceX, distanceY);
			} else {
			    if (height > physicsHeight) {
				imageView.postTranslate(-distanceX, -distanceY);
			    } else {
				imageView.postTranslate(-distanceX, 0);
			    }
			}
		    } else if (distanceX < 0)// 向右滑动
		    {
			if (r.right < physicsWidth) {
			    super.onScroll(e1, e2, distanceX, distanceY);
			} else if (left > 0) {
			    super.onScroll(e1, e2, distanceX, distanceY);
			} else {
			    if (height > physicsHeight) {
				imageView.postTranslate(-distanceX, -distanceY);
			    } else {
				imageView.postTranslate(-distanceX, 0);
			    }
			}
		    }
		}
	    }
	} else {
	    super.onScroll(e1, e2, distanceX, distanceY);
	}
	return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
	return false;
    }

    // 以下代码是放大缩小图片和手指离开触摸屏的时�?是否符合条件进行边界回弹的关键代�?
    @Override
    public boolean onTouchEvent(MotionEvent event) {
	gestureScanner.onTouchEvent(event);
	View view = ZoomableGallery.this.getSelectedView();
	if (view instanceof ImageViewTouchBase) {
	    imageView = (ImageViewTouchBase) view;
	    switch (event.getAction() & MotionEvent.ACTION_MASK) {
	    case MotionEvent.ACTION_UP:
		// 判断上下边界是否越界
		if (mode == DRAG) {
		    float height = imageView.getScale() * imageView.getImageHeight();
		    if ((int) height <= physicsHeight)// 图片边界回弹的先决条件是图片高度大于控件高度，才能允许被上下拖拽，手指离�?��时�?才能回弹
		    {
			break;
		    }
		    // 下面是图片边界回弹的代码，如果不经过高度判断，图片会乱跳
		    float v[] = new float[9];
		    Matrix m = imageView.getImageMatrix();
		    m.getValues(v);
		    float top = v[Matrix.MTRANS_Y];
		    float bottom = top + height;
		    if (top > 0) {
			imageView.postTranslateDur(-top, 200f);
		    }
		    if (bottom < physicsHeight) {
			imageView.postTranslateDur(physicsHeight - bottom, 200f);
		    }
		}
		break;
	    case MotionEvent.ACTION_DOWN:
		mode = DRAG;
		baseValue = 0;
		originalScale = imageView.getScale();
		break;
	    case MotionEvent.ACTION_MOVE:
		if (mode == ZOOM) {
		    if (event.getPointerCount() == 2) {
			float x = event.getX(0) - event.getX(1);
			float y = event.getY(0) - event.getY(1);
			float value = (float) Math.sqrt(x * x + y * y);// 计算两点的距�?
			if (baseValue == 0) {
			    baseValue = value;
			} else {
			    float scale = value / baseValue;// 当前两点间的距离除以手指落下时两点间的距离就是需要缩放的比例�?
			    imageView.zoomTo(originalScale * scale);
			}
		    }
		}
		break;
	    case MotionEvent.ACTION_POINTER_DOWN:
		mode = ZOOM;
		break;
	    case MotionEvent.ACTION_POINTER_UP:
		mode = NONE;
		break;
	    }
	}
	return super.onTouchEvent(event);
    }

    private class MySimpleGesture extends SimpleOnGestureListener {
	// 按两下的第二下Touch down时触�?
	public boolean onDoubleTap(MotionEvent e) {
	    View view = ZoomableGallery.this.getSelectedView();
	    if (view instanceof ImageViewTouchBase) {
		imageView = (ImageViewTouchBase) view;
		if (imageView.getScale() > imageView.getScaleRate()) {
		    imageView.zoomTo(imageView.getScaleRate(), physicsWidth / 2, physicsHeight / 2, 200f);
		} else {
		    imageView.zoomTo(1.0f, physicsWidth / 2, physicsHeight / 2, 200f);
		}
	    }
	    return true;
	}
    }
}