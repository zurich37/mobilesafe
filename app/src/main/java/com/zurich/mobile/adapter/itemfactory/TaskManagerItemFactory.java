package com.zurich.mobile.adapter.itemfactory;


import android.content.Context;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.zurich.mobile.R;
import com.zurich.mobile.assemblyadapter.AssemblyRecyclerItem;
import com.zurich.mobile.assemblyadapter.AssemblyRecyclerItemFactory;
import com.zurich.mobile.model.TaskInfo;

/**
 * 进程Item
 * Created by weixinfei on 16/5/2.
 */
public class TaskManagerItemFactory extends AssemblyRecyclerItemFactory<TaskManagerItemFactory.TaskManagerItem> {
    @Override
    public boolean isTarget(Object itemObject) {
        return itemObject instanceof TaskInfo;
    }

    @Override
    public TaskManagerItem createAssemblyItem(ViewGroup parent) {
        return new TaskManagerItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_task_manager, parent, false), this);
    }

    public class TaskManagerItem extends AssemblyRecyclerItem<TaskInfo, TaskManagerItemFactory> {
        private TextView tvTaskName;
        private ImageView ivTaskIcon;
        private TextView tvMemSize;
        private CheckBox checkbox;

        protected TaskManagerItem(View convertView, TaskManagerItemFactory itemFactory) {
            super(convertView, itemFactory);
        }

        @Override
        protected void onFindViews(View convertView) {
            tvTaskName = (TextView) convertView.findViewById(R.id.tv_task_name);
            ivTaskIcon = (ImageView) convertView.findViewById(R.id.iv_task_icon);
            tvMemSize = (TextView) convertView.findViewById(R.id.tv_memsize);
            checkbox = (CheckBox) convertView.findViewById(R.id.checkbox_task_item);
        }

        @Override
        protected void onConfigViews(final Context context) {
            getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkbox.setChecked(!checkbox.isChecked());
                    getData().setChecked(checkbox.isChecked());
                }
            });
        }

        @Override
        protected void onSetData(int position, TaskInfo taskInfo) {
            if (taskInfo != null){
                ivTaskIcon.setImageDrawable(taskInfo.getIcon());
                tvTaskName.setText(taskInfo.getName());
                tvMemSize.setText(Formatter.formatFileSize(tvMemSize.getContext(), taskInfo.getMemsize()));
                checkbox.setChecked(taskInfo.isChecked());
            }
        }
    }
}
