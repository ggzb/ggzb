package com.ilikezhibo.ggzb.step.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.step.UpLoadFileEntity;

/**
 * @ClassName: JingJiaAddAdapter
 * @Description: JingJiaAddAdapter
 * @author big
 * @date 2014-3-27 下午4:12:57
 * 
 */
public class FileAddAdapter extends BaseAdapter {
	private Context context;

	private ViewHolder holder;

	private ArrayList<UpLoadFileEntity> entities;

	public FileAddAdapter(Context context) {
		this.context = context;
	}

	public void setEntities(ArrayList<UpLoadFileEntity> entities) {
		this.entities = entities;
	}

	@Override
	public int getCount() {
		if (entities != null && entities.size() > 0) {
			return entities.size();
		}

		return 0;
	}

	@Override
	public Object getItem(int position) {

		return entities.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null || convertView.getTag() == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.shaiwu_gridview_item, null);
			holder.headImg = (ImageView) convertView
					.findViewById(R.id.content_image);
			holder.file_name = (TextView) convertView
					.findViewById(R.id.file_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String url = entities.get(position).getUrl();

		if (url != null) {
			if (url.endsWith("doc") || url.endsWith("docx")
					|| url.endsWith("ppt") || url.endsWith("pptx")
					|| url.endsWith("xls") || url.endsWith("xlsx")) {
				holder.headImg.setImageResource(R.drawable.fileadd_wps);
			} else if (url.endsWith("zip")) {
				holder.headImg.setImageResource(R.drawable.fileadd_zip);
			} else if (url.endsWith("rar")) {
				holder.headImg.setImageResource(R.drawable.fileadd_rar);
			} else if (url.endsWith("pdf")) {
				holder.headImg.setImageResource(R.drawable.fileadd_rar);
			} else if (url.endsWith("txt")) {
				holder.headImg.setImageResource(R.drawable.fileadd_txt);
			} else {
				holder.headImg.setImageResource(R.drawable.fileadd_wps);
			}
		}
		if (url.length() > 10) {
			holder.file_name.setVisibility(View.VISIBLE);
			holder.file_name.setText(url.subSequence(url.length() - 10,
					url.length()));
		}
		// ImageLoader.getInstance().displayImage(,
		// holder.headImg, AULiveApplication.getGlobalImgOptions());
		return convertView;
	}

	class ViewHolder {
		ImageView headImg;
		TextView file_name;
	}
}
