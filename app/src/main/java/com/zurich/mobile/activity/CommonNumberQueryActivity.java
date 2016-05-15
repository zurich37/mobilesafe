package com.zurich.mobile.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zurich.mobile.R;
import com.zurich.mobile.db.Dao.CommonNumberDao;
import com.zurich.mobile.widget.HintView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 查询常用号码
 * Created by weixinfei on 16/5/11.
 */
public class CommonNumberQueryActivity extends BaseActivity {
    @Bind(R.id.iv_toolbar_back)
    ImageView ivToolbarBack;
    @Bind(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @Bind(R.id.elv)
    ExpandableListView elv;
    @Bind(R.id.hint_common_number_hint)
    HintView hintCommonNumberHint;
    @Bind(R.id.tool_bar)
    Toolbar mToolbar;
    private SQLiteDatabase db;
    public static final String path = "/data/data/com.zurich.mobile/files/commonnum.db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_number);
        ButterKnife.bind(this);

        initActionBar();
        db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        initView();
    }

    private void initActionBar() {
        mToolbar.setTitle(getResources().getString(R.string.query_common));
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
                SettingActivity.launch(CommonNumberQueryActivity.this);
                return true;
            case R.id.men_action_about_me:
                return true;
            case R.id.menu_action_share:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        hintCommonNumberHint.loading().show();
        elv.setAdapter(new CommonNumberAdapter());
        elv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                TextView tv = (TextView) v;
                String phone = tv.getText().toString().split("\n")[1];
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                startActivity(intent);
                return false;
            }
        });
        hintCommonNumberHint.hidden();
    }

    private class CommonNumberAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return CommonNumberDao.getGroupCount(db);
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            TextView tv;
            if (convertView != null) {
                tv = (TextView) convertView;
            } else {
                tv = new TextView(getApplicationContext());
            }
            tv.setText("       " + CommonNumberDao.getGroupName(db, groupPosition));
            tv.setTextSize(20);
            tv.setTextColor(Color.RED);
            return tv;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            TextView tv;
            if (convertView != null) {
                tv = (TextView) convertView;
            } else {
                tv = new TextView(getApplicationContext());
            }
            tv.setText(CommonNumberDao.getChildNameByPosition(db, groupPosition, childPosition));
            tv.setTextSize(16);
            tv.setTextColor(Color.BLACK);
            return tv;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return CommonNumberDao.getChildCountByPosition(db, groupPosition);
        }

        @Override
        public Object getGroup(int groupPosition) {
            return null;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

}
