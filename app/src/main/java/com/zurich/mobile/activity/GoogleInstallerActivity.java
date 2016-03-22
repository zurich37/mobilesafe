package com.zurich.mobile.activity;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.zurich.mobile.R;
import com.zurich.mobile.widget.GifMovieView;

import java.util.List;

public class GoogleInstallerActivity extends FragmentActivity{
	private Activity mActivity;
	private TextView statusTv;
	private TextView operationTv;
	private TextView infoTv;
	private GifMovieView gifView;
	private ImageView imageView;
	private boolean isGoogleOK;
	
	private final int MSG_REPLAY_TIME = 1;
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_REPLAY_TIME:
				if(gifView != null) gifView.setVisibility(View.GONE);
				if(operationTv != null) operationTv.setVisibility(View.VISIBLE);
				if(statusTv != null) statusTv.setGravity(Gravity.LEFT);
				if(imageView != null) imageView.setVisibility(View.VISIBLE);
				if(isGoogleOK){
					imageView.setImageResource(R.drawable.google_right);
					statusTv.setText("谷歌服务框架和市场已安装，快去下载体验精彩游戏吧~");
					operationTv.setText("确定");
					operationTv.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							finish();
						}
					});
					

				}else{
					imageView.setImageResource(R.drawable.google_wrong);
					statusTv.setText("尚未安装‘谷歌服务框架’，部分大型游戏可能出现黑屏闪退的现象，建议使用‘谷歌安装器’一键安装");
					operationTv.setText("下载谷歌安装器");
					infoTv.setVisibility(View.VISIBLE);
					operationTv.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
//							Asset asset = new Asset();
//							asset.pkgName = "com.appchina.googleinstaller";
//							AppDetailActivity.launch(mActivity, asset, "GoogleInstallerActivity");
						}
					});
				}
				
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_google_installer);
		setTitle("谷歌检测");
		mActivity = this;
		
		initView();
		initData();
	}
	
	private void initView(){
		imageView = (ImageView) findViewById(R.id.imageView);
		statusTv = (TextView) findViewById(R.id.statusTv);
		operationTv = (TextView) findViewById(R.id.operationTv);
		infoTv = (TextView) findViewById(R.id.infoTv);
		gifView = (GifMovieView) findViewById(R.id.gifView);
		
		infoTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				GoogleServerGuideActivity.launch(mActivity);
			}
		});
	}
	
	private void initData(){
		statusTv.setGravity(Gravity.CENTER);
		statusTv.setText("正在进行谷歌检测...");
		operationTv.setVisibility(View.INVISIBLE);
		infoTv.setVisibility(View.INVISIBLE);
		imageView.setVisibility(View.INVISIBLE);
		gifView.setMovieResource(R.raw.scaning);
		
		isGoogleOK = isInstalled();
//		isGoogleOK = false;
		mHandler.sendEmptyMessageDelayed(MSG_REPLAY_TIME,2000);
	}

	public  boolean isInstalled(){
		String[] appIds = new String[]{"com.android.vending", "com.google.android.gsf"};
		boolean[] installStatus = new boolean[]{false, false};
		PackageManager packageManager = getPackageManager();
		List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
		for(PackageInfo packageInfo : packageInfos){
			for(int w = 0; w < appIds.length; w++){
				String appId = appIds[w];
				if(packageInfo.packageName.equals(appId)){
					installStatus[w] = true;
				}
			}
		}
		boolean installed = true;
		for(int w = 0; w < installStatus.length; w++){
			if(!installStatus[w]){
				installed = false;
				break;
			}
		}
		return installed;
	}

}
