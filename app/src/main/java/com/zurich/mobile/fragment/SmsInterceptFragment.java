package com.zurich.mobile.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zurich.mobile.R;
import com.zurich.mobile.adapter.itemfactory.SmsInterceptItemFactory;
import com.zurich.mobile.assemblyadapter.AssemblyRecyclerAdapter;
import com.zurich.mobile.db.Dao.SmsInterceptDao;
import com.zurich.mobile.model.SmsDataInfo;
import com.zurich.mobile.utils.GlobalUtils;
import com.zurich.mobile.widget.HintView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 短信拦截页面
 * Created by weixinfei on 16/5/6.
 */
public class SmsInterceptFragment extends AppBaseFragment {

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
        initAdapter();
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                if (smsDataInfos != null && smsDataInfos.size() > 0) {
                    smsDataInfos.clear();
                }
                smsDataInfos = dao.findAll();
                if (smsDataInfos != null && smsDataInfos.size() > 0){
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
                        mAdapter.notifyDataSetChanged();
                    }
                }else {
                    tvNullTip.setVisibility(View.VISIBLE);
                }
            }

        }.execute();

    }

    private void initAdapter() {
        tvNullTip.setVisibility(View.GONE);
        if (mAdapter == null) {
            mAdapter = new AssemblyRecyclerAdapter(smsDataInfos);
            mAdapter.addItemFactory(new SmsInterceptItemFactory());
            rvSmsList.setAdapter(mAdapter);
        }

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
                if (fromPosition == smsDataInfos.size()-1 && toPosition == smsDataInfos.size()){
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
        itemTouchHelper.attachToRecyclerView(rvSmsList);
    }

    /**
     * 从数据库中删除号码
     * @param number
     */
    private void deletSmsFromDB(final String number) {
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

    public void getIterceptSms() {

        final String SMS_URI_ALL = "content://sms/";
        final String SMS_URI_INBOX = "content://sms/inbox";
        final String SMS_URI_SEND = "content://sms/sent";
        final String SMS_URI_DRAFT = "content://sms/draft";
        final String SMS_URI_OUTBOX = "content://sms/outbox";
        final String SMS_URI_FAILED = "content://sms/failed";
        final String SMS_URI_QUEUED = "content://sms/queued";

        try {
            Uri uri = Uri.parse(SMS_URI_ALL);
            String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
            Cursor cur = getContext().getContentResolver().query(uri, projection, null, null, "date desc");      // 获取手机内部短信

            if (cur.moveToFirst()) {
                int index_Address = cur.getColumnIndex("address");
                int index_Person = cur.getColumnIndex("person");
                int index_Body = cur.getColumnIndex("body");
                int index_Date = cur.getColumnIndex("date");
                int index_Type = cur.getColumnIndex("type");

                smsDataInfos.clear();

                do {
                    String strAddress = cur.getString(index_Address);
                    String intPerson = String.valueOf(cur.getInt(index_Person));
                    String strbody = cur.getString(index_Body);
                    long longDate = cur.getLong(index_Date);
                    int intType = cur.getInt(index_Type);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Date d = new Date(longDate);
                    String strDate = dateFormat.format(d);

                    if (intType == 1) {
                        SmsDataInfo smsDataInfo = new SmsDataInfo();
                        smsDataInfo.date = strDate;
                        smsDataInfo.senderNum = intPerson;
                        smsDataInfo.smsInfo = strbody;
                        smsDataInfos.add(smsDataInfo);
                    } else if (intType == 2) {
//                        strType = "发送";
                    } else {
//                        strType = "null";
                    }
//                    smsBuilder.append("[ ");
//                    smsBuilder.append(strAddress + ", ");
//                    smsBuilder.append(intPerson + ", ");
//                    smsBuilder.append(strbody + ", ");
//                    smsBuilder.append(strDate + ", ");
//                    smsBuilder.append(strType);
//                    smsBuilder.append(" ]\n\n");
                } while (cur.moveToNext());

                if (!cur.isClosed()) {
                    cur.close();
                    cur = null;
                }
            }
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        }

    }
}
