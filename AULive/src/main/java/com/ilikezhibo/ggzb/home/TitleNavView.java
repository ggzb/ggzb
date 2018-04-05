/**   
 * @Title: TitleNavView.java 
 * @Package com.qixi.ksong.home 
 * @Description: TODO
 * @author jack.long
 * @date 2013-9-23 下午9:22:17 
 * @version    
 */
package com.ilikezhibo.ggzb.home;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.ilikezhibo.ggzb.R;

/**
 * @ClassName: TitleNavView
 * @Description: 头部view
 * @author big
 * @date 2013-9-23 下午9:22:17
 * 
 */
public class TitleNavView {
	private TitleListener listener;
	private TextView titleNameTv;
	private ImageView topRightImg;
	private TextView mGeneralTopTitleLeftImg;
	private TextView topRightBtn;

	public TitleNavView(View view, String title, TitleListener listener,
			boolean isShowRight) {
		this.listener = listener;
		mGeneralTopTitleLeftImg = (TextView) view.findViewById(R.id.back);
		topRightBtn = (TextView) view.findViewById(R.id.topRightBtn);

		titleNameTv = (TextView) view.findViewById(R.id.title);
		topRightImg = (ImageView) view.findViewById(R.id.topRightImg);
		if (isShowRight) {
			topRightImg.setVisibility(View.VISIBLE);
			topRightImg.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					TitleNavView.this.listener.onTopRightEvent();
				}
			});
		} else {
			topRightImg.setVisibility(View.GONE);
		}

		mGeneralTopTitleLeftImg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				TitleNavView.this.listener.onBack();
			}
		});

		if (title != null) {
			titleNameTv.setText(title);
		}
	}

	public void setTitle(String title) {
		titleNameTv.setText(title);
	}

	public void setShowLeft(boolean isShow) {
		if (mGeneralTopTitleLeftImg != null) {
			if (isShow) {
				mGeneralTopTitleLeftImg.setVisibility(View.VISIBLE);
			} else {
				mGeneralTopTitleLeftImg.setVisibility(View.GONE);
			}
		}
	}

	public void setRightBtnText(String text) {
		if (topRightBtn == null) {
			return;
		}
		topRightImg.setVisibility(View.GONE);
		topRightBtn.setVisibility(View.VISIBLE);
		topRightBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				listener.onTopRightEvent();
			}
		});
		topRightBtn.setBackgroundResource(0);
		topRightBtn.setText(text);
	}

	public void setRightResId(int resId) {
		topRightImg.setBackgroundResource(resId);
	}

	public interface TitleListener {
		void onBack();

		void onTopRightEvent();
	}
}
