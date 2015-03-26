package com.example.photopicker.views.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.example.photopicker.R;
import com.example.photopicker.models.PhotoInfo;
import com.example.photopicker.util.ThumbnailsUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PhotoPickerAdapter extends BaseAdapter {

	DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.lp_img_photo_default)
			.showImageForEmptyUri(R.drawable.lp_img_photo_nophoto)
			.showImageOnFail(R.drawable.lp_img_photo_loadfail)
			.cacheInMemory(true).considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565).build();

	protected ImageLoader imageLoader = ImageLoader.getInstance();

	/** 是否可以继续选择图片 */
	private boolean mCanSelectMore = true;
	private Context mContext;
	private List<PhotoInfo> dataList;
	private ArrayList<String> mSelectedImgIdList;

	private int mWidth;

	public PhotoPickerAdapter(Context c, List<PhotoInfo> dataList) {

		mContext = c;
		this.dataList = dataList;
		mWidth = (getWidth() - dip2px(10)) / 4;
	}

	public void setmCanSelectMore(boolean canable) {
		this.mCanSelectMore = canable;
	}

	public void setSelectedImgIdList(ArrayList<String> aSelectedImgIdList) {
		this.mSelectedImgIdList = aSelectedImgIdList;
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 存放列表项控件句柄
	 */
	private class ViewHolder {
		public ImageView imageView;
		public View tgView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.lp_adapter_photogrid, parent, false);
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.image_view);
			LayoutParams lp = new LayoutParams(mWidth, mWidth);
			viewHolder.imageView.setLayoutParams(lp);
			viewHolder.tgView = (View) convertView
					.findViewById(R.id.toggle_view);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		PhotoInfo item = null;
		if (dataList != null && dataList.size() > position) {
			item = dataList.get(position);
		}
		if (item == null) {
			viewHolder.imageView
					.setImageResource(R.drawable.lp_img_photo_default);
		} else {
			String displayItemUri = ThumbnailsUtil.MapgetHashValue(
					item.getImageId(), item.getPathFile());
			imageLoader.displayImage(displayItemUri, viewHolder.imageView,
					defaultOptions);
		}

		boolean hasSelect = mSelectedImgIdList.contains(item.getPathAbsolute());

		boolean flag = item.getIsSelected() || hasSelect;
		viewHolder.tgView.setSelected(flag);
		if (flag) {
			viewHolder.tgView.setVisibility(View.VISIBLE);
		} else {
			viewHolder.tgView.setVisibility(View.GONE);
		}
		return convertView;
	}

	/**
	 * 修改选中状态
	 * */
	public void changeSelection(GridView gridView, int position) {

		int firstPosition = gridView.getFirstVisiblePosition();
		int lastPosition = gridView.getLastVisiblePosition();

		if (position < firstPosition && position > lastPosition) {
			return;
		}

		View v = gridView.getChildAt(position - firstPosition);
		if (v == null) {
			return;
		}

		ViewHolder mHolder = (ViewHolder) v.getTag();
		if (mHolder == null)
			return;

		boolean flag = dataList.get(position).getIsSelected();
		if (flag) {
			dataList.get(position).setSelected(false);
			mHolder.tgView.setVisibility(View.VISIBLE);
		} else {
			if (!mCanSelectMore) {
				Toast.makeText(mContext, "超过限制", Toast.LENGTH_SHORT).show();
			} else {
				dataList.get(position).setSelected(true);
				mHolder.tgView.setVisibility(View.VISIBLE);
			}
		}
		mHolder.tgView.setSelected(dataList.get(position).getIsSelected());
	}

	private int getWidth() {
		return mContext.getApplicationContext().getResources()
				.getDisplayMetrics().widthPixels;
	}

	private int dip2px(float dpValue) {
		final float scale = mContext.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
}
