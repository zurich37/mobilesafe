package com.zurich.mobile.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.rey.material.widget.ListView;
import com.zurich.mobile.R;
import com.zurich.mobile.adapter.itemfactory.SelectContactItemFactory;
import com.zurich.mobile.assemblyadapter.AssemblyAdapter;
import com.zurich.mobile.model.ContactInfo;
import com.zurich.mobile.utils.GlobalUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 选择联系人
 * Created by weixinfei on 16/4/24.
 */
public class SelectContactActivity extends BaseActivity {

    @Bind(R.id.lv_select_contact)
    ListView lvSelectContact;
    @Bind(R.id.setup_tool_bar)
    Toolbar mToolbar;
    private AssemblyAdapter mAdapter;
    public static final String RESULT_CONTACT_NUMBER = "contact_number";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact);
        ButterKnife.bind(this);
        initActionBar();
        initData();

    }

    private void initData() {

        new AsyncTask<Void, Void, Boolean>() {

            private ArrayList<ContactInfo> datas;

            @Override
            protected Boolean doInBackground(Void... params) {
                datas = getContactInfos();
                if (datas != null)
                    return true;
                return false;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if (aBoolean && mAdapter == null) {
                    mAdapter = new AssemblyAdapter(datas);
                    mAdapter.addItemFactory(new SelectContactItemFactory(new SelectContactEventListener()));
                    lvSelectContact.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                } else {
                    GlobalUtils.showToast(getBaseContext(), "您还没有任何联系人");
                }
            }
        }.execute();
    }

    private void initActionBar() {
        mToolbar.setTitle(getResources().getString(R.string.select_contact));
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
                SettingActivity.launch(SelectContactActivity.this);
                return true;
            case R.id.men_action_about_me:
                return true;
            case R.id.menu_action_share:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 获取系统的所有的联系人信息.
     *
     * @return
     */
    public ArrayList<ContactInfo> getContactInfos() {
        ArrayList<ContactInfo> contactInfos = new ArrayList<ContactInfo>();
        ContentResolver resolver = getContentResolver();
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri datauri = Uri.parse("content://com.android.contacts/data");
        // 查询raw_contact表 取联系人id
        Cursor cursor = resolver.query(uri, new String[]{"contact_id"}, null, null, null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            if (id != null) {
                // 查询data表 把当前联系人的数据给取出来.
                ContactInfo contact = new ContactInfo();
                Cursor dataCursor = resolver.query(datauri, null, "raw_contact_id=?", new String[]{id}, null);
                while (dataCursor.moveToNext()) {
                    String data1 = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                    String mimetype = dataCursor.getString(dataCursor.getColumnIndex("mimetype"));
                    if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
                        contact.number = data1.replace("-", "").replace(" ", "");
                    } else if ("vnd.android.cursor.item/email_v2".equals(mimetype)) {
                        contact.mail = data1;
                    } else if ("vnd.android.cursor.item/name".equals(mimetype)) {
                        contact.name = data1;
                    }
                }
                dataCursor.close();
                contactInfos.add(contact);
            }
        }
        cursor.close();
        return contactInfos;
    }

    class SelectContactEventListener implements SelectContactItemFactory.EventListener {

        @Override
        public void OnContactItemClick(String number) {
            if (!TextUtils.isEmpty(number)) {
                Intent intent = new Intent();
                intent.putExtra(RESULT_CONTACT_NUMBER, number);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }
}
