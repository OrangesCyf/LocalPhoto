package com.example.photopicker.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.photopicker.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initImageEngine();
		Intent aIntent = new Intent(this,
				com.example.photopicker.activities.LocalPhotoActivity.class);
		this.startActivity(aIntent);
	}

	private void initImageEngine() {

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).denyCacheImageMultipleSizesInMemory()
				.diskCacheFileCount(1000)
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.threadPoolSize(3).threadPriority(Thread.NORM_PRIORITY - 2)
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.memoryCache(new LruMemoryCache(5 * 1024 * 1024)).build();

		ImageLoader.getInstance().init(config);
	}

}
