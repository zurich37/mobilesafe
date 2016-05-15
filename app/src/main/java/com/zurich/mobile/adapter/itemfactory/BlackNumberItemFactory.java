package com.zurich.mobile.adapter.itemfactory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zurich.mobile.R;
import com.zurich.mobile.assemblyadapter.AssemblyRecyclerItem;
import com.zurich.mobile.assemblyadapter.AssemblyRecyclerItemFactory;
import com.zurich.mobile.model.BlackNumberInfo;

/**
 * 黑名单item
 * Created by weixinfei on 16/5/9.
 */
public class BlackNumberItemFactory extends AssemblyRecyclerItemFactory<BlackNumberItemFactory.BlackNumberItem> {

    private BlackNumberEvent mListener;
    public BlackNumberItemFactory(BlackNumberEvent listener){
        this.mListener = listener;
    }

    @Override
    public boolean isTarget(Object itemObject) {
        return itemObject instanceof BlackNumberInfo;
    }

    @Override
    public BlackNumberItem createAssemblyItem(ViewGroup parent) {
        return new BlackNumberItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_black_number, parent, false), this);
    }

    public class BlackNumberItem extends AssemblyRecyclerItem<BlackNumberInfo, BlackNumberItemFactory> {

        private TextView tvMode;
        private TextView tvNumber;

        protected BlackNumberItem(View convertView, BlackNumberItemFactory itemFactory) {
            super(convertView, itemFactory);
        }

        @Override
        protected void onFindViews(View convertView) {
            tvNumber = (TextView) convertView.findViewById(R.id.tv_black_num);
            tvMode = (TextView) convertView.findViewById(R.id.tv_black_mode);
        }

        @Override
        protected void onConfigViews(Context context) {
            getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null){
                        mListener.onBlackNumberClick(getData());
                    }
                }
            });
        }

        @Override
        protected void onSetData(int position, BlackNumberInfo blackNumberInfo) {
            String mode = blackNumberInfo.mode;
            if (mode != null){
                if (mode.equals("1"))
                    tvMode.setText("电话拦截");
                else if (mode.equals("2"))
                    tvMode.setText("短信拦截");
                else if (mode.equals("3"))
                    tvMode.setText("全部拦截");
            }
            tvNumber.setText(blackNumberInfo.number);
        }
    }

    public interface BlackNumberEvent{
        void onBlackNumberClick(BlackNumberInfo info);
    }
}
