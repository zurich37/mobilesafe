package com.zurich.mobile.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.util.Log;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class StorageUtils {
	/**
	 * 获取所有可用的SD卡的路径
	 * @return 所有可用的SD卡的路径
	 */
	@SuppressLint("LongLogTag")
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public static String[] getAllAvailableSdcardPath(Context context){
		// 获取所有的存储器的路径
		if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1){
			if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
				return new String[]{Environment.getExternalStorageDirectory().getPath()};
			}else{
				return null;
			}
		}

		String[] paths;
		Method getVolumePathsMethod;
		try {
			getVolumePathsMethod = StorageManager.class.getMethod("getVolumePaths");
		} catch (NoSuchMethodException e) {
			Log.e("getAllAvailableSdcardPath", "not found StorageManager.getVolumePaths() method");
			if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
				return new String[]{Environment.getExternalStorageDirectory().getPath()};
			}else{
				return null;
			}
		}

		StorageManager sm = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
		try {
			paths = (String[]) getVolumePathsMethod.invoke(sm);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}

		if(paths == null || paths.length == 0){
			return null;
		}

		// 去掉不可用的存储器
		List<String> storagePathList = new LinkedList<String>();
		Collections.addAll(storagePathList, paths);
		Iterator<String> storagePathIterator = storagePathList.iterator();

		String path;
		Method getVolumeStateMethod = null;
		while(storagePathIterator.hasNext()){
			path = storagePathIterator.next();
			if(getVolumeStateMethod == null){
				try {
					getVolumeStateMethod = StorageManager.class.getMethod("getVolumeState", String.class);
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
					return null;
				}
			}
			String status;
			try {
				status = (String) getVolumeStateMethod.invoke(sm, path);
			} catch (Exception e) {
				e.printStackTrace();
				storagePathIterator.remove();
				continue;
			}
			if(!(Environment.MEDIA_MOUNTED.equals(status) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(status))){
				storagePathIterator.remove();
			}
		}
		return storagePathList.toArray(new String[storagePathList.size()]);
	}

	/**
	 * 寻找除默认存储外空间足够的SD卡
	 * @param context
	 * @return 空间足够的SD卡的路径
	 */
	public static String findAvailableSdcardBySpaceEnough(Context context, String dir, long needSize){
		// 取得所有SD卡的信息
		String[] sdcardPaths = getAllAvailableSdcardPath(context);
		if(sdcardPaths == null || sdcardPaths.length < 2){
			return null;
		}

		// 拿到默认存储的路径
		String defaultStoragePath = null;
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
			defaultStoragePath = Environment.getExternalStorageDirectory().getPath();
		}

		for(String sdcardPath : sdcardPaths){
			// 跳过默认存储
			if(sdcardPath.equals(defaultStoragePath)){
				continue;
			}

			// 判断是否有足够的空间
			File dataPackageDecompressDir;
			if(dir != null){
				dataPackageDecompressDir = new File(sdcardPath+ File.separator+dir);
			}else{
				dataPackageDecompressDir = new File(sdcardPath);
			}
			if(getDirAvailableSize(dataPackageDecompressDir)>needSize){
				return sdcardPath;
			}
		}
		return null;
	}

	/**
	 * 获取给定目录的可用大小
	 * @param dir 被检查的目录
	 * @return 可用大小，目录不存在并且创建失败就会返回0
	 */
	@SuppressWarnings("deprecation")
	public static long getDirAvailableSize(File dir){
		if(!dir.exists() && !dir.mkdirs()){
			return 0;
		}
		StatFs dirStatFs = new StatFs(dir.getPath());
		return (long) dirStatFs.getAvailableBlocks() * dirStatFs.getBlockSize();
	}
}
