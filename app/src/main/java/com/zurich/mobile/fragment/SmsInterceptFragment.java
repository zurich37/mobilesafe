package com.zurich.mobile.fragment;

import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.zurich.mobile.R;
import com.zurich.mobile.adapter.itemfactory.SmsInterceptItemFactory;
import com.zurich.mobile.adapter.assemblyadapter.AssemblyRecyclerAdapter;
import com.zurich.mobile.db.Dao.SmsInterceptDao;
import com.zurich.mobile.model.SmsDataInfo;
import com.zurich.mobile.utils.GlobalUtils;
import com.zurich.mobile.widget.HintView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 短信拦截页面
 * Created by weixinfei on 16/5/6.
 */
public class SmsInterceptFragment extends AppBaseFragment implements SmsInterceptItemFactory.InterceptSmsEvent {

    private RecyclerView rvSmsList;
    private List<SmsDataInfo> smsDataInfos;
    private AssemblyRecyclerAdapter mAdapter;
    private TextView tvNullTip;
    private SmsInterceptDao dao;
    private HintView hintView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        smsDataInfos = new ArrayList<SmsDataInfo>();
        dao = new SmsInterceptDao(getContext());
    }

    @Override
    public int getContentViewLayoutId() {
        return R.layout.fragment_sms_intercept;
    }

    @Override
    public void onInitViews(View view, Bundle savedInstanceState) {
        rvSmsList = (RecyclerView) findViewById(R.id.rv_sms_inter_list);
        tvNullTip = (TextView) findViewById(R.id.tv_null_tips);
        hintView = (HintView) findViewById(R.id.hint_sms_intercept);
        tvNullTip.setVisibility(View.GONE);
        rvSmsList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false));
        rvSmsList.setItemAnimator(new DefaultItemAnimator());
        hintView.loading().show();
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
        loadSmsData();
        showData();
    }

    private void loadSmsData() {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                if (smsDataInfos != null && smsDataInfos.size() > 0) {
                    smsDataInfos.clear();
                }
                smsDataInfos = dao.findAll();
                if (smsDataInfos != null && smsDataInfos.size() > 0) {
                    return true;
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (aBoolean) {
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
                    } else {
                        initAdapter();
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    tvNullTip.setVisibility(View.VISIBLE);
                }
            }

        }.execute();

    }

    private void initAdapter() {
        tvNullTip.setVisibility(View.GONE);
        mAdapter = new AssemblyRecyclerAdapter(smsDataInfos);
        mAdapter.addItemFactory(new SmsInterceptItemFactory(this));
        rvSmsList.setAdapter(mAdapter);

        initRvSwipe();
    }

    private void initRvSwipe() {
        ItemTouchHelper.Callback mCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT) {
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
                if (fromPosition == smsDataInfos.size() - 1 && toPosition == smsDataInfos.size()) {
                    return false;
                }
                Collections.swap(smsDataInfos, fromPosition, toPosition);
                mAdapter.notifyItemMoved(fromPosition, toPosition);
                //返回true表示执行拖动
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                deletSmsFromDB(smsDataInfos.get(position).senderNum);
                smsDataInfos.remove(position);
                mAdapter.notifyItemRemoved(position);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    //左右滑动时改变Item的透明度
                    final float alpha = 1 - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
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
        itemTouchHelper.attachToRecyclerView(rvSmsList);
    }

    /**
     * 从数据库中删除号码
     *
     * @param number
     */
    private void deletSmsFromDB(final String number) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                dao.delete(number);
                if (!dao.find(number)) {
                    return true;
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (aBoolean) {
                    GlobalUtils.showToast(getContext(), "删除成功");
                } else {
                    GlobalUtils.showToast(getContext(), "删除失败");
                }
            }
        }.execute();
    }

    @Override
    public void onInterceptSmsClick(SmsDataInfo smsDataInfo) {
        showSmsDataDiaglog(smsDataInfo);
    }

    private void showSmsDataDiaglog(SmsDataInfo smsDataInfo) {
        Dialog.Builder builder = null;
        builder = new SimpleDialog.Builder(R.style.SimpleDialogLight) {
            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                super.onPositiveActionClicked(fragment);
            }

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                super.onNegativeActionClicked(fragment);
            }
        };

        ((SimpleDialog.Builder) builder).message(smsDataInfo.senderNum+"\n\n"+smsDataInfo.smsInfo)
                .title("短信内容")
                .positiveAction("确认")
                .negativeAction("关闭");
        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getChildFragmentManager(), null);
    }
}
