package com.zurich.mobile.adapter.itemfactory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zurich.mobile.R;
import com.zurich.mobile.adapter.assemblyadapter.AssemblyRecyclerItem;
import com.zurich.mobile.adapter.assemblyadapter.AssemblyRecyclerItemFactory;
import com.zurich.mobile.model.BlackNumberInfo;

/**
 * 电话拦截item
 * Created by weixinfei on 16/5/10.
 */
public class CallInterceptItemFactory extends AssemblyRecyclerItemFactory<CallInterceptItemFactory.CallInterceptItem>{
    @Override
    public boolean isTarget(Object itemObject) {
        return itemObject instanceof BlackNumberInfo;
    }

    @Override
    public CallInterceptItem createAssemblyItem(ViewGroup parent) {
        return new CallInterceptItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_call_intercept, parent, false), this);
    }

    public class CallInterceptItem extends AssemblyRecyclerItem<BlackNumberInfo, CallInterceptItemFactory>{

        private TextView tvNumber;
        private TextView tvCount;

        protected CallInterceptItem(View convertView, CallInterceptItemFactory itemFactory) {
            super(convertView, itemFactory);
        }

        @Override
        protected void onFindViews(View convertView) {
            tvNumber = (TextView) convertView.findViewById(R.id.tv_call_intercept_num);
            tvCount = (TextView) convertView.findViewById(R.id.tv_intercept_count);
        }

        @Override
        protected void onConfigViews(Context context) {

        }

        @Override
        protected void onSetData(int position, BlackNumberInfo info) {
            tvNumber.setText(info.number);
            tvCount.setText(info.count + "");
        }
    }
}
