package com.zurich.mobile.model;

import com.zurich.mobile.assemblyadapter.AssemblyGroup;

import java.util.ArrayList;
import java.util.List;


public class PackageClearGroup implements AssemblyGroup {
    public String title;
    public List<PackageClearChild> itemList;
    public long totalLength;

    public boolean scanning;
    public boolean cleaning;
    public int checkedCount;
    public long checkedTotalLength;

    public PackageClearGroup(String title) {
        this.title = title;
    }

    public void setItemList(List<PackageClearChild> newItemList) {
        if (newItemList != null && newItemList.size() > 0) {
            itemList = new ArrayList<PackageClearChild>(newItemList);
        } else {
            itemList = null;
        }

        refreshState();
    }

    @Override
    public int getChildCount() {
        // 扫描中不显示子项
        return !scanning && itemList != null ? itemList.size() : 0;
    }

    @Override
    public Object getChild(int childPosition) {
        return itemList != null ? itemList.get(childPosition) : null;
    }

    public void refreshState() {
        checkedCount = 0;
        checkedTotalLength = 0;
        totalLength = 0;

        if (itemList != null && itemList.size() > 0) {
            for (PackageClearChild clearItem : itemList) {
                if(!clearItem.isDeleted()){
                    if (clearItem.isChecked()) {
                        checkedCount++;
                        checkedTotalLength += clearItem.getFileLength();
                    }
                    totalLength += clearItem.getFileLength();
                }
            }
        }
    }

    public boolean isAllChecked() {
        return checkedCount != 0 && checkedCount == getChildCount();
    }

    public boolean isAllUnchecked() {
        return checkedCount == 0;
    }

    public void setAllChildCheckedState(boolean newCheckedState) {
        if (itemList != null && itemList.size() > 0) {
            for (PackageClearChild clearItem : itemList) {
                clearItem.setChecked(newCheckedState);
            }
            refreshState();
        }
    }
}
