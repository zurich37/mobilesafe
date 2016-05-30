package com.zurich.mobile.adapter.itemfactory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zurich.mobile.R;
import com.zurich.mobile.adapter.assemblyadapter.AbstractLoadMoreRecyclerItemFactory;
import com.zurich.mobile.adapter.assemblyadapter.OnRecyclerLoadMoreListener;

/**
 * RecyclerVeiw加载更多item
 * Created by weixinfei on 16/5/9.
 */
public class LoadMoreRecyclerListItemFactory extends AbstractLoadMoreRecyclerItemFactory {

    public LoadMoreRecyclerListItemFactory(OnRecyclerLoadMoreListener eventListener) {
        super(eventListener);
    }

    @Override
    public AbstractLoadMoreRecyclerItem createAssemblyItem(ViewGroup parent) {
        return new LoadMoreListItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_footer_load_more, parent, false), this);
    }

    public class LoadMoreListItem extends AbstractLoadMoreRecyclerItemFactory.AbstractLoadMoreRecyclerItem {
        private View loadingLayout;
        private View errorRetryLayout;
        private View endLayout;

        protected LoadMoreListItem(View convertView, AbstractLoadMoreRecyclerItemFactory baseFactory) {
            super(convertView, baseFactory);
        }

        @Override
        protected void onFindViews(View convertView) {
            this.loadingLayout = convertView.findViewById(R.id.loading_layout);
            this.errorRetryLayout = convertView.findViewById(R.id.footer_retry_view);
            this.endLayout = convertView.findViewById(R.id.footer_retry_end);
        }

        @Override
        public View getErrorRetryView() {
            return errorRetryLayout;
        }

        @Override
        public void showLoading() {
            loadingLayout.setVisibility(View.VISIBLE);
            errorRetryLayout.setVisibility(View.GONE);
            endLayout.setVisibility(View.GONE);
        }

        public void showErrorRetry() {
            loadingLayout.setVisibility(View.GONE);
            errorRetryLayout.setVisibility(View.VISIBLE);
            endLayout.setVisibility(View.GONE);
        }

        @Override
        public void showEnd() {
            loadingLayout.setVisibility(View.GONE);
            errorRetryLayout.setVisibility(View.GONE);
            endLayout.setVisibility(View.VISIBLE);
        }
    }
}
