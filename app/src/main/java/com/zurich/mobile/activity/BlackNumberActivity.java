package com.zurich.mobile.activity;

import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.widget.EditText;
import com.zurich.mobile.R;
import com.zurich.mobile.adapter.itemfactory.BlackNumberItemFactory;
import com.zurich.mobile.assemblyadapter.AssemblyRecyclerAdapter;
import com.zurich.mobile.db.Dao.BlackNumberDao;
import com.zurich.mobile.model.BlackNumberInfo;
import com.zurich.mobile.utils.GlobalUtils;
import com.zurich.mobile.widget.HintView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * 黑名单管理
 * Created by weixinfei on 16/5/9.
 */
public class BlackNumberActivity extends BaseActivity implements BlackNumberItemFactory.BlackNumberEvent {
    @Bind(R.id.rv_black_number)
    RecyclerView rvBlackNumber;
    @Bind(R.id.hint_black_number_hint)
    HintView hintBlackNumberHint;
    @Bind(R.id.black_number_tool_bar)
    Toolbar mToolbar;
    @Bind(R.id.btn_black_add)
    FloatingActionButton btnBlackAdd;


    private List<BlackNumberInfo> infos;
    private BlackNumberDao dao;
    private AssemblyRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_number);
        ButterKnife.bind(this);

        infos = new ArrayList<BlackNumberInfo>();
        dao = new BlackNumberDao(getBaseContext());
        loadBlackNumber();
        initActionBar();
        initView();
    }

    private void initActionBar() {
        mToolbar.setTitle(getResources().getString(R.string.safe_soft_black_number));
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.toolbar_back_normal);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    //设置menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.men_action_settings:
                SettingActivity.launch(BlackNumberActivity.this);
                return true;
            case R.id.men_action_about_me:
                return true;
            case R.id.menu_action_share:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 读取数据库中黑名单号码
     */
    private void loadBlackNumber() {
        hintBlackNumberHint.loading().show();
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                if (infos != null) {
                    infos.clear();
                }
                infos = dao.findAll();
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (aBoolean) {
                    initAdapter();
                    hintBlackNumberHint.hidden();
                }
            }
        }.execute();
    }

    /**
     * 向数据库中添加黑名单
     *
     * @param info
     */
    private void addBlackNumberToDB(final BlackNumberInfo info) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                dao.add(info.number, info.mode);
                if (dao.find(info.number))
                    return true;
                return false;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (aBoolean) {
                    updateAdapter();
                }
            }
        }.execute();
    }

    /**
     * 从数据库中删除号码
     *
     * @param number
     */
    private void deletNumberFromDB(final String number) {
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
                    GlobalUtils.showToast(getBaseContext(), "删除成功");
                } else {
                    GlobalUtils.showToast(getBaseContext(), "删除失败");
                }
            }
        }.execute();
    }

    private void initView() {
        rvBlackNumber.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvBlackNumber.setItemAnimator(new DefaultItemAnimator());

        btnBlackAdd.setOnClickListener(new View.OnClickListener() {

            private SimpleDialog.Builder builder;

            @Override
            public void onClick(View v) {
                builder = new SimpleDialog.Builder(R.style.SimpleDialogLight) {
                    @Override
                    protected void onBuildDone(Dialog dialog) {
                        dialog.layoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    }

                    @Override
                    public void onPositiveActionClicked(DialogFragment fragment) {
                        EditText etAddNumber = (EditText) fragment.getDialog().findViewById(R.id.et_add_black_number);
                        CheckBox cbCallMode = (CheckBox) fragment.getDialog().findViewById(R.id.cb_add_black_call);
                        CheckBox cbSmsMode = (CheckBox) fragment.getDialog().findViewById(R.id.cb_add_black_sms);
                        if (!TextUtils.isEmpty(etAddNumber.getText())) {
                            BlackNumberInfo info = new BlackNumberInfo();
                            if (cbCallMode.isChecked() && cbSmsMode.isChecked()) {
                                info.mode = 3 + "";
                            } else if (cbCallMode.isChecked()) {
                                info.mode = 1 + "";
                            } else if (cbSmsMode.isChecked()) {
                                info.mode = 2 + "";
                            }
                            info.number = etAddNumber.getText().toString();
                            addBlackNumberToDB(info);
                            infos.add(info);
                        }
                        super.onPositiveActionClicked(fragment);
                    }

                    @Override
                    public void onNegativeActionClicked(DialogFragment fragment) {
                        super.onNegativeActionClicked(fragment);
                    }
                };

                builder.title("添加黑名单")
                        .positiveAction("确定")
                        .negativeAction("取消")
                        .contentView(R.layout.dialog_add_black);

                DialogFragment fragment = DialogFragment.newInstance(builder);
                fragment.show(getSupportFragmentManager(), null);
            }
        });
    }

    private void updateAdapter() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    private void initAdapter() {
        if (mAdapter == null) {
            mAdapter = new AssemblyRecyclerAdapter(infos);
            mAdapter.addItemFactory(new BlackNumberItemFactory(this));
        }
        rvBlackNumber.setAdapter(mAdapter);
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
                if (fromPosition == infos.size() - 1 && toPosition == infos.size()) {
                    return false;
                }
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
        itemTouchHelper.attachToRecyclerView(rvBlackNumber);
    }

    @Override
    public void onBlackNumberClick(BlackNumberInfo info) {

    }
}
