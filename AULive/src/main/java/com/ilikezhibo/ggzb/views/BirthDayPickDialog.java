package com.ilikezhibo.ggzb.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jack.utils.DateUtils;
import com.jack.utils.TextUtils;
import com.jack.utils.Trace;
import com.ilikezhibo.ggzb.BaseDialog;
import com.ilikezhibo.ggzb.R;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @ClassName: BirthDayPickDialog
 * @Description: 注册弹框
 * @author jack.long
 * @date 2014-3-27 下午3:35:32
 * 
 */
@SuppressLint("NewApi")
public class BirthDayPickDialog extends BaseDialog implements
		OnDateChangedListener {

	private TextView mHtvConstellation;
	private TextView mHtvAge;
	private DatePicker mDpBirthday;

	private Calendar mCalendar;
	private Date mMinDate;
	private Date mMaxDate;
	private Date mSelectDate;
	private static final int MAX_AGE = 60;
	private static final int MIN_AGE = -4;

	public BirthDayPickDialog(Context context, String oldTime) {
		super(context);
		setDialogContentView(R.layout.include_dialog_birthday);
		
		initViews();
		initData(oldTime);
	}

	@Override
	public void setTitle(CharSequence text) {
		super.setTitle(text);
	}

	//只显示日，隐藏年月
	public void setShowDayOnly()
	{
		((ViewGroup) ((ViewGroup) mDpBirthday.getChildAt(0)).getChildAt(0)).getChildAt(0).setVisibility(View.GONE);
		((ViewGroup) ((ViewGroup) mDpBirthday.getChildAt(0)).getChildAt(0)).getChildAt(1).setVisibility(View.GONE);
	}
	public void setButton(CharSequence text,
			DialogInterface.OnClickListener listener) {
		super.setButton1(text, listener);
	}

	public void setButton(CharSequence text1,
			DialogInterface.OnClickListener listener1, CharSequence text2,
			DialogInterface.OnClickListener listener2) {
		super.setButton1(text1, listener1);
		super.setButton2(text2, listener2);
	}

	private void flushBirthday(Calendar calendar) {
		String constellation = TextUtils.getConstellation(
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
		mSelectDate = calendar.getTime();
		mHtvConstellation.setText(constellation);
		int age = TextUtils.getAge(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
		mHtvAge.setText(age + "");
	}

	private void initData(String oldTime) {
		if (oldTime == null) {
			oldTime = "";
		}

		Trace.d("initData oldTime:" + oldTime);
		// "19900101"
		mSelectDate = DateUtils.getDate2(oldTime);

		
		//最大最小时间
		Calendar mMinCalendar = Calendar.getInstance();
		Calendar mMaxCalendar = Calendar.getInstance();

		mMinCalendar.set(Calendar.YEAR, mMinCalendar.get(Calendar.YEAR)
				- MIN_AGE);
		mMinDate = mMinCalendar.getTime();
		mMaxCalendar.set(Calendar.YEAR, mMaxCalendar.get(Calendar.YEAR)
				- MAX_AGE);
		mMaxDate = mMaxCalendar.getTime();

		mDpBirthday.setMinDate(mMaxDate.getTime());
		mDpBirthday.setMaxDate(mMinDate.getTime());
		
		
		//设置当前日期
		mCalendar = Calendar.getInstance();
		mCalendar.setTime(mSelectDate);
		flushBirthday(mCalendar);
		mDpBirthday.init(mCalendar.get(Calendar.YEAR),
				mCalendar.get(Calendar.MONTH),
				mCalendar.get(Calendar.DAY_OF_MONTH), this);

	}

	private void initViews() {
		mHtvConstellation = (TextView) findViewById(R.id.reg_birthday_htv_constellation);
		mHtvAge = (TextView) findViewById(R.id.reg_birthday_htv_age);
		mDpBirthday = (DatePicker) findViewById(R.id.date_picker);
		// final Calendar c = Calendar.getInstance();
		// c.setTime(DateUtils.getDate("19900101"));
		// // 将日历初始化为当前系统时间，并设置其事件监听
		// mDpBirthday.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
		// c.get(Calendar.DAY_OF_MONTH),
		// this);
	}

	public void setHeadGone()
	{
		LinearLayout xingzhuan_years_ly = (LinearLayout) findViewById(R.id.xingzhuan_years_ly);
		xingzhuan_years_ly.setVisibility(View.GONE);
		
		View sperate_line =  findViewById(R.id.sperate_line);
		sperate_line.setVisibility(View.GONE);	
	}
	
	public String getBirthDay() {
		int month = mDpBirthday.getMonth() + 1;
		String m;
		if (month < 10)
			m = "0" + month;
		else
			m = "" + month;

		int day = mDpBirthday.getDayOfMonth();
		String d;
		if (day < 10)
			d = "0" + day;
		else
			d = "" + day;
		return mDpBirthday.getYear() + "-" + m + "-" + d;
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		mCalendar = Calendar.getInstance();
		mCalendar.set(year, monthOfYear, dayOfMonth);
		if (mCalendar.getTime().after(mMinDate)
				|| mCalendar.getTime().before(mMaxDate)) {
//			mCalendar.setTime(mSelectDate);
//			mDpBirthday.init(mCalendar.get(Calendar.YEAR),
//					mCalendar.get(Calendar.MONTH),
//					mCalendar.get(Calendar.DAY_OF_MONTH), this);
			
			flushBirthday(mCalendar);
		} else {
			flushBirthday(mCalendar);
		}
	}
}
