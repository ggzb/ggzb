package com.ilikezhibo.ggzb.photos.photobrowser;

import android.view.View;
import android.widget.Button;
import java.util.ArrayList;

import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.TextView;

import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;

/**
 * @ClassName: PicBrowseActivity
 * @Description: 聊天图片浏览
 * @author big
 * @date 2014-6-11 下午3:31:36
 * 
 */
public class PicBrowseActivity extends BaseFragmentActivity implements
		OnPageChangeListener {
	public static final String INTENT_BROWSE_POS_KEY = "INTENT_BROWSE_POS_KEY";
	public static final String INTENT_BROWSE_LST_KEY = "INTENT_BROWSE_LST_KEY";

	private TextView posBrowseTv;
	private TextView countBrowseTv;
	private ScrollViewPager mSvpPager;

	@Override
	protected void setContentView() {
		setContentView(R.layout.pic_browse_layout);
	}

	@Override
	protected void initializeViews() {
		mSvpPager = (ScrollViewPager) findViewById(R.id.imagebrowser_svp_pager);
		mSvpPager.setOnPageChangeListener(this);

		posBrowseTv = (TextView) findViewById(R.id.posBrowseTv);
		countBrowseTv = (TextView) findViewById(R.id.countBrowseTv);
	}

	@Override
	protected void initializeData() {
		int pos = getIntent().getIntExtra(INTENT_BROWSE_POS_KEY, 0);
		String[] pic_urls = getIntent().getStringArrayExtra(
				INTENT_BROWSE_LST_KEY);
		ArrayList<String> entities = new ArrayList<String>();
		for (String url : pic_urls) {
			entities.add(url);
		}

		posBrowseTv.setText((pos + 1) + "");
		countBrowseTv.setText(entities.size() + "");
		PicBrowseAdapter adapter = new PicBrowseAdapter(this, entities);
		mSvpPager.setAdapter(adapter);
		mSvpPager.setCurrentItem(pos, false);


       Button back_btn = (Button) findViewById(R.id.back_btn);
       back_btn.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
             PicBrowseActivity.this.finish();
          }
       });
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		posBrowseTv.setText((arg0 + 1) + "");
	}

}
