package com.ilikezhibo.ggzb.home;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jack.utils.Trace;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.home.listener.NavigationListener;

/**
 * 导航栏管理器 ,广场,消息,发现，我
 * 
 * @author big
 * 
 */
public class NavigationManager implements OnClickListener {

	public static final int TYPE_BANGMAMATAO_ZHOUFANGZI = 1;// 找房子
	public static final int TYPE_JIESIHUO_MSG = 2;// 消息
	public static final int TYPE_BANGMAMATAO_LIANXIRAN = 3;// 联系人
	public static final int TYPE_BANGMAMATAO_GROUP = 4;// 品牌团
	public static final int TYPE_BANGMAMATAO_MYINFO = 5;// 我的
	public static final int TYPE_RANK = 6;
	public static final int TYPE_PAY = 7;

	private static int type = TYPE_BANGMAMATAO_ZHOUFANGZI;

	private NavigationListener listener = null;

	private TextView tv1;
	private TextView tv2;
	private TextView tv3;
	private TextView tv4;
	private TextView tv5;
	private TextView tv7;
	private TextView tv8;

	private LinearLayout special_sell_RL;
	private LinearLayout findRL;
	private LinearLayout miaoshaRL;
	private LinearLayout groupRL;
	private LinearLayout my_info_RL;
	private LinearLayout LL_rank;
	private LinearLayout LL_pay;

	private TextView newMsgTotal;

	/**
	 * 创建导航栏菜单
	 * 
	 * @param layout
	 * @param focuseIdx
	 *            导航栏的焦点是哪一个 取值NEW_SONG-MY_DOWNLOAD
	 */
	public NavigationManager(View view, int tempType,
			NavigationListener listener) {
		type = tempType;
		this.listener = listener;

		special_sell_RL = (LinearLayout) view
				.findViewById(R.id.maintab_layout_1);
		special_sell_RL.setOnClickListener(this);
		findRL = (LinearLayout) view.findViewById(R.id.maintab_layout_2);
		findRL.setOnClickListener(this);
		newMsgTotal = (TextView) view
				.findViewById(R.id.tabitem_prifile_iv_badge);
		newMsgTotal.setVisibility(View.GONE);
		miaoshaRL = (LinearLayout) view.findViewById(R.id.maintab_layout_3);
		miaoshaRL.setOnClickListener(this);
		groupRL = (LinearLayout) view.findViewById(R.id.maintab_layout_4);
		groupRL.setOnClickListener(this);
		my_info_RL = (LinearLayout) view.findViewById(R.id.maintab_layout_5);
		my_info_RL.setOnClickListener(this);
		LL_rank = (LinearLayout) view.findViewById(R.id.maintab_layout_7);
		LL_rank.setOnClickListener(this);
		LL_pay = (LinearLayout) view.findViewById(R.id.maintab_layout_8);
		LL_pay.setOnClickListener(this);

		special_sell_RL.setPressed(true);
		special_sell_RL.setSelected(true);
		tv1 = (TextView) view.findViewById(R.id.tab_item_nearby_tv_label);
		tv1.setPressed(true);
		tv1.setSelected(true);
		tv2 = (TextView) view.findViewById(R.id.tab_item_find_tv_label);
		tv3 = (TextView) view.findViewById(R.id.tab_item_miaosha_tv_label);
		tv4 = (TextView) view.findViewById(R.id.tab_item_group_tv_label);
		tv5 = (TextView) view.findViewById(R.id.tab_item_shaiwu_tv_label);
		tv7 = (TextView) view.findViewById(R.id.tab_item_rank_tv_label);
		tv8 = (TextView) view.findViewById(R.id.tab_item_pay_tv_label);
	}

	public static int getCurrType() {
		return type;
	}

