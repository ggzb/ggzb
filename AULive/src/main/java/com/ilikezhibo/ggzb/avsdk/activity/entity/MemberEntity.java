package com.ilikezhibo.ggzb.avsdk.activity.entity;

import com.ilikezhibo.ggzb.BaseEntity;
import java.io.Serializable;

/**
 * @ClassName: GiftEntity
 * @Description: 主页内容
 * @author big
 * @date 2016-3-1 下午4:05:19
 * 
 */
public class MemberEntity extends BaseEntity implements Serializable {
   //{
   //   "stat": 200,
   //    "msg": "",
   //    "list": [
   //   {
   //      "headimagepath": "http://img.hrbhzkj.com/face/14/16225038.jpg",
   //       "username": "Giggle",
   //       "userid": "16225038"
   //   }
   //   ]
   //}

	public String face;
   public String nickname;
   public String uid;
   public String grade;
   public int offical;

}
