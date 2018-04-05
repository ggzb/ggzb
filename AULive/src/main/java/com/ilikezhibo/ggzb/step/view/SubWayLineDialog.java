package com.ilikezhibo.ggzb.step.view;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.step.ProjectTypeEntity;

/**
 * 自定义对话框
 * 
 * @author
 * 
 */
public class SubWayLineDialog extends Dialog {

	private SubWayDialogListener listener = null;
	private boolean isCancelable = true;
	private ArrayList<ProjectTypeEntity> types = null;
	private Context context;

	public SubWayLineDialog(Context context, SubWayDialogListener listener,
			ArrayList<ProjectTypeEntity> types) {
		super(context, R.style.Theme_dialog);
		this.listener = listener;
		this.types = types;
		this.context = context;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.dialog_subway_line);

			LinearLayout item_container = (LinearLayout) findViewById(R.id.item_container);

			for (ProjectTypeEntity type : types) {
				final ProjectTypeEntity type_final = type;
				View view = LayoutInflater.from(context).inflate(
						R.layout.dialog_subway_line_item, null);
				TextView textView = (TextView) view.findViewById(R.id.text);
				textView.setText(type_final.getName());
				item_container.addView(view);
				view.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view) {

						if (listener != null)
							listener.onItemClick(type_final);
					}
				});
			}

			setCanceledOnTouchOutside(true);
		} catch (InflateException e) {
			e.printStackTrace();
		}
	}

	public void setCancelable(boolean isCancelable) {
		this.isCancelable = isCancelable;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_UP) {
				if (isCancelable) {
					dismiss();
					return false;
				} else {
					return true;
				}
			} else {
				if (isCancelable) {
					return super.onKeyDown(keyCode, event);
				} else {
					return true;
				}
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
