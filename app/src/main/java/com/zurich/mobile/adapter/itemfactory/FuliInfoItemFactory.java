package com.zurich.mobile.adapter.itemfactory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zurich.mobile.R;
import com.zurich.mobile.adapter.assemblyadapter.AssemblyRecyclerItem;
import com.zurich.mobile.adapter.assemblyadapter.AssemblyRecyclerItemFactory;
import com.zurich.mobile.model.GankInfo;

/**
 * 福利factory
 * Created by weixinfei on 16/5/30.
 */
public class FuliInfoItemFactory extends AssemblyRecyclerItemFactory<FuliInfoItemFactory.FuliInfoItem> {
    private FuliEventListener mListener;
    public FuliInfoItemFactory(FuliEventListener listener) {
        mListener = listener;
    }

    @Override
    public boolean isTarget(Object itemObject) {
        return itemObject instanceof GankInfo.ResultsBean;
    }

    @Override
    public FuliInfoItem createAssemblyItem(ViewGroup parent) {
        return new FuliInfoItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_fuli, parent, false), this);
    }

    public class FuliInfoItem extends AssemblyRecyclerItem<GankInfo.ResultsBean, FuliInfoItemFactory> {

        private ImageView ivMeizhi;

        protected FuliInfoItem(View convertView, FuliInfoItemFactory itemFactory) {
            super(convertView, itemFactory);
        }

        @Override
        protected void onFindViews(View convertView) {
            ivMeizhi = (ImageView) convertView.findViewById(R.id.iv_fuli_item);
        }

        @Override
        protected void onConfigViews(Context context) {
            getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null){
                        mListener.onMeizhiClick(getData());
                    }
                }
            });
        }

        @Override
        protected void onSetData(int position, GankInfo.ResultsBean resultsBean) {
            Glide.with(ivMeizhi.getContext())
                    .load(resultsBean.getUrl())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(ivMeizhi);
        }
    }

    public interface FuliEventListener {
        void onMeizhiClick(GankInfo.ResultsBean gankInfo);
    }
}
