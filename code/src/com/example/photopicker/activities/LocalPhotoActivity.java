package com.example.photopicker.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.photopicker.R;
import com.example.photopicker.models.AlbumInfo;
import com.example.photopicker.models.AlbumList;
import com.example.photopicker.models.PhotoInfo;
import com.example.photopicker.models.PhotoList;
import com.example.photopicker.util.ThumbnailsUtil;
import com.example.photopicker.views.fragments.AlbumFragment;
import com.example.photopicker.views.fragments.AlbumFragment.OnAlbumClickedListener;
import com.example.photopicker.views.fragments.PhotoPickerFragment;
import com.example.photopicker.views.fragments.PhotoPickerFragment.OnPhotoSelectClickListener;

/**
 * @author chengyanfang
 * 
 *         本地图片选择类
 * */
public class LocalPhotoActivity extends FragmentActivity implements
		OnAlbumClickedListener, OnPhotoSelectClickListener {
	/** 标题 */
	private TextView mTitleTextView;
	/** 左侧取消返回箭头 */
	private TextView mTvLeftArrowBtn;
	/** 右侧取消标题 */
	private TextView mTvRightCancelBtn;
	/** 左侧返回标题 */
	private TextView mTvLeftCancleBtn;
	/** 相册列表界面 */
	private AlbumFragment mPhotoFolderFragment;
	/** 图片列表界面 */
	private PhotoPickerFragment mPhotoPickerFragment;
	/** 当前界面 */
	private Fragment mCurrentFragment;
	/** 相册信息列表 */
	private List<AlbumInfo> mListImageInfo = new ArrayList<AlbumInfo>();
	/** 当前选中相册名称 */
	private String mAlbumName = null;

	private FragmentManager mFragmentManager;
	private ContentResolver mContentResolver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.lp_activity_localphoto);

		setTitleView();

		mFragmentManager = getSupportFragmentManager();

		mPhotoFolderFragment = new AlbumFragment();
		mPhotoPickerFragment = new PhotoPickerFragment();
		mPhotoPickerFragment.setOnPhotoSelectClickListener(this);

		mContentResolver = getContentResolver();
		mListImageInfo.clear();

		mAlbumName = getIntent().getStringExtra("albumName");
		new ImageLoadAsyncTask().execute();
	}

	/**
	 * titlebar
	 * */
	private void setTitleView() {
		mTitleTextView = (TextView) findViewById(R.id.tvTitleName);
		mTitleTextView.setText("选择相册");
		mTvLeftArrowBtn = (TextView) findViewById(R.id.tvTitleArrowBtnLeft);
		mTvLeftCancleBtn = (TextView) findViewById(R.id.tvTitleBtnLeftButton);
		mTvLeftCancleBtn.setText("相册");
		mTvLeftCancleBtn.setVisibility(View.GONE);
		mTvLeftArrowBtn.setVisibility(View.GONE);
		mTvLeftCancleBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showPhotoAlbumFragment();
			}
		});
		mTvLeftArrowBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showPhotoAlbumFragment();
			}
		});
		mTvRightCancelBtn = (TextView) findViewById(R.id.tvTitleBtnRightButton);
		mTvRightCancelBtn.setText("取消");
		mTvRightCancelBtn.setVisibility(View.VISIBLE);
		mTvRightCancelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				LocalPhotoActivity.this.finish();
			}
		});
	}

	// 在相册选择界面选择了某个相册后回调该方法 ;
	@Override
	public void onAlbumClickedListener(String albumName, List<PhotoInfo> list) {
		// 替换当前的Fragment;
		showPhotoPickerFragment(albumName, list);
	}

	// 显示某个相册的照片列表Fragment;
	private void showPhotoPickerFragment(String albumName, List<PhotoInfo> list) {
		// 显示左边按钮;
		mTvLeftCancleBtn.setVisibility(View.VISIBLE);
		mTvLeftArrowBtn.setVisibility(View.VISIBLE);
		mTitleTextView.setText(albumName);

		FragmentTransaction transaction = mFragmentManager.beginTransaction()
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		// 替换当前的Fragment;
		if (!mPhotoPickerFragment.isAdded()) { // 先判断是否被add过,如果没有add过,直接add这个fragment;
			Bundle args = new Bundle();
			// 这个每次进入前都要把文件夹照片列表发送给photopickfragment;
			PhotoList photoSerializable = new PhotoList();
			photoSerializable.setList(list);
			args.putSerializable("list", photoSerializable);
			mPhotoPickerFragment.setArguments(args);
			transaction.hide(mPhotoFolderFragment)
					.add(R.id.fragment_container, mPhotoPickerFragment)
					.commit(); // 隐藏当前的fragment，add下一个到Activity中
		} else { // 如果已经add过,隐藏这个fragment,并且显示下一个fragment;
			mPhotoPickerFragment.updateDataList(list);
			transaction.hide(mPhotoFolderFragment).show(mPhotoPickerFragment)
					.commit(); // 隐藏当前的fragment，显示下一个
		}
		mCurrentFragment = mPhotoPickerFragment;
	}

	// 显示相册列表界面;
	private void showPhotoAlbumFragment() {
		mTvLeftArrowBtn.setVisibility(View.GONE);
		mTvLeftCancleBtn.setVisibility(View.GONE);
		mTitleTextView.setText("选择相册");

		FragmentTransaction transaction = mFragmentManager.beginTransaction()
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		if (!mPhotoFolderFragment.isAdded()) {
			transaction.hide(mPhotoPickerFragment)
					.add(R.id.fragment_container, mPhotoFolderFragment)
					.commit();
		} else {
			transaction.hide(mPhotoPickerFragment).show(mPhotoFolderFragment)
					.commit();
		}
		mCurrentFragment = mPhotoFolderFragment;
	}

	/*
	 * 在照片选择界面点击选图完成后回调该方法
	 */
	@Override
	public void onOKClickListener() {
		setResult(RESULT_OK);
		finish();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_CANCELED) {
			// 如果相册选择被caceled,那么结束自己直接回到主activity;
			finish();
			return;
		}
		// 根据上面发送过去的请求码来区别
		switch (requestCode) {
		case 50001:
			break;
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		// 如果当前是选择相册界面按了返回键,直接退出activity;
		if (mCurrentFragment == mPhotoFolderFragment) {
			setResult(RESULT_CANCELED);
			finish();
		} else {
			// 如果是选择照片界面按了返回键,退回到相册选择界面; 替换当前的Fragment;
			showPhotoAlbumFragment();
		}
	}

	@Override
	public void finish() {
		super.finish();
		this.overridePendingTransition(R.anim.push_stay, R.anim.push_bottom_out);
	}

	private class ImageLoadAsyncTask extends AsyncTask<Void, Void, Object> {
		@Override
		protected Object doInBackground(Void... params) {
			// 获取缩略图
			ThumbnailsUtil.clear();
			String[] projection = { Thumbnails._ID, Thumbnails.IMAGE_ID,
					Thumbnails.DATA };
			Cursor cur = mContentResolver.query(
					Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null,
					null);

			if (cur != null && cur.moveToFirst()) {
				int image_id;
				String image_path;
				int image_idColumn = cur.getColumnIndex(Thumbnails.IMAGE_ID);
				int dataColumn = cur.getColumnIndex(Thumbnails.DATA);
				do {
					image_id = cur.getInt(image_idColumn);
					image_path = cur.getString(dataColumn);
					ThumbnailsUtil.put(image_id, "file://" + image_path);
				} while (cur.moveToNext());
			}

			cur.close();
			// 获取原图
			Cursor cursor = mContentResolver.query(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null,
					null, "date_modified DESC");

			String _path = "_data";
			String _album = "bucket_display_name";

			HashMap<String, AlbumInfo> myhash = new HashMap<String, AlbumInfo>();
			AlbumInfo albumInfo = null;
			PhotoInfo photo = null;
			if (cursor != null && cursor.moveToFirst()) {
				do {
					int index = 0;
					int _id = cursor.getInt(cursor.getColumnIndex("_id"));
					String path = cursor
							.getString(cursor.getColumnIndex(_path));
					String album = cursor.getString(cursor
							.getColumnIndex(_album));
					List<PhotoInfo> stringList = new ArrayList<PhotoInfo>();
					photo = new PhotoInfo();
					if (myhash.containsKey(album)) {
						albumInfo = myhash.remove(album);
						if (mListImageInfo.contains(albumInfo))
							index = mListImageInfo.indexOf(albumInfo);
						photo.setImageId(_id);
						photo.setPathFile("file://" + path);
						photo.setPathAbsolute(path);
						albumInfo.getList().add(photo);
						mListImageInfo.set(index, albumInfo);
						myhash.put(album, albumInfo);
					} else {
						albumInfo = new AlbumInfo();
						stringList.clear();
						photo.setImageId(_id);
						photo.setPathFile("file://" + path);
						photo.setPathAbsolute(path);
						stringList.add(photo);
						albumInfo.setImage_id(_id);
						albumInfo.setPath_file("file://" + path);
						albumInfo.setPath_absolute(path);
						albumInfo.setName_album(album);
						albumInfo.setList(stringList);
						mListImageInfo.add(albumInfo);
						myhash.put(album, albumInfo);
					}
				} while (cursor.moveToNext());
				cursor.close();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			List<PhotoInfo> listInit = null;
			if (mAlbumName != null && mAlbumName.length() > 0) {
				for (AlbumInfo ai : mListImageInfo) {
					if (ai.getName_album().equals(mAlbumName)) {
						listInit = ai.getList();
						break;
					}
				}
			}
			Bundle args = new Bundle();
			AlbumList photoSerializable = new AlbumList();
			photoSerializable.setList(mListImageInfo);
			args.putSerializable("list", photoSerializable);
			mPhotoFolderFragment.setArguments(args);
			FragmentTransaction transaction = mFragmentManager
					.beginTransaction();
			transaction.add(R.id.fragment_container, mPhotoFolderFragment);
			transaction.commit();

			mCurrentFragment = mPhotoFolderFragment;
			if (listInit != null) {
				showPhotoPickerFragment(mAlbumName, listInit);
			}
		}
	}

}
