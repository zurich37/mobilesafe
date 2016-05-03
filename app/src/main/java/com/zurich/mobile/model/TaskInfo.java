package com.zurich.mobile.model;

import android.graphics.drawable.Drawable;

/**
 * 进程信息
 * Created by weixinfei on 16/5/2.
 */
public class TaskInfo {
    /**
     * 标识当前item是否被选中。
     */
    private boolean checked;
    public boolean isChecked() {
        return checked;
    }
    public void setChecked(boolean checked) {
        this.checked = checked;
    }
    private Drawable icon;
    private String name;
    /**
     * byte为单位
     */
    private long memsize;

    private boolean userTask;
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
    public long getMemsize() {
        return memsize;
    }
    public void setMemsize(long memsize) {
        this.memsize = memsize;
    }
    public boolean isUserTask() {
        return userTask;
    }
    public void setUserTask(boolean userTask) {
        this.userTask = userTask;
    }
    public String getPackname() {
        return packname;
    }
    public void setPackname(String packname) {
        this.packname = packname;
    }
    @Override
    public String toString() {
        return "TaskInfo [name=" + name + ", memsize=" + memsize
                + ", userTask=" + userTask + ", packname=" + packname + "]";
    }
}
