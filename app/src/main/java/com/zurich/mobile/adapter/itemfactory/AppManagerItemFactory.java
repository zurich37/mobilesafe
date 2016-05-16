package com.zurich.mobile.adapter.itemfactory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zurich.mobile.R;
import com.zurich.mobile.adapter.assemblyadapter.AssemblyRecyclerItem;
import com.zurich.mobile.adapter.assemblyadapter.AssemblyRecyclerItemFactory;
import com.zurich.mobile.model.AppInfo;

/**
 * 应用管理item
 * Created by weixinfei on 16/5/3.
 */
public class AppManagerItemFactory extends AssemblyRecyclerItemFactory<AppManagerItemFactory.AppManagerItem> {

    private final AppManagerClicEvent appManagerClicEvent;

    public AppManagerItemFactory(AppManagerClicEvent listener){
        appManagerClicEvent = listener;
    }
    @Override
    public boolean isTarget(Object itemObject) {
        return itemObject instanceof AppInfo;
    }

    @Override
    public AppManagerItem createAssemblyItem(ViewGroup parent) {
        return new AppManagerItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_app_manager, parent, false), this);
    }

    public class AppManagerItem extends AssemblyRecyclerItem<AppInfo, AppManagerItemFactory> {

        private TextView tvAppName;
        private ImageView ivAppIcon;
        private TextView tvAppLocation;

        protected AppManagerItem(View convertView, AppManagerItemFactory itemFactory) {
            super(convertView, itemFactory);
        }

        @Override
        protected void onFindViews(View convertView) {
            tvAppName = (TextView) convertView.findViewById(R.id.tv_app_name);
            ivAppIcon = (ImageView) convertView.findViewById(R.id.iv_app_icon);
            tvAppLocation = (TextView) convertView.findViewById(R.id.tv_location);
        }

        @Override
        protected void onConfigViews(Context context) {
            getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (appManagerClicEvent != null)
                        appManagerClicEvent.OnItemClick(getConvertView(), getData());
                }
            });
        }

        @Override
        protected void onSetData(int position, AppInfo appInfo) {
            tvAppName.setText(appInfo.getName());
            ivAppIcon.setImageDrawable(appInfo.getIcon());
            if (appInfo.isUserApp()){
                tvAppLocation.setText("外部存储");
            }else {
                tvAppLocation.setText("系统内存");
            }
        }
    }

    public interface AppManagerClicEvent{
        void OnItemClick(View view, AppInfo appInfo);
    }
}
