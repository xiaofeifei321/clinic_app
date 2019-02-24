package com.hospital.util;

import java.io.File;



import com.hospital.yuyue.R;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;


/**
 * ��ȡϵͳ��Ϣ�Ĺ�����
 */
public class SystemHelper {
	private SystemHelper() {
	}

	/**
	 * ������Ӧ�õ������ݷ�ʽ<br/>
	 * ע�⣺��Ҫ���Ȩ��&lt;uses-permission android:name="com.android.launcher.permission.INSTALL_SHORTCUT"/&gt;  
	 * @param paramContext
	 */
	public static void createShortcut(Context context, Class<?> clazz){
		Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
			
		//��ݷ�ʽ������
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(R.string.app_name));
		shortcut.putExtra("duplicate", false); //�������ظ�����
			
		Intent localIntent2 = new Intent(context, clazz);
	    localIntent2.setAction(Intent.ACTION_MAIN);
	    localIntent2.addCategory(Intent.CATEGORY_LAUNCHER);
	    
	    shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, localIntent2);//ָ����ݷ�ʽҪ������Activity����
		
		//��ݷ�ʽ��ͼ��
		ShortcutIconResource iconResource = Intent.ShortcutIconResource.fromContext(context, R.drawable.ic_launcher);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconResource);
			
		context.sendBroadcast(shortcut);
	}

	/**
	 * ����Ƿ��Ѿ������������ݷ�ʽ<br/>
	 * ע�⣺��Ҫ���Ȩ��&lt;uses-permission android:name="com.android.launcher.permission.READ_SETTINGS"/&gt;  
	 * @param ctx
	 * @return
	 */
	public static boolean hasShortCut(Context context) {
		String url = "";
		if (android.os.Build.VERSION.SDK_INT < 8) {
			url = "content://com.android.launcher.settings/favorites?notify=true";
		} else {
			url = "content://com.android.launcher2.settings/favorites?notify=true";
		}
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(Uri.parse(url), null, "title=?",
				new String[] { context.getString(R.string.app_name) }, null);

		if (cursor != null && cursor.moveToFirst()) {
			cursor.close();
			return true;
		}

		return false;
	}
	/**
	 * ��ȡ��ǰ��������Ļ��Ϣ����<br/>
	 * ���⣺ͨ��android.os.Build����Ի�ȡ��ǰϵͳ�������Ϣ
	 * @param context
	 * @return
	 */
	public static DisplayMetrics getScreenInfo(Context context) {
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(dm);
		// dm.widthPixels;//����
		// dm.heightPixels; //�߶�
		// dm.density; //�ܶ�
		return dm;
	}

	/**
	 * ��ȡ�ֻ���<br/>
	 * ע�⣺��Ҫ���Ȩ��&lt;uses-permission
	 * android:name="android.permission.READ_PHONE_STATE"/&gt;������ܶ��ֻ����ܻ�ȡ����ǰ�ֻ���
	 * 
	 * @param context
	 * @return
	 */
	public static String getMobileNumber(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getLine1Number();
	}

	/**
	 * ��⵱ǰ�����������Ƿ����<br/>
	 * ע�⣺��Ҫ���Ȩ��&lt;uses-permission
	 * android:name="android.permission.ACCESS_NETWORK_STATE"/&gt;
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConnected(Context context) {
		boolean flag = false;
		try {
			ConnectivityManager connManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (null != connManager) {
				NetworkInfo info = connManager.getActiveNetworkInfo();
				if (null != info && info.isAvailable()) {
					flag = true;
				}
			}
		} catch (Exception e) {
			Log.e("NetworkInfo", "Exception", e);
		}
		return flag;
	}

	/**
	 * ��⵱ǰ�������ӵ�����<br/>
	 * ע�⣺��Ҫ���Ȩ�� <uses-permission  android:name="android.permission.ACCESS_NETWORK_STATE"/>;
	 * @param context
	 * @return ����0����GPRS����;����1,����WIFI����;����-1�������粻����
	 */
	public static int getNetworkType(Context context) {
		int code = -1;
		try {
			ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (null != connManager) {
				State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
				if (State.CONNECTED == state) {
					code = ConnectivityManager.TYPE_WIFI;
				} else {
					state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
					if (State.CONNECTED == state) {
						code = ConnectivityManager.TYPE_MOBILE;
					}
				}
			}
		} catch (Exception e) {
			Log.e("NetworkInfo", "Exception", e);
		}
		return code;
	}

	/**
	 * ���ص�ǰ����汾����,��:1
	 * @param context
	 * @return ��ǰ����汾����
	 */
	public static int getAppVersionCode(Context context) {
		int versionCode = -1;
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionCode = pi.versionCode;

		} catch (Exception e) {
			Log.e("VersionInfo", "Exception", e);
		}
		return versionCode;
	}

	/**
	 * ���ص�ǰ����汾��,��:1.0.1
	 * 
	 * @param context
	 * @return ��ǰ����汾��
	 */
	public static String getAppVersionName(Context context) {
		String versionName = "";
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;

		} catch (Exception e) {
			Log.e("VersionInfo", "Exception", e);
		}
		return versionName;
	}
	

	/**
	 * ��װָ����APK�ļ�����Ҫ���ڱ�Ӧ�ó���ĸ���
	 * 
	 * @param context
	 * @param apk
	 *            (apk�ļ���ȫ·����)
	 */
	public static void installAPK(Context context, String apk) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(apk)),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}
}
