package com.example.photopicker.views.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.example.photopicker.R;
import com.example.photopicker.activities.ShowSelectedActivity;
import com.example.photopicker.models.PhotoInfo;
import com.example.photopicker.models.PhotoList;
import com.example.photopicker.views.adapters.PhotoPickerAdapter;
import com.example.photopicker.views.widgets.DelImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 相片列表
 * */
public class PhotoPickerFragment extends Fragment implements
		OnItemClickListener, OnClickListener {

	private GridView gridView;
	private List<PhotoInfo> mSelectedList;
	private List<PhotoInfo> dataList;
	private ArrayList<String> mSelectedImgIdList;
	private PhotoPickerAdapter gridImageAdapter;

	/** 底部已选择图片 */
	private View mBottomView;
	/** 完成按钮 */
	private Button mCompleteBt;
	/** 以选择图片列表 */
	private LinearLayout mSelectedImgslay;
	/** 选图数目现实 */
	private final int mMaxLimited = 6;
	/** 选图完成点击监听 */
	private OnPhotoSelectClickListener mOnPhotoSelectClickListener;

	protected ImageLoader imageLoader = ImageLoader.getInstance();

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (onPhotoSelectClickListener == null) {
			onPhotoSelectClickListener = (OnPhotoSelectClickListener) activity;
		}
	}

	public void setOnPhotoSelectClickListener(
			OnPhotoSelectClickListener aOnPhotoSelectClickListener) {
		this.mOnPhotoSelectClickListener = aOnPhotoSelectClickListener;
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.lp_fragment_photopicker,
				container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Bundle args = getArguments();

		// 文件夹照片列表;
		PhotoList photoSerializable = (PhotoList) args.getSerializable("list");
		dataList = new ArrayList<PhotoInfo>();
		dataList.addAll(photoSerializable.getList());

		// 过滤以选择图片
		filterSelectedImgs();
		init();
		initSelectedLay();
	}

	private void filterSelectedImgs() {
		// mSelectedList = Config.mSelectedImgs;
		mSelectedList = new ArrayList<PhotoInfo>();
		mSelectedImgIdList = new ArrayList<String>();
		for (int i = 0; i < mSelectedList.size(); i++) {
			mSelectedImgIdList.add(mSelectedList.get(i).getPathAbsolute());
		}
	}

	public void updateDataList(List<PhotoInfo> newList) {
		if (dataList == newList) {
			return;
		}
		dataList.clear();
		dataList.addAll(newList);
		gridImageAdapter.notifyDataSetChanged();
	}

	private void init() {
		View v = getView();
		gridView = (GridView) v.findViewById(R.id.myGrid);
		gridImageAdapter = new PhotoPickerAdapter(getActivity(), dataList);
		gridView.setOnItemClickListener(this);
		gridView.setAdapter(gridImageAdapter);
		gridImageAdapter.setSelectedImgIdList(mSelectedImgIdList);
		mBottomView = v.findViewById(R.id.bottom_layout);
		mCompleteBt = (Button) mBottomView.findViewById(R.id.ok_button);
		mSelectedImgslay = (LinearLayout) mBottomView
				.findViewById(R.id.selected_image_layout);
		mCompleteBt.setOnClickListener(this);
	}

	/**
	 * 刷新以选择图片列表
	 * */
	private void initSelectedLay() {
		// TODO Auto-generated method stub
		mSelectedImgslay.removeAllViews();
		for (int i = 0; i < mSelectedList.size(); i++) {
			DelImageView aView = new DelImageView(getActivity());
			aView.setTag(i);
			LayoutParams params = new LayoutParams(dip2px(72), dip2px(72));
			aView.setDelLayoutParams(params);
			aView.setImageResource(mSelectedList.get(i).getPathAbsolute());
			mSelectedImgslay.addView(aView);
		}
		refreshCompleteBt();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		boolean isSelectNew = !dataList.get(position).getIsSelected();
		gridImageAdapter.changeSelection(gridView, position);
		if (dataList.get(position).getIsSelected()) {
			refreshSelectedLay(true, dataList.get(position));
		} else if (isSelectNew && mSelectedList.size() == mMaxLimited) {
			return;
		} else {
			refreshSelectedLay(false, dataList.get(position));
		}
	}

	/**
	 * 刷新已选择图片列表
	 * */
	private void refreshSelectedLay(boolean isAdd, PhotoInfo photoInfo) {
		if (isAdd) {
			mBottomView.setVisibility(View.VISIBLE);
			DelImageView aView = new DelImageView(getActivity());
			aView.setTag(mSelectedList.size());
			LayoutParams params = new LayoutParams(dip2px(70), dip2px(70));
			aView.setDelLayoutParams(params);
			aView.setImageResource(photoInfo.getPathAbsolute());
			mSelectedImgslay.addView(aView);
			mSelectedList.add(photoInfo);

			aView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent aIntent = new Intent(getActivity(),
							ShowSelectedActivity.class);
					aIntent.putExtra("position", (Integer) v.getTag());
					mSelectedImgIdList = new ArrayList<String>();
					for (int i = 0; i < mSelectedList.size(); i++) {
						mSelectedImgIdList.add(mSelectedList.get(i)
								.getPathAbsolute());
					}
					aIntent.putStringArrayListExtra("data", mSelectedImgIdList);
					getActivity().startActivity(aIntent);
				}
			});

		} else {
			mBottomView.setVisibility(View.VISIBLE);
			int index = mSelectedList.indexOf(photoInfo);
			mSelectedList.remove(index);
			mSelectedImgslay.removeViewAt(index);
		}
		refreshCompleteBt();
		gridImageAdapter.setmCanSelectMore(mSelectedList.size() < mMaxLimited);
	}

	/**
	 * 刷新以选择图片数目
	 * */
	private void refreshCompleteBt() {
		mCompleteBt.setText("完成(" + mSelectedList.size() + "/" + mMaxLimited
				+ ")");
	}

	public int dip2px(float dpValue) {
		final float scale = getActivity().getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public ArrayList<String> getSelectedImgs() {
		ArrayList<String> photoList = new ArrayList<String>();
		if (mSelectedList == null)
			return photoList;
		for (int i = 0; i < this.mSelectedList.size(); i++) {
			photoList.add(this.mSelectedList.get(i).getPathAbsolute());
		}
		return photoList;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.ok_button && mOnPhotoSelectClickListener != null) {
			mOnPhotoSelectClickListener.onOKClickListener();
		}
	}

	public interface OnPhotoSelectClickListener {
		public void onOKClickListener();
	}

	private OnPhotoSelectClickListener onPhotoSelectClickListener;

}
