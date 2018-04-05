package com.ilikezhibo.ggzb.avsdk.userinfo.paytop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ilikezhibo.ggzb.BaseEntity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.RoomUserListAdapter;
import com.ilikezhibo.ggzb.avsdk.userinfo.toprank.TopRankEntity;
import com.ilikezhibo.ggzb.home.MainActivity;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.android.tpush.XGPushManager;

import java.util.ArrayList;

/**
 * Created by hasee on 2017/9/4.
 */

public class TotalSendAdapter extends BaseAdapter {

    private Context context;

    private ViewHolder holder;

    private ArrayList<TopRankEntity> entities;

    public TotalSendAdapter(Context context) {
        this.context = context;
    }
    public void setEntities(ArrayList<TopRankEntity> entities) {
        this.entities = entities;
    }


    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        if (entities != null && entities.size() > 0) {
            return entities.size();
        }

        return 0;
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return entities.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null || convertView.getTag() == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.cell_rec_new_user_list_item, null);
            holder.user_portrait = (ImageView) convertView.findViewById(R.id.user_portrait);
            holder.txt_username = (TextView) convertView.findViewById(R.id.txt_username);

            holder.imge_grade = (ImageView) convertView.findViewById(R.id.image_grade);
            holder.txt_info = (TextView) convertView.findViewById(R.id.txt_info);

            holder.txt_rank = (TextView) convertView.findViewById(R.id.rank_num);

            holder.txt_follow = (TextView) convertView.findViewById(R.id.txt_follow);
            // holder.tv_grade = (TextView) convertView.findViewById(R.id.grade_tv);

            holder.postion = position;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(position == 0){
            convertView =
                    LayoutInflater.from(context).inflate(R.layout.cell_rec_top_new_user_list_item, null);


            holder.user_portrait = (ImageView) convertView.findViewById(R.id.user_portrait);
            holder.txt_username = (TextView) convertView.findViewById(R.id.txt_username);

            holder.imge_grade = (ImageView) convertView.findViewById(R.id.image_grade);
            holder.txt_info = (TextView) convertView.findViewById(R.id.txt_info);


            holder.txt_follow = (TextView) convertView.findViewById(R.id.txt_follow);
            holder.postion = position;
        }
        final TopRankEntity entity = entities.get(position);

        //排名
        int no_num = position + 1;
        if(position != 0) holder.txt_rank.setText(no_num + "");
        DisplayImageOptions options =
                new DisplayImageOptions.Builder().showStubImage(R.drawable.face_male)
                        .showImageForEmptyUri(R.drawable.face_male)
                        .showImageOnFail(R.drawable.face_male)
                        .cacheInMemory()
                        .cacheOnDisc()
                        .build();
        //头像
        ImageLoader.getInstance().displayImage(entity.getFace(), holder.user_portrait, options);

        //名字
        holder.txt_username.setText(entity.getNickname());

        //等级标识

        String grade = entity.getGrade();

        RoomUserListAdapter.setTopIcon(grade,holder.imge_grade);

        //标签
        holder.txt_info.setText("贡献 "+entity.getConsume_diamond()+"钻石");


        //
        boolean has_follow = false;
        if (MainActivity.atten_uids.contains(entity.getUid())) {
            has_follow = true;
        }
        final boolean tem_b = has_follow;
        if (tem_b) {
            holder.txt_follow.setText("已关注");
            if(position!=0){
                holder.txt_follow.setTextColor(0xff999999);
                holder.txt_follow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.shape_circle_rectangle_gray));

            }

        } else {
            holder.txt_follow.setText("关注");
            if(position!=0){
                holder.txt_follow.setTextColor(0xffe482ec);
                holder.txt_follow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.shape_circle_rectangle_pink));
            }
        }
        holder.txt_follow.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if (tem_b) {
                    doDelAttend(entity.getUid());
                } else {
                    doAttend(entity.getUid());
                }
            }
        });
        return convertView;
    }


    class ViewHolder {
        // 分享
        ImageView user_portrait;  //头像
        // ImageView img_user_type;  //用户标识

        TextView txt_username;   //用户名

        public ImageView imge_grade; //用户标识
        TextView txt_info; //信息

        TextView txt_rank; //等级

        TextView txt_follow; //关注

        TextView tv_grade;

        int postion;
    }
    private void doAttend(final String uid) {

        RequestInformation request = new RequestInformation(UrlHelper.ROOM_ADD_ATTEN + "?u=" + uid,
                RequestInformation.REQUEST_METHOD_GET);

        request.setCallback(new JsonCallback<BaseEntity>() {

            @Override public void onCallback(BaseEntity callback) {

                if (callback == null) {
                    return;
                }
                if (callback.getStat() == 200) {
                    XGPushManager.setTag(context, uid);
                    if (MainActivity.atten_uids.contains(uid)) {
                    } else {
                        MainActivity.atten_uids.add(uid);
                    }
                    TotalSendAdapter.this.notifyDataSetChanged();
                } else {
                    Utils.showMessage(callback.getMsg());
                }
            }

            @Override public void onFailure(AppException e) {
                Utils.showMessage("获取网络数据失败");
            }
        }.setReturnType(BaseEntity.class));
        request.execute();
    }

    private void doDelAttend(final String uid) {

        RequestInformation request = new RequestInformation(UrlHelper.ROOM_DEL_ATTEN + "?u=" + uid,
                RequestInformation.REQUEST_METHOD_GET);

        request.setCallback(new JsonCallback<BaseEntity>() {

            @Override public void onCallback(BaseEntity callback) {

                if (callback == null) {
                    return;
                }
                if (callback.getStat() == 200) {
                    XGPushManager.deleteTag (context, uid);

                    if (MainActivity.atten_uids.contains(uid)) {
                        MainActivity.atten_uids.remove(uid);
                    } else {

                    }

                    TotalSendAdapter.this.notifyDataSetChanged();
                } else {
                    Utils.showMessage(callback.getMsg());
                }
            }

            @Override public void onFailure(AppException e) {
                Utils.showMessage("获取网络数据失败");
            }
        }.setReturnType(BaseEntity.class));
        request.execute();
    }
}
