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
 * 应用管理title
 * Created by weixinfei on 16/5/3.
 */
public class AppManagerTitleFactory extends AssemblyRecyclerItemFactory<AppManagerTitleFactory.AppManagerTitleItem> implements AssemblyPinnedSectionAdapter.PinnedSectionItemFactory {
    @Override
    public boolean isTarget(Object itemObject) {
        return itemObject instanceof String;
    }

    @Override
    public AppManagerTitleItem createAssemblyItem(ViewGroup parent) {
        return new AppManagerTitleItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_app_manager_title, parent, false), this);
    }

    public class AppManagerTitleItem extends AssemblyRecyclerItem<String, AppManagerTitleFactory> {

        private TextView tvTitle;

        protected AppManagerTitleItem(View convertView, AppManagerTitleFactory itemFactory) {
            super(convertView, itemFactory);
        }

        @Override
        protected void onFindViews(View convertView) {
            tvTitle = (TextView) convertView.findViewById(R.id.tv_app_manger_title);
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
