package com.lbi.academy.model;

import com.google.gson.annotations.SerializedName;

public class Rain {

	@SerializedName("3h")
	private Double threeHour;

	public Double getThreeHour() {
		return threeHour;
	}

	public void setThreeHour(Double threeHour) {
		this.threeHour = threeHour;
	}
}
