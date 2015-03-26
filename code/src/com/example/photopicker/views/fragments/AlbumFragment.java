package com.example.photopicker.views.fragments;

import java.util.ArrayList;
import java.util.List;

import com.example.photopicker.R;
import com.example.photopicker.models.AlbumInfo;
import com.example.photopicker.models.AlbumList;
import com.example.photopicker.models.PhotoInfo;
import com.example.photopicker.views.adapters.AlbumAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * @author chengyanfang
 * 
 *         相册列表页
 * 
 */
public class AlbumFragment extends Fragment {

	public interface OnAlbumClickedListener {
		public void onAlbumClickedListener(String albumName,
				List<PhotoInfo> list);
	}

	private OnAlbumClickedListener onPageLodingClickListener;
	private ListView listView;
	private List<AlbumInfo> listAlbumInfo = new ArrayList<AlbumInfo>();
	private AlbumAdapter listAdapter;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (onPageLodingClickListener == null) {
			onPageLodingClickListener = (OnAlbumClickedListener) activity;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.lp_fragment_album, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Bundle args = getArguments();

		AlbumList photoAlbumSerializable = (AlbumList) args
				.getSerializable("list");
		listAlbumInfo.clear();
		listAlbumInfo.addAll(photoAlbumSerializable.getList());

		listView = (ListView) getView().findViewById(R.id.lv_album_list);

		if (getActivity() != null) {
			listAdapter = new AlbumAdapter(getActivity(), listAlbumInfo);
			listView.setAdapter(listAdapter);
		}

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				onPageLodingClickListener.onAlbumClickedListener(listAlbumInfo
						.get(arg2).getName_album(), listAlbumInfo.get(arg2)
						.getList());
			}
		});
	}

}
