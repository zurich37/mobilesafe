package com.zurich.mobile.fragment;

import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;

import com.zurich.mobile.R;
import com.zurich.mobile.adapter.itemfactory.CallInterceptItemFactory;
import com.zurich.mobile.adapter.assemblyadapter.AssemblyRecyclerAdapter;
import com.zurich.mobile.db.Dao.CallInterceptDao;
import com.zurich.mobile.model.BlackNumberInfo;
import com.zurich.mobile.utils.GlobalUtils;
import com.zurich.mobile.widget.HintView;

import java.util.Collections;
import java.util.List;

/**
 * 黑名单拦截页面
 * Created by weixinfei on 16/5/6.
 */
public class CallInterceptFragment extends AppBaseFragment {

    private RecyclerView rvCallList;
    private TextView tvNullTip;
    private CallInterceptDao dao;
    private List<BlackNumberInfo> infos;
    private AssemblyRecyclerAdapter mAdapter;
    private HintView hintView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dao = new CallInterceptDao(getContext());
    }

    @Override
    public int getContentViewLayoutId() {
        return R.layout.fragment_call_intercept;
    }

    @Override
    public void onInitViews(View view, Bundle savedInstanceState) {
        rvCallList = (RecyclerView) findViewById(R.id.rv_call_inter_list);
        tvNullTip = (TextView) findViewById(R.id.tv_call_null_tips);
        hintView = (HintView) findViewById(R.id.hint_call_intercept);

        rvCallList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvCallList.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public boolean isReadyData() {
        return mAdapter != null;
    }

    @Override
    public void onShowData() {
        hintView.hidden();
    }

    @Override
    public void onLoadData() {
        hintView.loading().show();
        loadCallInterData();
    }

    private void loadCallInterData() {
        initAdapter();
        loadBlackNumber();
        hintView.hidden();
    }

    /**
     * 读取数据库中电话拦截信息
     */
    private void loadBlackNumber() {
        new AsyncTask<Void, Void, Boolean>(){

            @Override
            protected Boolean doInBackground(Void... params) {
                if (infos != null){
                    infos.clear();
                }
                infos = dao.findAll();
                if (infos != null && infos.size() > 0)
                    return true;
                return false;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (aBoolean){
                    mAdapter.notifyDataSetChanged();
                }else {
                    tvNullTip.setVisibility(View.VISIBLE);
                    rvCallList.setVisibility(View.GONE);
                }
            }
        }.execute();
    }

    private void initAdapter() {
        tvNullTip.setVisibility(View.GONE);
        rvCallList.setVisibility(View.VISIBLE);
        if (mAdapter == null){
            mAdapter = new AssemblyRecyclerAdapter(infos);
            mAdapter.addItemFactory(new CallInterceptItemFactory());
        }
        rvCallList.setAdapter(mAdapter);
        initRvSwipe();
    }

    private void initRvSwipe() {
        ItemTouchHelper.Callback mCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN,ItemTouchHelper.RIGHT) {
            /**
             * @param recyclerView
             * @param viewHolder 拖动的ViewHolder
             * @param target 目标位置的ViewHolder
             * @return
             */
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();//得到拖动ViewHolder的position
                int toPosition = target.getAdapterPosition();//得到目标ViewHolder的position
                if (fromPosition == infos.size()-1 && toPosition == infos.size()){
                    return false;
                }
                Collections.swap(infos, fromPosition, toPosition);
                mAdapter.notifyItemMoved(fromPosition, toPosition);
                //返回true表示执行拖动
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                deletNumberFromDB(infos.get(position).number);
                infos.remove(position);
                mAdapter.notifyItemRemoved(position);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    //左右滑动时改变Item的透明度
                    final float alpha = 1 - Math.abs(dX) / (float)viewHolder.itemView.getWidth();
                    viewHolder.itemView.setAlpha(alpha);
                    viewHolder.itemView.setTranslationX(dX);
                }
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                super.onSelectedChanged(viewHolder, actionState);
                //当选中Item时候会调用该方法，重写此方法可以实现选中时候的一些动画逻辑
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                //当动画已经结束的时候调用该方法，重写此方法可以实现恢复Item的初始状态
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mCallback);
        itemTouchHelper.attachToRecyclerView(rvCallList);
    }

    /**
     * 从数据库中删除号码
     * @param number
     */
    private void deletNumberFromDB(final String number) {
        new AsyncTask<Void, Void, Boolean>(){

            @Override
            protected Boolean doInBackground(Void... params) {
                dao.delete(number);
                if (!dao.find(number)){
                    return true;
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (aBoolean){
                    GlobalUtils.showToast(getContext(), "删除成功");
                }else {
                    GlobalUtils.showToast(getContext(), "删除失败");
                }
            }
        }.execute();
    }
}
