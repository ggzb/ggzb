package com.ilikezhibo.ggzb.step.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ilikezhibo.ggzb.R;

public class ListDialogAdapter extends BaseAdapter {

	private boolean isItemHorizontalCenter = false;
	private String[] items = { "取消" };
	// private Drawable[] iconDrawable = null;
	private int[] itemResId = null;

	private Context context;

	private class EvianNewsItemView {
		AlwaysMarqueeTextView title;
		ImageView icon;
	}

	public ListDialogAdapter(Context c, String[] items, int[] itemResId,
			boolean isItemHorizontalCentre) {
		context = c;
		this.items = items;
		this.itemResId = itemResId;
		this.isItemHorizontalCenter = isItemHorizontalCentre;
	}

	public int getCount() {
		return items.length;
	}

	public Object getItem(int arg0) {
		return items[arg0];
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	public View getView(int arg0, View arg1, ViewGroup arg2) {
		final EvianNewsItemView itemView;
		if (arg1 == null) {
			itemView = new EvianNewsItemView();
			if (isItemHorizontalCenter) {
				arg1 = LayoutInflater.from(context).inflate(
						R.layout.custom_dialog_list_item_center, null);
			} else {
				arg1 = LayoutInflater.from(context).inflate(
						R.layout.custom_dialog_list_item, null);
			}
			itemView.title = (AlwaysMarqueeTextView) arg1
					.findViewById(R.id.item);
			itemView.icon = (ImageView) arg1.findViewById(R.id.icon);
			arg1.setTag(itemView);
		} else {
			itemView = (EvianNewsItemView) arg1.getTag();
		}

		// icon的处理
		if (itemResId != null && itemResId[arg0] != 0) {
			itemView.icon.setVisibility(View.VISIBLE);
			itemView.icon.setBackgroundResource(itemResId[arg0]);
		} else if (itemResId != null) {
			itemView.icon.setVisibility(View.GONE);
		}

		itemView.title.setText(items[arg0]);
		// // 字体大小
		// float fontSize = itemView.title.getTextSize();
		// // 获得字符串长度
		// int stringLen = TextUtil.getStrWidth(items[arg0], fontSize);
		// // 得窗口的长度
		// int dialogWidth = itemView.title.getMeasuredWidth();

		return arg1;
	}
}
