package com.ilikezhibo.ggzb.step;

import com.ilikezhibo.ggzb.BaseEntity;

public class ProjectTypeEntity extends BaseEntity {

	private String id;
	private String name;
	private String step;

	private boolean is_pressed;
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public boolean isIs_pressed() {
		return is_pressed;
	}

	public void setIs_pressed(boolean is_pressed) {
		this.is_pressed = is_pressed;
	}

}
