package com.zurich.mobile.adapter.itemfactory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zurich.mobile.R;
import com.zurich.mobile.assemblyadapter.AssemblyRecyclerItem;
import com.zurich.mobile.assemblyadapter.AssemblyRecyclerItemFactory;
import com.zurich.mobile.model.SmsDataInfo;

/**
 * 拦截短信条目
 * Created by weixinfei on 16/5/8.
 */
public class SmsInterceptItemFactory extends AssemblyRecyclerItemFactory<SmsInterceptItemFactory.SmsInterceptItem> {
    @Override
    public boolean isTarget(Object itemObject) {
        return itemObject instanceof SmsDataInfo;
    }

    @Override
    public SmsInterceptItem createAssemblyItem(ViewGroup parent) {
        return new SmsInterceptItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_sms_intercept, parent, false), this);
    }

    public class SmsInterceptItem extends AssemblyRecyclerItem<SmsDataInfo, SmsInterceptItemFactory> {

        private TextView tvSmsName;
        private TextView tvSmsContent;

        protected SmsInterceptItem(View convertView, SmsInterceptItemFactory itemFactory) {
            super(convertView, itemFactory);
        }

        @Override
        protected void onFindViews(View convertView) {
            tvSmsName = (TextView) convertView.findViewById(R.id.tv_sms_name);
            tvSmsContent = (TextView) convertView.findViewById(R.id.tv_sms_content);
        }

        @Override
        protected void onConfigViews(Context context) {
        }

        @Override
        protected void onSetData(int position, SmsDataInfo smsDataInfo) {
            tvSmsContent.setText(smsDataInfo.smsInfo);
            tvSmsName.setText(smsDataInfo.senderNum);
        }
    }
}
