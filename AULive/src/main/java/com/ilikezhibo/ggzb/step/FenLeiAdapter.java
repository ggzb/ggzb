package com.ilikezhibo.ggzb.step;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jack.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.R;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @ClassName: GroupFragmentAdapter
 * @Description: 主页适配
 * @author big
 * @date 2014-7-8 下午4:12:57
 * 
 */
public class FenLeiAdapter extends BaseAdapter {
	private Context context;

	private ViewHolder holder;

	private ArrayList<AdvEntity> entities;

	public FenLeiAdapter(Context context) {
		this.context = context;
	}

	// 最多可以选3
	private int select_count = 0;

	private HashMap<String, ArrayList<AdvEntity>> fenleiHashMap = new HashMap<String, ArrayList<AdvEntity>>();

	public void setEntities(ArrayList<AdvEntity> entities_temp) {
		// 清空前面的得重复内容
		fenleiHashMap.clear();

		// 做分类处理,遍历一遍
		for (AdvEntity advEntity : entities_temp) {
			String key = advEntity.getTab();
			if (key == null)
				key = "";
			ArrayList<AdvEntity> list = fenleiHashMap.get(key);
			if (list == null) {
				list = new ArrayList<AdvEntity>();
				fenleiHashMap.put(key, list);
			}
			list.add(advEntity);
		}

		this.entities = new ArrayList<AdvEntity>();

		for (String key : fenleiHashMap.keySet()) {
			ArrayList<AdvEntity> tem = fenleiHashMap.get(key);
			// 设置标题
			tem.get(0).setHas_title(key);

			// 当最后一行不满三个时，用空白代替
			int size = tem.size();
			int add = size % 3;
			// Trace.d("size % 3:"+add);
			if (add == 1) {
				AdvEntity advEntity = new AdvEntity();
				advEntity.setEmpty(true);
				tem.add(advEntity);
				AdvEntity advEntity2 = new AdvEntity();
				advEntity2.setEmpty(true);
				tem.add(advEntity2);

			}
			if (add == 2) {
				AdvEntity advEntity = new AdvEntity();
				advEntity.setEmpty(true);
				tem.add(advEntity);

			}
			entities.addAll(tem);
		}
	}

	@Override
	public int getCount() {
		if (entities != null && entities.size() > 0) {
			int count = entities.size() / 3;
			if (entities.size() % 3 != 0) {
				count++;
			}
			return count;
		}

		return 0;
	}

	@Override
	public Object getItem(int position) {
		return entities.get(position);
	}

	@Override
	public long getItemId(int position) {

		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null || convertView.getTag() == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.layer_group_grid_fenlei_item, null);
			holder.title_layout = (LinearLayout) convertView
					.findViewById(R.id.title_layout);
			holder.title_tv = (TextView) convertView
					.findViewById(R.id.title_tv);

			holder.layout_one = (LinearLayout) convertView
					.findViewById(R.id.layout_one);
			holder.layout_two = (LinearLayout) convertView
					.findViewById(R.id.layout_two);
			holder.layout_three = (LinearLayout) convertView
					.findViewById(R.id.layout_three);

			holder.iv_deal_pic = (ImageView) convertView
					.findViewById(R.id.iv_deal_pic);
			holder.tv_title_one = (TextView) convertView
					.findViewById(R.id.tv_title_one);

			holder.iv_deal_pic_two = (ImageView) convertView
					.findViewById(R.id.iv_deal_pic_two);
			holder.tv_title_two = (TextView) convertView
					.findViewById(R.id.tv_title_two);

			holder.iv_deal_pic_three = (ImageView) convertView
					.findViewById(R.id.iv_deal_pic_three);
			holder.tv_title_three = (TextView) convertView
					.findViewById(R.id.tv_title_three);

			holder.iv_check1 = (ImageView) convertView
					.findViewById(R.id.iv_check1);
			holder.iv_check2 = (ImageView) convertView
					.findViewById(R.id.iv_check2);
			holder.iv_check3 = (ImageView) convertView
					.findViewById(R.id.iv_check3);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// 一行显示两个entity
		final int index = position * 3;
		final int netxt = index + 1;
		final int three = netxt + 1;
		final AdvEntity entity = entities.get(index);

