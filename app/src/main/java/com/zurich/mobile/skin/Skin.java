package com.zurich.mobile.skin;

import android.graphics.Color;

/**
 * 皮肤枚举对象
 */
public enum Skin {
	HUANG_NIE("\ue609\ue60e", Color.parseColor("#FFDA44")),
	
	ZHONG_HONG("\ue619\ue608", Color.parseColor("#DB4D6D")),
	
	YIN_ZHU("\ue618\ue61a", Color.parseColor("#C73E3A")),
	
	PU_TAO("\ue610\ue611", Color.parseColor("#7B0051")),
	
	QIAN_CONG("\ue612\ue605", Color.parseColor("#00D1C1")),
	
	LU_CAO("\ue60c\ue602", Color.parseColor("#0382da")),
	
	CHANG_PAN("\ue603\ue60f", Color.parseColor("#1b813E")),
	
	QING_BI("\ue613\ue601", Color.parseColor("#007A87")),
	
	CHONG_AO("\ue604\ue600", Color.parseColor("#20604F")),
	
	HONG_BI("\ue608\ue601", Color.parseColor("#7B90D2")),
	
	TENG_SHU("\ue616\ue615", Color.parseColor("#6E75A4")),
	
	LIU_LI("\ue60b\ue60a", Color.parseColor("#005caf")),
	
	GAN_QING("\ue606\ue613", Color.parseColor("#113285")),
	
	HEI_XIANG("\ue607\ue617", Color.parseColor("#101518")),
	
	USER_CUSTOM("\ue901\ue902\ue900", -1);
	
	private String skinName;
	private int primaryColor;
	
	Skin(String skinName, int primaryColor) {
		this.skinName = skinName;
		this.primaryColor = primaryColor;
	}
	
	public String getSkinName() {
		return skinName;
	}
	
	public int getPrimaryColor() {
		return primaryColor;
	}

	void setPrimaryColor(int primaryColor) {
		this.primaryColor = primaryColor;
	}
}