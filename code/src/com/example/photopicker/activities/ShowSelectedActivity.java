package com.example.photopicker.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.photopicker.R;
import com.example.photopicker.views.widgets.HackyViewPager;
import com.example.photopicker.views.widgets.PhotoView.PhotoView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class ShowSelectedActivity extends Activity {

	HackyViewPager mHackyViewPager;

	DisplayImageOptions mOptions;

	private ArrayList<String> mDatas;
	private int mPagerPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lp_activity_showselectimg);
		initUI();
		initData();
		initEvent();
	}

	private void initUI() {
		mHackyViewPager = (HackyViewPager) findViewById(R.id.viewpage);
	}

	@SuppressWarnings("unchecked")
	private void initData() {
		// Options
		mOptions = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.imageScaleType(ImageScaleType.NONE)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		// Data
		ArrayList<String> data = (ArrayList<String>) this.getIntent()
				.getSerializableExtra("data");
		this.mDatas = data;
		this.mPagerPosition = this.getIntent().getIntExtra("position", 0);
	}

	private void initEvent() {
		mHackyViewPager.setAdapter(new ImagePagerAdapter(this.mDatas));
		mHackyViewPager.setCurrentItem(mPagerPosition);
	}

	private class ImagePagerAdapter extends PagerAdapter {

		private ArrayList<String> images;
		private LayoutInflater inflater;

		public ImagePagerAdapter(ArrayList<String> images) {
			this.images = images;
			inflater = getLayoutInflater();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}

		@Override
		public void finishUpdate(View container) {
		}

		@Override
		public int getCount() {
			return images.size();
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			View imageLayout = inflater.inflate(
					R.layout.lp_adapter_selectedimgs, container, false);
			final PhotoView imageView = (PhotoView) imageLayout
					.findViewById(R.id.image);

			ImageLoader.getInstance().displayImage(
					"file://" + this.images.get(position), imageView, mOptions);
			((ViewPager) container).addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View container) {
		}

	}

}
