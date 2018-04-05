package com.ilikezhibo.ggzb.step;

import com.ilikezhibo.ggzb.BaseEntity;
import java.io.Serializable;

/**
 * @ClassName: MyInfoEntity
 * @Description: 主页内容
 * @author big
 * @date 2014-8-1 下午4:05:19
 * 
 */
public class UpLoadFileEntity extends BaseEntity implements Serializable {
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
