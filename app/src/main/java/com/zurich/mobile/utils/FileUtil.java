package com.zurich.mobile.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class FileUtil {

    private static final String TAG = FileUtil.class.getSimpleName();

    private static final String[] markets = new String[]{"Yingyonghui/yyhdownload/",
            "Yingyonghui_backup/", "wanduojia/app/", "360Download/",
            "baidu/AppSearch/downloads/", "bao/Apk/",
            "91 wireless/PandaSpace/apps/", "gomark/download/", "nDuoaMarket/",
            "mumayi/download/", "digua/downs/", "gfan/market/",
            "MzwDownloads/", "MzwBackup/download/", "hispace/application/",
            ".LeStore/download/", "mm/download/", "store_download/",
            "wostore/", "Download/", "baidu/flyflow/downloads", "UCDownloads/",
            "360/download/", "muzhiwan/com.muzhiwan.market/gpk/"};
    private static final String[] ex_markets = new String[]{"Yingyonghui/",
            "muzhiwan/", "Yingyonghui_backup/", "wanduojia/", "360Download",
            "baidu/", "bao/", "91 wireless/", "gomark/", "nDuoaMarket/",
            "mumayi/", "digua/", "gfan/", "MzwDownloads/", "MzwBackup/",
            "hispace/", ".LeStore/", "mm/", "store_download/", "wostore/",
            "Download/", "UCDownloads/", "Android/", "DICM/", "image/",
            "Movies/", "music/", "Pictures/", "360/download/"};

    /**
     * 是否有可读写外部sd卡
     *
     * @return
     */
    public static boolean isSDCardMounted() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        }

        return false;
    }

    /**
     * 获得sdcard 剩余空间字节数
     *
     * @return byte
     */
    public static long getSDCardFreespace() {
        File sdcardDir = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(sdcardDir.getPath());
        long blockSize = sf.getBlockSize(); // block大小
        long availCount = sf.getAvailableBlocks();
        sdcardDir = null;
        sf = null;

        return availCount * blockSize;
    }

    public static long getSDCardTotalSpace() {
        File sdcardDir = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(sdcardDir.getPath());
        long blockSize = sf.getBlockSize(); // block大小
        long count = sf.getBlockCount();
        sdcardDir = null;
        sf = null;

        return blockSize * count;
    }

    public static File getSplashCacheDir(Context mContext) {
        return mContext.getCacheDir();
    }

    /**
     * 获得手机system 剩余空间字节数
     *
     * @return byte
     */
    public static long getSystemFreespace() {
        File root = Environment.getDataDirectory();
        StatFs sf = new StatFs(root.getPath());
        long blockSize = sf.getBlockSize();
        long availCount = sf.getAvailableBlocks();
        root = null;
        sf = null;

        return availCount * blockSize;
    }

    public static long getSystemTotalSpace() {
        File root = Environment.getDataDirectory();
        StatFs sf = new StatFs(root.getPath());
        long blockSize = sf.getBlockSize();
        long blockCount = sf.getBlockCount();
        root = null;
        sf = null;

        return blockCount * blockSize;
    }

    public static String readAssetsFile(Context context, String fileName) {
        StringBuffer fileContent = new StringBuffer();
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        try {
            inputStream = context.getAssets().open(fileName);
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
            String str;
            while ((str = reader.readLine()) != null) {
                fileContent.append(str).append("\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                if (inputStreamReader != null)
                    inputStreamReader.close();
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return fileContent.toString();
    }

    public static String readFile(File file) {
        if (!file.exists())
            return null;

        StringBuilder builder = new StringBuilder();
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        try {
            inputStream = new FileInputStream(file);
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
            String str;
            while ((str = reader.readLine()) != null) {
                builder.append(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (reader != null)
                    reader.close();
                if (inputStreamReader != null)
                    inputStreamReader.close();
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return builder.toString();
    }

    public static void copyFile(File src, File dest) throws IOException {
        int size = 8192 * 4;
        FileInputStream fis = new FileInputStream(src);
        InputStream inputStream = new BufferedInputStream(fis, size);
        FileOutputStream fos = new FileOutputStream(dest);
        OutputStream outputStream = new BufferedOutputStream(fos, size);

        byte[] buf = new byte[4096];
        int i;
        while ((i = inputStream.read(buf)) != -1) {
            outputStream.write(buf, 0, i);
        }

        fos.flush();
        outputStream.flush();

        inputStream.close();
        outputStream.close();
        fis.close();
        fos.close();
    }

    public static String subSuffix(String fileNameOrPath) {
        if (fileNameOrPath == null) {
            return null;
        }

        int dotIndex = fileNameOrPath.lastIndexOf('.');
        if (dotIndex == -1) {
            return null;
        }

        return fileNameOrPath.substring(dotIndex);
    }


    /**
     * /data/data/.../files 从这两个文件夹中清除数据
     * /data/data/.../cache
     *
     * @param context
     */
    public static void cleanCachedFiles(Context context, boolean deleteApk) {
        File directory = context.getFilesDir();
        File[] files = directory.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (!deleteApk && file.getName().endsWith(".apk")) {// 当sdcard
                    // 未挂载的时候，apk有可能会下在这里。
                    continue;
                }
                file.delete();
            }
        }

        File CachedDirectory = context.getCacheDir();
        File[] cachedFiles = CachedDirectory.listFiles();
        if (cachedFiles != null) {
            for (int i = 0; i < cachedFiles.length; i++) {
                cachedFiles[i].delete();
            }
        }

    }

    public static boolean safeCreateFile(File file) throws IOException {
        if (file.exists()) {
            deleteFile(file);
        }
        File parentFile = file.getParentFile();
        if (parentFile != null && !parentFile.exists()) {
            parentFile.mkdirs();
        }
        return file.createNewFile();
    }

    public static boolean deleteFile(File file) {
        boolean result = true;
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null && files.length > 0) {
                    for (int i = 0, count = files.length; i < count; i++) {
                        result &= deleteFile(files[i]);
                    }
                }
                result &= file.delete(); // Delete empty directory.
            } else {
                result &= file.delete();
            }
        }
        return result;
    }

    /**
     * 删除列表缓存文件 <br>
     * 仅在该版本在第一次启动时被调用 <br>
     *
     * @param mContext
     */
    public static void deleteApiCache(Context mContext) {
        File cacheDir = mContext.getCacheDir();
        if (cacheDir != null) {
            File[] files = cacheDir.listFiles();

            if (files != null) {
                for (File file : files)
                    file.delete();
            }
        }
        /*
		 * if
		 * (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED
		 * )) { try { File dirSD = new File(MarketConstants.API_CACHE_SUBDIR);
		 * File[] filesSD = dirSD.listFiles(); if (filesSD != null) { for (int i
		 * = 0; i < filesSD.length; i++) { File file = filesSD[i];
		 * file.delete(); } } } catch (Exception e) { e.printStackTrace(); } }
		 */
    }

    /**
     * 为路径创建文件夹
     *
     * @param dirPath
     */
    public static void makeDir(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    // /**
    // * 得到小头像的File文件
    // * @return
    // */
    // public static File getSmallHeadPicture(AccountInfo accountInfo) {
    // File picSmall = null;
    // picSmall = new File(getSmallHeadPicLocation(accountInfo));
    // if (!picSmall.exists()){
    // picSmall = null;
    // }
    // return picSmall;
    // }
    // /**
    // * 得到大头像的File文件
    // * @return
    // */
    // public static File getBigHeadPicture(AccountInfo accountInfo) {
    // File picBig = null;
    // picBig = new File(getBigHeadPicLocation(accountInfo));
    // if (!picBig.exists()){
    // picBig = null;
    // }
    // return picBig;
    // }

    // public static String getSmallHeadPicLocation(AccountInfo accountInfo) {
    // String picName = accountInfo.uid
    // + MarketConstants.NAME_HEADPICTURE_SMALL;
    // picName = getHexNameForSourceName(picName);
    // return picName == null ? null : MarketConstants.IMG_HEAD_PICTURE_USED
    // + "/" + picName;
    // }
    //
    // public static String getBigHeadPicLocation(AccountInfo accountInfo) {
    // String picName = accountInfo.uid + MarketConstants.NAME_HEADPICTURE_BIG;
    // picName = getHexNameForSourceName(picName);
    // return picName == null ? null : MarketConstants.IMG_HEAD_PICTURE_USED
    // + "/" + picName;
    // }
    // /**
    // * 删除大头像、小头像File
    // * @param accountInfo
    // */
    // public static void deleteHeadPictures(AccountInfo accountInfo){
    // File picSmall=getSmallHeadPicture(accountInfo);
    // File picBig=getBigHeadPicture(accountInfo);
    // Log.i("test",
    // picSmall.getAbsolutePath()+" ;;;;"+picBig.getAbsolutePath());
    // if(picSmall!=null&&picSmall.exists()){
    // picSmall.delete();
    // }
    // if(picBig!=null&&picBig.exists()){
    // picBig.delete();
    // }
    // }

    /**
     * JAVA判断字符串数组中是否包含某字符串元素
     *
     * @param substring 某字符串
     * @param source    源字符串数组
     * @return 包含则返回true，否则返回false
     */
    public boolean isIn(String substring, String[] source) {
        if (source == null || source.length == 0) {
            return false;
        }
        for (int i = 0; i < source.length; i++) {
            String aSource = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/" + source[i];
            if (aSource.equals(substring)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取文件长度，此方法的关键点在于，他也能获取目录的长度
     *
     * @param file
     * @return
     */
    public static long countFileLength(File file) {
        if (file == null || !file.exists()) {
            return 0;
        }

        long length = 0;
        if (file.isFile()) {
            length += file.length();
        } else {
            File[] files = file.listFiles();
            if (files != null) {
                for (File childFile : files) {
                    length += countFileLength(childFile);
                }
            }
        }
        return length;
    }
}
