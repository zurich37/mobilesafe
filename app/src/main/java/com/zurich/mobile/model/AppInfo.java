package com.zurich.mobile.model;

import android.graphics.drawable.Drawable;

/**
 * 应用信息
 * Created by weixinfei on 16/5/3.
 */
public class AppInfo {
    private Drawable icon;
    private String name;
    private boolean inRom;
    private boolean userApp;


    private String packname;
    public Drawable getIcon() {
        return icon;
    }
    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean isUserApp() {
        return userApp;
    }
    public void setUserApp(boolean userApp) {
        this.userApp = userApp;
    }

    public boolean isInRom() {
        return inRom;
    }
    public void setInRom(boolean inRom) {
        this.inRom = inRom;
    }
    public String getPackname() {
        return packname;
    }
    public void setPackname(String packname) {
        this.packname = packname;
    }
    @Override
    public String toString() {
        return "AppInfo [name=" + name + ", inRom=" + inRom + ", userApp="
                + userApp + ", packname=" + packname + "]";
    }

}
