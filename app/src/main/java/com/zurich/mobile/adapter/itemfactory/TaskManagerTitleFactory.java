package com.zurich.mobile.adapter.itemfactory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zurich.mobile.R;
import com.zurich.mobile.adapter.AssemblyPinnedSectionAdapter;
import com.zurich.mobile.assemblyadapter.AssemblyItem;
import com.zurich.mobile.assemblyadapter.AssemblyItemFactory;

/**
 * 进程管理title
 * Created by weixinfei on 16/5/2.
 */
public class TaskManagerTitleFactory extends AssemblyItemFactory<TaskManagerTitleFactory.TaskmanagerTitleItem> implements AssemblyPinnedSectionAdapter.PinnedSectionItemFactory{
    @Override
    public boolean isTarget(Object itemObject) {
        return itemObject instanceof String;
    }

    @Override
    public TaskmanagerTitleItem createAssemblyItem(ViewGroup parent) {
        return new TaskmanagerTitleItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_task_manager_title, parent, false), this);
    }

    public class TaskmanagerTitleItem extends AssemblyItem<String, TaskManagerTitleFactory>{


        protected TaskmanagerTitleItem(View convertView, TaskManagerTitleFactory itemFactory) {
            super(convertView, itemFactory);
        }

        @Override
        protected void onFindViews(View convertView) {
        }

        @Override
        protected void onConfigViews(Context context) {
        }

        @Override
        protected void onSetData(int position, String str) {
            ((TextView)convertView).setText(str);
        }
    }
}
