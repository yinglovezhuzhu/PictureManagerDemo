package com.example.picturemanagerdemo.widget;

import com.example.picturemanagerdemo.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TitleBar extends RelativeLayout {
	
	private Button mBtnLeft;
	private TextView mTvTitle;
	private Button mBtnRight;

	public TitleBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public TitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public TitleBar(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context) {
		View view = View.inflate(context, R.layout.layout_title_bar, null);
		mBtnLeft = (Button) view.findViewById(R.id.btn_title_left);
		mBtnRight = (Button) view.findViewById(R.id.btn_title_right);
		mTvTitle = (TextView) view.findViewById(R.id.tv_title_center);
		addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}
	
	public void setTitleText(CharSequence text) {
		mTvTitle.setText(text);
	}
	
	public void setTitleText(int resid) {
		mTvTitle.setText(resid);
	}
	
	public void setLeftButtonText(CharSequence text) {
		mBtnLeft.setText(text);
	}
	
	public void setLeftButtonText(int resid) {
		mBtnLeft.setText(resid);
	}
	
	public void setLeftButtonListener(View.OnClickListener listener) {
		mBtnLeft.setOnClickListener(listener);
	}
	
	public void setLeftButton(CharSequence text, View.OnClickListener listener) {
		setLeftButtonText(text);
		setLeftButtonListener(listener);
	}

	public void setLeftButton(int resid, View.OnClickListener listener) {
		setLeftButtonText(resid);
		setLeftButtonListener(listener);
	}
	
	public void setRightButtonText(CharSequence text) {
		mBtnRight.setText(text);
	}
	
	public void setRightButtonText(int resid) {
		mBtnRight.setText(resid);
	}
	
	public void setRightButtonListener(View.OnClickListener listener) {
		mBtnRight.setOnClickListener(listener);
	}
	
	public void setRightButton(CharSequence text, View.OnClickListener listener) {
		setRightButtonText(text);
		setRightButtonListener(listener);
	}
	
	public void setRightButton(int resid, View.OnClickListener listener) {
		setRightButtonText(resid);
		setLeftButtonListener(listener);
	}

}
