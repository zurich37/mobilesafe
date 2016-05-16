package com.zurich.mobile.activity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zurich.mobile.R;
import com.zurich.mobile.utils.GlobalUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 程序锁输入密码
 * Created by weixinfei on 16/5/10.
 */
public class EnterPwdActivity extends FragmentActivity {
    @Bind(R.id.enter_pass_tool_bar)
    Toolbar mToolbar;
    private TextView tv_appname;
    private ImageView iv_appicon;
    private EditText et_password;
    private String packname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pwd);
        ButterKnife.bind(this);

        initToolbar();
        et_password = (EditText) findViewById(R.id.et_password);
        tv_appname = (TextView) findViewById(R.id.tv_appname);
        iv_appicon = (ImageView) findViewById(R.id.iv_appicon);
        Intent intent = getIntent();
        packname = intent.getStringExtra("packname");
        PackageManager pm = getPackageManager();

        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo(packname, 0);
            iv_appicon.setImageDrawable(applicationInfo.loadIcon(pm));
            tv_appname.setText(applicationInfo.loadLabel(pm));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initToolbar() {
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        intent.addCategory("android.intent.category.DEFAULT");
        startActivity(intent);
        //杀死packname对应的进程
    }

    public void enter(View view) {
        String password = et_password.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            GlobalUtils.showToast(getBaseContext(), "密码不能为空！");
            return;
        }

        if ("123".equals(password)) {
            //密码输入正确。
            //如果密码输入正确 告诉看门狗（后台的一个服务） 你不要在保护这个应用程序了 。 这个哥们密码输入正确。
            Intent intent = new Intent();
            intent.setAction("com.zurich.mobile.stopprotect");
            intent.putExtra("packname", packname);
            sendBroadcast(intent);//发送一个自定义的广播
            finish();
        } else {
            GlobalUtils.showToast(getBaseContext(), "密码错误");
            return;
        }

    }
}
