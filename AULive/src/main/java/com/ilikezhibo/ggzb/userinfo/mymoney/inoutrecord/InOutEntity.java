package com.ilikezhibo.ggzb.userinfo.mymoney.inoutrecord;

import java.io.Serializable;

import com.ilikezhibo.ggzb.BaseEntity;

/**
 * @ClassName: NewEntity
 * @Description: 主页内容
 * @author big
 * @date 2014-4-1 下午4:05:19
 * 
 */
public class InOutEntity extends BaseEntity implements Serializable {

	// "id": "13",
	// "uid": "16143899",
	// "money": "5000",
	// "memo": "《是去看完善》预支付了<font color=red>5000</font>金额",
	// "type": "2",
	// "recv_uid": "12121216",
	// "project_id": "42",
	// "add_time": "1438237679"

	private String id;
	private String uid;
	private String money;
	private String memo;
	private String type;
	private String recv_uid;
	private String project_id;
	private String add_time;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRecv_uid() {
		return recv_uid;
	}

	public void setRecv_uid(String recv_uid) {
		this.recv_uid = recv_uid;
	}

	public String getProject_id() {
		return project_id;
	}

	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}

	public String getAdd_time() {
		return add_time;
	}

	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}

}
