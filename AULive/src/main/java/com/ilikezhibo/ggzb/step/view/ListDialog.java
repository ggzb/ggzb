package com.ilikezhibo.ggzb.step.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ilikezhibo.ggzb.R;

/**
 * 自定义列表对话框
 * 
 * @author jack.long
 * 
 */
public class ListDialog extends Dialog implements OnItemClickListener,
		OnItemSelectedListener {
	private int iconId = android.R.drawable.ic_dialog_info;
	private String titleStr = "";
	private ListView listView = null;
	private Context context = null;
	private OnListDialogItemClickListener listener = null;
	private boolean isItemHorizontalCentre = false;// item是否水平居中
	// private Drawable[] iconDrawable = null;
	private int[] itemResId = null;
	private String[] items = null;
	private ListDialogAdapter adapter = null;

	private ImageView IconImg = null;

	public ListDialog(Context context, OnListDialogItemClickListener listener) {
		super(context, R.style.Theme_Light_FullScreenDialogAct);
		this.context = context;
		this.listener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.custom_dialog_list);

		init();
	}

	/**
	 * 设置ITEM是否水平居中
	 * 
	 * @param isItemHorizontalCentre
	 */
	public void setIsItemHorizontalCentre(boolean isItemHorizontalCentre) {
		this.isItemHorizontalCentre = isItemHorizontalCentre;
	}

	/**
	 * 设置列表项
	 * 
	 * @param items
	 */
	public void setItems(String[] items) {
		this.items = items;
	}

	public void setIconIds(int[] itemResId) {
		this.itemResId = itemResId;
	}

	public void setIcon(int iconId) {
		this.iconId = iconId;
	}

	public void setIconVisibility(int visibility) {
		switch (visibility) {
		case View.VISIBLE:
			IconImg.setVisibility(View.VISIBLE);
			break;
		case View.INVISIBLE:
			IconImg.setVisibility(View.INVISIBLE);
			break;
		case View.GONE:
			IconImg.setVisibility(View.GONE);
			break;
		}

	}

	public void setTitle(String titleStr) {
		this.titleStr = titleStr;
	}

	public void setOnListDialogItemClickListener(
			OnListDialogItemClickListener listener) {
		this.listener = listener;
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		this.dismiss();
		if (listener != null) {
			listener.onItemClicked(arg2);
		}
	}

	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		LinearLayout layout = (LinearLayout) arg1;
		if (layout.getChildAt(0) instanceof TextView) {
			TextView tv = (TextView) layout.getChildAt(0);
			tv.setTextColor(Color.WHITE);
		}
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
	}

	private void init() {
		IconImg = (ImageView) findViewById(R.id.icon);
		IconImg.setBackgroundResource(iconId);

		TextView title = (TextView) findViewById(R.id.title);
		title.setText(titleStr);

		if (adapter == null) {
			adapter = new ListDialogAdapter(context, items, itemResId,
					isItemHorizontalCentre);
		}

		listView = (ListView) findViewById(R.id.itemListView);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		listView.setOnItemSelectedListener(this);
	}
}
