package com.ilikezhibo.ggzb.views;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import com.ilikezhibo.ggzb.BaseDialog;
import com.ilikezhibo.ggzb.R;

public class ModifyAvatarDialog extends BaseDialog implements OnClickListener {

	private boolean isSetBg;
	private boolean isAgainUp;

	private LayoutInflater factory;

	private Button setDefaultBtn;
	private LinearLayout lineLayout;
	private Button mImg;
	private Button mPhone;
	private Button mCancel;

	private LinearLayout againUpLayout;
	private Button againUpBtn;
	private LinearLayout delLayout;
	private Button delBtn;

	private DealPhotoListener listener;

	public ModifyAvatarDialog(Context context, boolean isSetBg,
			boolean isAgainUp) {
		super(context);
		factory = LayoutInflater.from(context);
		this.isSetBg = isSetBg;
		this.isAgainUp = isAgainUp;
	}

	public void setListener(DealPhotoListener listener) {
		this.listener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(factory.inflate(
				R.layout.gl_modify_avatar_choose_dialog, null));
		mImg = (Button) this.findViewById(R.id.gl_choose_img);
		mPhone = (Button) this.findViewById(R.id.gl_choose_phone);
		mCancel = (Button) this.findViewById(R.id.gl_choose_cancel);
		mImg.setOnClickListener(this);
		mPhone.setOnClickListener(this);
		mCancel.setOnClickListener(this);
		setDefaultBtn = (Button) findViewById(R.id.button_one);
		lineLayout = (LinearLayout) findViewById(R.id.lineImg);

		againUpLayout = (LinearLayout) findViewById(R.id.againUpLineImg);
		againUpBtn = (Button) findViewById(R.id.againUpBtn);
		delLayout = (LinearLayout) findViewById(R.id.deleteLocalLineImg);
		delBtn = (Button) findViewById(R.id.deleteLocalBtn);
		if (isAgainUp) {
			againUpLayout.setVisibility(View.VISIBLE);
			againUpBtn.setVisibility(View.VISIBLE);
			againUpBtn.setOnClickListener(this);
			delBtn.setVisibility(View.VISIBLE);
			delLayout.setVisibility(View.VISIBLE);
			delBtn.setOnClickListener(this);
		}

		if (isSetBg) {
			setDefaultBtn.setOnClickListener(this);
			setDefaultBtn.setVisibility(View.VISIBLE);
			lineLayout.setVisibility(View.VISIBLE);
		} else {
			setDefaultBtn.setVisibility(View.GONE);
			lineLayout.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_one:
			setDefaultImg();
			break;
		case R.id.gl_choose_img:
			doGoToImg();
			break;
		case R.id.gl_choose_phone:
			doGoToPhone();
			break;
		case R.id.gl_choose_cancel:
			dismiss();
			break;
		case R.id.deleteLocalBtn:
			listener.onDelPhoto();
			break;
		case R.id.againUpBtn:
			listener.onAgainUploadPhoto();
			break;
		}
	}

	public void setDelVisible() {
		delBtn.setVisibility(View.VISIBLE);
		delLayout.setVisibility(View.VISIBLE);
		delBtn.setOnClickListener(this);
	}

	public void setAgainUploadVisible() {
		againUpLayout.setVisibility(View.VISIBLE);
		againUpBtn.setVisibility(View.VISIBLE);
		againUpBtn.setOnClickListener(this);
	}

	public void setDelGone() {
		delBtn.setVisibility(View.GONE);
		delLayout.setVisibility(View.GONE);
	}

	public void setAgainUploadGone() {
		againUpLayout.setVisibility(View.GONE);
		againUpBtn.setVisibility(View.GONE);
	}

	public void doGoToImg() {
	}

	public void doGoToPhone() {
	}

	public void setDefaultImg() {

	}

	public interface DealPhotoListener {
		void onAgainUploadPhoto();

		void onDelPhoto();
	}
}
