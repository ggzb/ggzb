package com.ilikezhibo.ggzb.step;

import com.ilikezhibo.ggzb.BaseEntity;
import java.util.ArrayList;

public class ProjectTypeListEntity extends BaseEntity {

	private ArrayList<ProjectTypeEntity> list;

	private ArrayList<ProjectTypeEntity> geo_range;
	private ArrayList<ProjectTypeEntity> pro_category;

	public ArrayList<ProjectTypeEntity> getList() {
		return list;
	}

	public void setList(ArrayList<ProjectTypeEntity> list) {
		this.list = list;
	}

	public ArrayList<ProjectTypeEntity> getGeo_range() {
		return geo_range;
	}

	public void setGeo_range(ArrayList<ProjectTypeEntity> geo_range) {
		this.geo_range = geo_range;
	}

	public ArrayList<ProjectTypeEntity> getPro_category() {
		return pro_category;
	}

	public void setPro_category(ArrayList<ProjectTypeEntity> pro_category) {
		this.pro_category = pro_category;
	}

}
