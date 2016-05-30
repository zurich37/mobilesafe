package com.zurich.mobile.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * 分享工具类
 * Created by weixinfei on 16/5/30.
 */
public class ShareUtil {
    public static void shareImage(Context context, Uri uri){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpeg");//图片

        //File image = new File(Environment.getExternalStorageDirectory().getPath()+"/DCIM/Screenshots/lock_wallpaper.jpg");
        //Uri uri = Uri.fromFile(image);//图片路径

        intent.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(Intent.createChooser(intent,"分享妹纸"));
    }
}
