package com.ilikezhibo.ggzb.photos.photobrowser;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.photos.photoview.PhotoView;

/**
 * @ClassName: ChatBrowseAdapter
 * @Description: 浏览图片适配器
 * @author big
 * @date 2014-6-11 下午4:33:49
 * 
 */
public class PicBrowseAdapter extends PagerAdapter {
	private Context mContext;

	private ArrayList<String> entities;
	private ArrayList<PhotoView> photoViews = new ArrayList<PhotoView>();

	public PicBrowseAdapter(Context mContext, ArrayList<String> entities) {

		this.mContext = mContext;
		this.entities = entities;
	}

	@Override
	public int getCount() {
		return entities.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public View instantiateItem(ViewGroup container, int position) {

		PhotoView photoView = new PhotoView(mContext);
		photoViews.add(photoView);
		container.addView(photoView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		try {
			String url = entities.get(position);

			if (url != null) {
				ImageLoader.getInstance().displayImage(url, photoView,
						AULiveApplication.getGlobalImgOptions());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return photoView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	// 每次都刷新内容
	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}
}
