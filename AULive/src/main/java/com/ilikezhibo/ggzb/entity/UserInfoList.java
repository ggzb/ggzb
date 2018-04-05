package com.ilikezhibo.ggzb.entity;

import com.ilikezhibo.ggzb.BaseEntity;
import java.util.ArrayList;

/**
 * @author big
 * @ClassName: UserDetailEntity
 * @Description: 用户详情
 * @date 2014-3-27 下午4:05:19
 */
public class UserInfoList extends BaseEntity {

   public ArrayList<LoginUserEntity> getUserinfo() {
      return userinfo;
   }

   public void setUserinfo(ArrayList<LoginUserEntity> userinfo) {
      this.userinfo = userinfo;
   }

   private ArrayList<LoginUserEntity> userinfo;

}
