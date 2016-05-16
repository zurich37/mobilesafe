package com.zurich.mobile.adapter.itemfactory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zurich.mobile.R;
import com.zurich.mobile.adapter.AssemblyPinnedSectionAdapter;
import com.zurich.mobile.adapter.assemblyadapter.AssemblyRecyclerItem;
import com.zurich.mobile.adapter.assemblyadapter.AssemblyRecyclerItemFactory;

/**
 * 进程管理title
 * Created by weixinfei on 16/5/2.
 */
public class TaskManagerTitleFactory extends AssemblyRecyclerItemFactory<TaskManagerTitleFactory.TaskmanagerTitleItem> implements AssemblyPinnedSectionAdapter.PinnedSectionItemFactory{
    @Override
    public boolean isTarget(Object itemObject) {
        return itemObject instanceof String;
    }

    @Override
    public TaskmanagerTitleItem createAssemblyItem(ViewGroup parent) {
        return new TaskmanagerTitleItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_task_manager_title, parent, false), this);
    }

    public class TaskmanagerTitleItem extends AssemblyRecyclerItem<String, TaskManagerTitleFactory> {


        private TextView tvTitle;

        protected TaskmanagerTitleItem(View convertView, TaskManagerTitleFactory itemFactory) {
            super(convertView, itemFactory);
        }

        @Override
        protected void onFindViews(View convertView) {
            tvTitle = (TextView) convertView.findViewById(R.id.tvTask_list_title);
        }

        @Override
        protected void onConfigViews(Context context) {
        }

        @Override
        protected void onSetData(int position, String str) {
            tvTitle.setText(str);
        }
    }
}
