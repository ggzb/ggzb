package com.ilikezhibo.ggzb;

import com.google.myjson.stream.JsonReader;
import com.google.myjson.stream.JsonWriter;
import com.ilikezhibo.ggzb.listener.IJSON;
import java.io.IOException;

/**
 * @ClassName: BaseEntity
 * @Description: 解析json的基类
 * @author jack.long
 * @date 2014-3-27 下午3:39:14
 * 
 */
public class BaseEntity implements IJSON {
	private int stat;
	private String msg;

	@Override
	public void writeToJson(JsonWriter writer) {

	}

	@Override
	public boolean readFromJson(JsonReader reader, String tag)
			throws IOException {
		if (tag.equalsIgnoreCase("stat")) {
			stat = reader.nextInt();
			return true;
		} else if (tag.equalsIgnoreCase("msg")) {
			msg = reader.nextString();
			return true;
		}
		return false;
	}

	public int getStat() {
		return stat;
	}

	public void setStat(int stat) {
		this.stat = stat;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
