package com.ilikezhibo.ggzb.userinfo.buydiamond;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.ilikezhibo.ggzb.R;

import java.util.ArrayList;

/**
 * @author big
 * @ClassName: BuyDiamondAdapter
 * @Description: 主页适配
 * @date 2014-7-8 下午4:12:57
 */
public class BuyDiamondAdapter extends BaseAdapter {
   private Context context;

   private ViewHolder holder;

   private ArrayList<BuyDiamondEntity> entities;

   public BuyDiamondAdapter(Context context) {
      this.context = context;
   }

   public void setEntities(ArrayList<BuyDiamondEntity> entities) {
      this.entities = entities;
   }

   @Override public int getCount() {
      if (entities != null && entities.size() > 0) {
         return entities.size();
      }

      return 0;
   }

   @Override public Object getItem(int position) {
      return entities.get(position);
   }

   @Override public long getItemId(int position) {

      return 0;
   }

   @Override public View getView(final int position, View convertView, ViewGroup parent) {

      if (convertView == null || convertView.getTag() == null) {
         holder = new ViewHolder();
         convertView = LayoutInflater.from(context).inflate(R.layout.cell_charge_info, null);

         holder.txt_charge_desc = (TextView) convertView.findViewById(R.id.txt_charge_desc);
         holder.bt_pay = (TextView) convertView.findViewById(R.id.bt_pay);
         holder.txt_charge_value = (TextView) convertView.findViewById(R.id.txt_charge_value);

         convertView.setTag(holder);
      } else {
         holder = (ViewHolder) convertView.getTag();
      }

      final BuyDiamondEntity entity = entities.get(position);

      // String pic_url = entity.getPics();
      //
      // ImageLoader.getInstance().displayImage(pic_url, holder.main_image,
      // AULiveApplication.getGlobalImgOptions());

      //Html.fromHtml(
      holder.txt_charge_desc.setText(entity.getMemo());
      holder.bt_pay.setText(entity.getMoney() + "元");
      holder.txt_charge_value.setText(entity.getDiamond());

      return convertView;
   }

   class ViewHolder {

      TextView txt_charge_desc;
      TextView bt_pay;
      TextView txt_charge_value;
   }
}
