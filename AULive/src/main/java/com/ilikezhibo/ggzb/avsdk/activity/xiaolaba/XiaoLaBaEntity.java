package com.ilikezhibo.ggzb.avsdk.activity.xiaolaba;

import com.ilikezhibo.ggzb.BaseEntity;
import java.io.Serializable;

/**
 * @author big
 * @ClassName: GiftEntity
 * @Description: 主页内容
 * @date 2016-3-1 下午4:05:19
 */
public class XiaoLaBaEntity extends BaseEntity implements Serializable {

   //system_content 小喇叭内容  sender 发送者昵称 target主播昵称
   public String system_content;
   public String sender;
   public String target;
}
