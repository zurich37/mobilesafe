package com.zurich.mobile.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;

/**
 * ϵͳ��Ϣ�Ĺ�����
 * @author Administrator
 *
 */
public class SystemInfoUtils {

	/**
	 * ��ȡ�������еĽ��̵ĸ���
	 * @return ��������
	 */
	public static int getRunningProcessCount(Context context){
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		return am.getRunningAppProcesses().size();
	}
	
	/**��ȡ�ֻ����õ��ڴ���Ϣ ram
	 * 
	 * @param context
	 * @return ��λ��byte
	 */
	public static long getAvailRam(Context context){
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo = new MemoryInfo();
		am.getMemoryInfo(outInfo);
		return outInfo.availMem; //byteΪ��λ��long���͵Ŀ����ڴ��С
	}
	
	/**��ȡ�ֻ����ڴ���Ϣ ram
	 * 
	 * @param context
	 * @return ��λ��byte
	 */
	public static long getTotalRam(Context context){
//      �����api  totalmemֻ����16���ϰ汾��ʹ�á�
//		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//		MemoryInfo outInfo = new ActivityManager.MemoryInfo();
//		am.getMemoryInfo(outInfo);
//		return outInfo.totalMem;
		try {
			File file = new File("/proc/meminfo");
			FileInputStream fis = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			//MemTotal:         516452 kB  
			String line = br.readLine();
			//�ַ���  һ���ַ�--��
			StringBuffer sb  = new StringBuffer();
			for(char c : line.toCharArray()){
				if(c>='0'&&c<='9'){
					sb.append(c);
				}
			}
			return Integer.parseInt(sb.toString())*1024l;  //byte
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
}