		// 还是要使用position处理，不能使用index
		if (entities.size() > index) {
			// 处理第一个
			if (!entity.getHas_title().equals("")) {
				holder.title_layout.setVisibility(View.VISIBLE);
				holder.title_tv.setText(entity.getHas_title());
			}

			if (entity.isChecked()) {
				holder.iv_check1.setVisibility(View.VISIBLE);
			} else {
				holder.iv_check1.setVisibility(View.GONE);
			}

			ImageLoader.getInstance()
					.displayImage(entity.getImg(), holder.iv_deal_pic,
							AULiveApplication.getGlobalImgOptions());
			holder.tv_title_one.setText(entity.getName());
			holder.layout_one.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					boolean checked = entity.isChecked();
					if (select_count >= 0 && select_count < 3 || checked) {
						if (checked) {
							select_count--;
						} else {
							select_count++;
						}

						entity.setChecked(!entity.isChecked());
						FenLeiAdapter.this.notifyDataSetChanged();
					} else {
						Utils.showMessage("最多可以选3个");
					}
				}
			});

			// 处理第二个
			if (entities.size() > netxt) {
				// 即还有下一个
				final AdvEntity second_entity = entities.get(netxt);

				if (second_entity.isChecked()) {
					holder.iv_check2.setVisibility(View.VISIBLE);
				} else {
					holder.iv_check2.setVisibility(View.GONE);
				}

				if (second_entity.isEmpty() == true) {
					holder.layout_two.setVisibility(View.INVISIBLE);

				} else {

					ImageLoader.getInstance().displayImage(
							second_entity.getImg(), holder.iv_deal_pic_two,
							AULiveApplication.getGlobalImgOptions());

					holder.tv_title_two.setText(second_entity.getName());
					holder.layout_two.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View view) {
							if (second_entity == null)
								return;

							boolean checked = second_entity.isChecked();
							if (select_count >= 0 && select_count < 3
									|| checked) {
								if (checked) {
									select_count--;
								} else {
									select_count++;
								}

								second_entity.setChecked(!second_entity
										.isChecked());
								FenLeiAdapter.this.notifyDataSetChanged();
							} else {
								Utils.showMessage("最多可以选3个");
							}
						}
					});
				}
			} else {
				holder.layout_two.setVisibility(View.INVISIBLE);
			}

			// 处理第三个
			if (entities.size() > three) {
				// 即还有下一个
				final AdvEntity three_entity = entities.get(three);
				if (three_entity.isEmpty() == true) {
					holder.layout_three.setVisibility(View.INVISIBLE);
				} else {

					ImageLoader.getInstance().displayImage(
							three_entity.getImg(), holder.iv_deal_pic_three,
							AULiveApplication.getGlobalImgOptions());

					if (three_entity.isChecked()) {
						holder.iv_check3.setVisibility(View.VISIBLE);
					} else {
						holder.iv_check3.setVisibility(View.GONE);
					}
					holder.tv_title_three.setText(three_entity.getName());
					holder.layout_three
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View view) {
									if (three_entity == null)
										return;

									boolean checked = three_entity.isChecked();
									if (select_count >= 0 && select_count < 3
											|| checked) {
										if (checked) {
											select_count--;
										} else {
											select_count++;
										}
										three_entity.setChecked(!three_entity
												.isChecked());

										FenLeiAdapter.this
												.notifyDataSetChanged();
									} else {
										Utils.showMessage("最多可以选3个");
									}
								}
							});
				}
			} else {
				holder.layout_three.setVisibility(View.INVISIBLE);
			}

		} else {
			// convertView.setVisibility(View.INVISIBLE);
		}

		return convertView;

	}

	class ViewHolder {
		LinearLayout title_layout;
		TextView title_tv;

		LinearLayout layout_one;
		ImageView iv_deal_pic;
		TextView tv_title_one;

		LinearLayout layout_two;
		ImageView iv_deal_pic_two;
		TextView tv_title_two;

		LinearLayout layout_three;
		ImageView iv_deal_pic_three;
		TextView tv_title_three;

		ImageView iv_check1;
		ImageView iv_check2;
		ImageView iv_check3;
	}
}
