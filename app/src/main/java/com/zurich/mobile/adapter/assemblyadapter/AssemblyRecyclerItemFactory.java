package com.zurich.mobile.adapter.assemblyadapter;

import android.view.ViewGroup;

public abstract class AssemblyRecyclerItemFactory<ITEM extends AssemblyRecyclerItem>{
    protected int itemType;
    protected AssemblyRecyclerAdapter adapter;

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public AssemblyRecyclerAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(AssemblyRecyclerAdapter adapter) {
        this.adapter = adapter;
    }

    public abstract boolean isTarget(Object itemObject);

    public abstract ITEM createAssemblyItem(ViewGroup parent);
}
