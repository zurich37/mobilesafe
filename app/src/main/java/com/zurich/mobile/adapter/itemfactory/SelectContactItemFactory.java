package com.zurich.mobile.adapter.itemfactory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zurich.mobile.R;
import com.zurich.mobile.assemblyadapter.AssemblyItem;
import com.zurich.mobile.assemblyadapter.AssemblyItemFactory;
import com.zurich.mobile.model.ContactInfo;

/**
 * 选择联系人item
 * Created by weixinfei on 16/4/29.
 */
public class SelectContactItemFactory extends AssemblyItemFactory<SelectContactItemFactory.SelectContactItem> {

    private EventListener mListener;
    public SelectContactItemFactory(EventListener listener){
        this.mListener = listener;
    }
    @Override
    public boolean isTarget(Object itemObject) {
        return itemObject instanceof ContactInfo;
    }

    @Override
    public SelectContactItem createAssemblyItem(ViewGroup parent) {
        return new SelectContactItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_select_contact, parent, false), this);
    }

    public class SelectContactItem extends AssemblyItem<ContactInfo, SelectContactItemFactory> {

        private TextView tvName;
        private TextView tvNumber;

        protected SelectContactItem(View convertView, SelectContactItemFactory itemFactory) {
            super(convertView, itemFactory);
        }

        @Override
        protected void onFindViews(View convertView) {
            tvName = (TextView) convertView.findViewById(R.id.tv_contact_name);
            tvNumber = (TextView) convertView.findViewById(R.id.tv_contact_number);
        }

        @Override
        protected void onConfigViews(Context context) {
            getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null){
                        mListener.OnContactItemClick(getData().number);
                    }
                }
            });
        }

        @Override
        protected void onSetData(int position, ContactInfo contactInfo) {
            tvName.setText(contactInfo.name);
            tvNumber.setText(contactInfo.number);
        }
    }

    public interface EventListener{
        void OnContactItemClick(String number);
    }
}
