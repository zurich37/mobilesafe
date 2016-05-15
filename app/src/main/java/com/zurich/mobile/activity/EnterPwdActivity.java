package com.zurich.mobile.activity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zurich.mobile.R;

/**
 * 程序锁输入密码
 * Created by weixinfei on 16/5/10.
 */
public class EnterPwdActivity extends FragmentActivity{
    private TextView tv_appname;
    private ImageView iv_appicon;
    private EditText et_password;
    private String packname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pwd);
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
    public void enter(View view){
        String password = et_password.getText().toString().trim();
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "对不起啦，密码不能为空", 0).show();
            return;
        }
        if("123".equals(password)){
            //密码输入正确。
            //如果密码输入正确 告诉看门狗（后台的一个服务） 你不要在保护这个应用程序了 。 这个哥们密码输入正确。
            Intent intent = new Intent();
            intent.setAction("com.itheima.mobilesafe.stopprotect");
            intent.putExtra("packname", packname);
            sendBroadcast(intent);//发送一个自定义的广播
            finish();
        }else{
            Toast.makeText(this, "密码错误", 0).show();
            return;
        }

    }
}
