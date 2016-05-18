package com.zurich.mobile.adapter.itemfactory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.zurich.mobile.R;
import com.zurich.mobile.adapter.assemblyadapter.AssemblyRecyclerItem;
import com.zurich.mobile.adapter.assemblyadapter.AssemblyRecyclerItemFactory;
import com.zurich.mobile.model.Asset;
import com.zurich.mobile.utils.BFImageCache;

/**
 * 通用的应用列表
 */
public class AppItemFactory extends AssemblyRecyclerItemFactory<AppItemFactory.AppItem> {

    private AppItemClickEvent mEvent;
    public AppItemFactory(AppItemClickEvent event){
        this.mEvent = event;
    }

    @Override
    public boolean isTarget(Object itemObject) {
        return itemObject instanceof Asset;
    }

    @Override
    public AppItem createAssemblyItem(ViewGroup parent) {
        return new AppItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_app_common, parent, false), this);
    }

    public class AppItem extends AssemblyRecyclerItem<Asset, AppItemFactory> {
        private NetworkImageView ivAppIcon; // 应用icon
        private TextView tvAppName; // 应用名称
        private TextView tvAppDescription; // 应用描述

        protected AppItem(View convertView, AppItemFactory itemFactory) {
            super(convertView, itemFactory);
        }

        @Override
        protected void onFindViews(View convertView) {
            ivAppIcon = (NetworkImageView) convertView.findViewById(R.id.iv_app_common_icon);
            tvAppName = (TextView) convertView.findViewById(R.id.tv_app_common_name);
            tvAppDescription = (TextView) convertView.findViewById(R.id.tv_app_common_description);
        }

        @Override
        protected void onConfigViews(final Context context) {
            getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mEvent != null){
                        mEvent.onAppClick(getData());
                    }
                }
            });
        }

        @Override
        protected void onSetData(int position, Asset asset) {
            RequestQueue requestQueue = Volley.newRequestQueue(getConvertView().getContext());
            //实例化ImageLoader
            ImageLoader loader = new ImageLoader(requestQueue, new BFImageCache());
            //设置默认图片
            ivAppIcon.setDefaultImageResId(R.drawable.ic_app_light);
            //设置错误图片
            ivAppIcon.setErrorImageResId(R.drawable.ic_launcher);
            //设置图片url和ImageLoader
            ivAppIcon.setImageUrl(asset.iconUrl, loader);

            tvAppName.setText(asset.appName);
            tvAppDescription.setText(asset.appDescription);
        }
    }

    public interface AppItemClickEvent{
        void onAppClick(Asset asset);
    }
}
