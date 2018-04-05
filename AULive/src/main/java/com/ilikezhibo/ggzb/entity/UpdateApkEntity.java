package com.ilikezhibo.ggzb.entity;

import com.ilikezhibo.ggzb.BaseEntity;
import java.io.Serializable;

/**
 * @author big
 * @ClassName: UserDetailEntity
 * @Description: 用户详情
 * @date 2014-3-27 下午4:05:19
 */
public class UpdateApkEntity extends BaseEntity implements Serializable {

   public String updateUrl;
   public int versonCode;
   public String updateMessage;
   //0为可选 1为强制更新
   public int upType;
}
