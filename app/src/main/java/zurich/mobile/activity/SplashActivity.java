package zurich.mobile.activity;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import zurich.mobile.R;

/**
 * Created by weixi_000 .
 */
public class SplashActivity extends Activity{

    private TextView tv_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        String version = getAppVersion();
        tv_version = (TextView) findViewById(R.id.tv_wec);
        tv_version.setText("精简极致体验  v" + version);
    }

    /**
     * 得到应用的版本信息
     * @return version
     */
    public String getAppVersion() {

        PackageManager pm = getPackageManager();

        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
