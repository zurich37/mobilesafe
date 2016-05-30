package com.zurich.mobile.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 保存图片
 * Created by weixinfei on 16/5/30.
 */
public class ImageUtil {
    public static Uri saveImage(Context context, String url, Bitmap bitmap, ImageView imageView, String tag) {
        //图片存储路径
        String imgDir = Environment.getExternalStorageDirectory().getPath() + "/MeizhiPic";
        //图片名称处理
        String[] strings = url.substring(url.lastIndexOf("/") + 1).split("\\.");
        String fileName = strings[0] + ".png";
        //创建文件路径
        File fileDir = new File(imgDir);
        if (!fileDir.exists()){
            fileDir.mkdir();
        }
        //创建文件
        File imgFile = new File(fileDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(imgFile);
            boolean compress = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            if (tag.equals("save")){
                if (compress){
                    Snackbar.make(imageView,"~图片保存到图库啦~",Snackbar.LENGTH_SHORT).show();
                }else {
                    Snackbar.make(imageView,"保存失败，请重试...( ＞ω＜)",Snackbar.LENGTH_SHORT).show();
                }
            }
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri uri = Uri.fromFile(imgFile);
        //发送广播，通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,uri));
        return uri;
    }
}