	public void showNewMsgTotal(int total) {
		if (newMsgTotal == null) {
			return;
		}

		if (total != 0) {
			newMsgTotal.setVisibility(View.VISIBLE);
			newMsgTotal.setText(total + "");
		} else {
			newMsgTotal.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.maintab_layout_1:
			if (type == TYPE_BANGMAMATAO_ZHOUFANGZI) {
			}

			tv1.setPressed(true);
			tv1.setSelected(true);
			tv2.setPressed(false);
			tv2.setSelected(false);
			tv3.setPressed(false);
			tv3.setSelected(false);
			tv4.setPressed(false);
			tv4.setSelected(false);
			tv5.setPressed(false);
			tv5.setSelected(false);
			tv7.setPressed(false);
			tv7.setSelected(false);
			tv8.setPressed(false);
			tv8.setSelected(false);

			special_sell_RL.setPressed(true);
			special_sell_RL.setSelected(true);
			findRL.setPressed(false);
			findRL.setSelected(false);
			miaoshaRL.setPressed(false);
			miaoshaRL.setSelected(false);
			groupRL.setPressed(false);
			groupRL.setSelected(false);
			my_info_RL.setPressed(false);
			my_info_RL.setSelected(false);
			LL_rank.setPressed(false);
			LL_rank.setSelected(false);
			LL_pay.setPressed(false);
			LL_pay.setSelected(false);

			type = TYPE_BANGMAMATAO_ZHOUFANGZI;
			listener.onClickCurrBang(type);
			break;
		case R.id.maintab_layout_2:
			Trace.d("id:" + R.id.maintab_layout_2);
			if (type == TYPE_JIESIHUO_MSG) {
				return;
			}
			tv1.setPressed(false);
			tv1.setSelected(false);
			tv2.setPressed(true);
			tv2.setSelected(true);
			tv3.setPressed(false);
			tv3.setSelected(false);
			tv4.setPressed(false);
			tv4.setSelected(false);
			tv5.setPressed(false);
			tv5.setSelected(false);
			tv7.setPressed(false);
			tv7.setSelected(false);
			tv8.setPressed(false);
			tv8.setSelected(false);

			special_sell_RL.setPressed(false);
			special_sell_RL.setSelected(false);
			findRL.setPressed(true);
			findRL.setSelected(true);
			miaoshaRL.setPressed(false);
			miaoshaRL.setSelected(false);
			groupRL.setPressed(false);
			groupRL.setSelected(false);
			my_info_RL.setPressed(false);
			my_info_RL.setSelected(false);
			LL_rank.setPressed(false);
			LL_rank.setSelected(false);
			LL_pay.setPressed(false);
			LL_pay.setSelected(false);

			type = TYPE_JIESIHUO_MSG;
			listener.onClickCurrBang(type);
			break;

		case R.id.maintab_layout_3:
			if (type == TYPE_BANGMAMATAO_LIANXIRAN) {
				return;
			}
			tv1.setPressed(false);
			tv1.setSelected(false);
			tv2.setPressed(false);
			tv2.setSelected(false);
			tv3.setPressed(true);
			tv3.setSelected(true);
			tv4.setPressed(false);
			tv4.setSelected(false);
			tv5.setPressed(false);
			tv5.setSelected(false);
			tv7.setPressed(false);
			tv7.setSelected(false);
			tv8.setPressed(false);
			tv8.setSelected(false);

			special_sell_RL.setPressed(false);
			special_sell_RL.setSelected(false);
			findRL.setPressed(false);
			findRL.setSelected(false);
			miaoshaRL.setPressed(true);
			miaoshaRL.setSelected(true);
			groupRL.setPressed(false);
			groupRL.setSelected(false);
			my_info_RL.setPressed(false);
			my_info_RL.setSelected(false);
			LL_rank.setPressed(false);
			LL_rank.setSelected(false);
			LL_pay.setPressed(false);
			LL_pay.setSelected(false);

			type = TYPE_BANGMAMATAO_LIANXIRAN;
			listener.onClickCurrBang(type);
			break;

		case R.id.maintab_layout_4:
			if (type == TYPE_BANGMAMATAO_GROUP) {
				return;
			}
			tv1.setPressed(false);
			tv1.setSelected(false);
			tv2.setPressed(false);
			tv2.setSelected(false);
			tv3.setPressed(false);
			tv3.setSelected(false);
			tv4.setPressed(true);
			tv4.setSelected(true);
			tv5.setPressed(false);
			tv5.setSelected(false);
			tv7.setPressed(false);
			tv7.setSelected(false);
			tv8.setPressed(false);
			tv8.setSelected(false);

			special_sell_RL.setPressed(false);
			special_sell_RL.setSelected(false);
			findRL.setPressed(false);
			findRL.setSelected(false);
			miaoshaRL.setPressed(false);
			miaoshaRL.setSelected(false);
			groupRL.setPressed(true);
			groupRL.setSelected(true);
			my_info_RL.setPressed(false);
			my_info_RL.setSelected(false);
			LL_rank.setPressed(false);
			LL_rank.setSelected(false);
			LL_pay.setPressed(false);
			LL_pay.setSelected(false);

			type = TYPE_BANGMAMATAO_GROUP;
			listener.onClickCurrBang(type);
			break;

		case R.id.maintab_layout_5:
			if (type == TYPE_BANGMAMATAO_MYINFO) {
				return;
			}
			tv1.setPressed(false);
			tv1.setSelected(false);
			tv2.setPressed(false);
			tv2.setSelected(false);
			tv3.setPressed(false);
			tv3.setSelected(false);
			tv4.setPressed(false);
			tv4.setSelected(false);
			tv5.setPressed(true);
			tv5.setSelected(true);
			tv7.setPressed(false);
			tv7.setSelected(false);
			tv8.setPressed(false);
			tv8.setSelected(false);

			special_sell_RL.setPressed(false);
			special_sell_RL.setSelected(false);
			miaoshaRL.setPressed(false);
			miaoshaRL.setSelected(false);
			findRL.setPressed(false);
			findRL.setSelected(false);
			groupRL.setPressed(false);
			groupRL.setSelected(false);
			my_info_RL.setPressed(true);
			my_info_RL.setSelected(true);
			LL_rank.setPressed(false);
			LL_rank.setSelected(false);
			LL_pay.setPressed(false);
			LL_pay.setSelected(false);

			type = TYPE_BANGMAMATAO_MYINFO;
			listener.onClickCurrBang(type);
			break;
			case R.id.maintab_layout_7:
				if (type == TYPE_RANK) {
					return;
				}
				tv1.setPressed(false);
				tv1.setSelected(false);
				tv2.setPressed(false);
				tv2.setSelected(false);
				tv3.setPressed(false);
				tv3.setSelected(false);
				tv4.setPressed(false);
				tv4.setSelected(false);
				tv5.setPressed(false);
				tv5.setSelected(false);
				tv7.setPressed(true);
				tv7.setSelected(true);
				tv8.setPressed(false);
				tv8.setSelected(false);

				special_sell_RL.setPressed(false);
				special_sell_RL.setSelected(false);
				miaoshaRL.setPressed(false);
				miaoshaRL.setSelected(false);
				findRL.setPressed(false);
				findRL.setSelected(false);
				groupRL.setPressed(false);
				groupRL.setSelected(false);
				my_info_RL.setPressed(false);
				my_info_RL.setSelected(false);
				LL_rank.setPressed(true);
				LL_rank.setSelected(true);
				LL_pay.setPressed(false);
				LL_pay.setSelected(false);

				type = TYPE_RANK;
				listener.onClickCurrBang(type);
				break;
			case R.id.maintab_layout_8:
				if (type == TYPE_PAY) {
					return;
				}
				tv1.setPressed(false);
				tv1.setSelected(false);
				tv2.setPressed(false);
				tv2.setSelected(false);
				tv3.setPressed(false);
				tv3.setSelected(false);
				tv4.setPressed(false);
				tv4.setSelected(false);
				tv5.setPressed(false);
				tv5.setSelected(false);
				tv7.setPressed(false);
				tv7.setSelected(false);
				tv8.setPressed(true);
				tv8.setSelected(true);

				special_sell_RL.setPressed(false);
				special_sell_RL.setSelected(false);
				miaoshaRL.setPressed(false);
				miaoshaRL.setSelected(false);
				findRL.setPressed(false);
				findRL.setSelected(false);
				groupRL.setPressed(false);
				groupRL.setSelected(false);
				my_info_RL.setPressed(false);
				my_info_RL.setSelected(false);
				LL_rank.setPressed(false);
				LL_rank.setSelected(false);
				LL_pay.setPressed(true);
				LL_pay.setSelected(true);

				type = TYPE_PAY;
				listener.onClickCurrBang(type);
				break;
		}
	}
}
