package com.example.photopicker.views.widgets;

import java.io.File;
import java.io.IOException;

import com.example.photopicker.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 自定义带删除小标的图片View
 * */
public class DelImageView extends RelativeLayout {

	private ImageView mImageView;

	private ImageView mDelImageView;

	private Context mContext;

	private BitmapFactory.Options mBitmapOptions;

	public DelImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		this.addDelView(context);
		initBitmapOptions();
	}

	public DelImageView(Context context) {
		super(context);
		this.addDelView(context);
		initBitmapOptions();
	}

	private void initBitmapOptions() {
		// TODO Auto-generated method stub
		mBitmapOptions = new BitmapFactory.Options();
		mBitmapOptions.inJustDecodeBounds = false;
		mBitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
		mBitmapOptions.inSampleSize = 4;
	}

	private void addDelView(Context context) {

		LayoutInflater aLayoutInflater = LayoutInflater.from(context);
		View aView = aLayoutInflater.inflate(R.layout.lp_view_delimageview,
				null);
		mImageView = (ImageView) aView.findViewById(R.id.img);
		mDelImageView = (ImageView) aView.findViewById(R.id.del_btn);
		mDelImageView.setVisibility(View.GONE);
		this.addView(aView);
	}

	public void setImageResource(int resId) {
		mImageView.setImageResource(resId);
	}

	public void setImageResource(String url) {
		if (mBitmapOptions == null)
			initBitmapOptions();
		try {
			mImageView.setImageBitmap(getCameraBitmapFromPath(mContext, url));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			mImageView.setImageBitmap(BitmapFactory.decodeFile(url,
					mBitmapOptions));
			e.printStackTrace();
		}
		mImageView
				.setImageBitmap(BitmapFactory.decodeFile(url, mBitmapOptions));

	}

	public void setDelLayoutParams(
			android.widget.LinearLayout.LayoutParams params) {
		super.setLayoutParams(params);

		LayoutParams aLayoutParams = (LayoutParams) mImageView
				.getLayoutParams();
		aLayoutParams.width = params.width;
		aLayoutParams.height = params.height;
		mImageView.setLayoutParams(aLayoutParams);

		this.requestLayout();
	}

	public void clearImage() {
		this.mImageView.setImageBitmap(null);
	}

	public void showDel() {
		this.mDelImageView.setVisibility(View.VISIBLE);
	}

	public boolean isShowDel() {
		return this.mDelImageView.getVisibility() == View.VISIBLE;
	}

	public void reusmeDel() {
		this.mDelImageView.setVisibility(View.GONE);
	}

	public void setOnDelListener(final OnClickListener aListener) {
		this.mDelImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				aListener.onClick(DelImageView.this);
			}
		});
	}

	// 获取相册图片，并根据图片大小进行压缩
	private Bitmap getCameraBitmapFromPath(Context context, String photoUriPath)
			throws IOException {
		File imageFile = new File(photoUriPath);
		int inSampleSize = 8;
		long filesize = imageFile.length();
		Bitmap photoBitmap = null;
		if (filesize < 102400) {
			inSampleSize = 1;
		} else if (filesize < 1024000) {
			inSampleSize = 4;
		} else if (filesize < 3024000) {
			inSampleSize = 6;
		} else {
			inSampleSize = (int) filesize / 12800;
		}
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = inSampleSize;
		photoBitmap = BitmapFactory.decodeFile(photoUriPath, options);
		return photoBitmap;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return super.onInterceptTouchEvent(ev);
	}

}
