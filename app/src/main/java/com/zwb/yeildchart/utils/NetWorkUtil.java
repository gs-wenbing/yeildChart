package com.zwb.yeildchart.utils;


import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class NetWorkUtil {
	public static ConnectivityManager manager;
//	public static Context context;
	public static final int NETWORK_TYPE_NONE = 0; // 断网情况
	public static final int NETWORK_TYPE_WIFI = 1; // WiFi模式
	public static final int NETWOKR_TYPE_MOBILE = 2; // gprs模式

	/**
	 * 判断当前是否为CMWAP接入点
	 *
	 * @return
	 */
	public static boolean isCmwap() {
		if (manager != null) {
			NetworkInfo netWrokInfo = manager.getActiveNetworkInfo();
			if (netWrokInfo != null
					&& netWrokInfo.getTypeName().equalsIgnoreCase("MOBILE")
					&& "cmwap".equalsIgnoreCase(netWrokInfo.getExtraInfo())) {
				return true;
			}
		}
		return false;
	}

	public static boolean isNetworkAvailable(Context mContext) {
		if (mContext == null) {
			return false;
		}
		ConnectivityManager connectivity = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

//	public static int getNetworkType(Context mContext) {
//		if (mContext == null) {
//			return 0;
//		}
//		Context context = mContext;
//		ConnectivityManager connectivity = (ConnectivityManager) context
//				.getSystemService(Context.CONNECTIVITY_SERVICE);
//		if (connectivity == null) {
//			return 0;
//		} else {
//			NetworkInfo wifi = connectivity
//					.getNetworkInfo(ConnectivityManager.TYPE_WIFI); // wifi
//			NetworkInfo gprs = connectivity
//					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); // gprs
//			if (wifi != null && wifi.getState() == NetworkInfo.State.CONNECTED) {
//				return NETWORK_TYPE_WIFI;
//			} else if (gprs != null && gprs.getState() == NetworkInfo.State.CONNECTED) {
//				return NETWOKR_TYPE_MOBILE;
//			} else {
//				return NETWORK_TYPE_NONE;
//			}
//		}
//	}

	public static int getNetworkType(Context context){
		int netType=-1;
		String strNetworkType = "";
		try{
			NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnected()){
				if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
					strNetworkType = "WIFI";
					netType = 3;
				}
				else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
					String _strSubTypeName = networkInfo.getSubtypeName();
					// TD-SCDMA   networkType is 17
					int networkType = networkInfo.getSubtype();
					switch (networkType) {
						case TelephonyManager.NETWORK_TYPE_GPRS:
						case TelephonyManager.NETWORK_TYPE_EDGE:
						case TelephonyManager.NETWORK_TYPE_CDMA:
						case TelephonyManager.NETWORK_TYPE_1xRTT:
						case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
							strNetworkType = "2G";
							netType = 0;
							break;
						case TelephonyManager.NETWORK_TYPE_UMTS:
						case TelephonyManager.NETWORK_TYPE_EVDO_0:
						case TelephonyManager.NETWORK_TYPE_EVDO_A:
						case TelephonyManager.NETWORK_TYPE_HSDPA:
						case TelephonyManager.NETWORK_TYPE_HSUPA:
						case TelephonyManager.NETWORK_TYPE_HSPA:
						case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
						case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
						case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
							strNetworkType = "3G";
							netType = 1;
							break;
						case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
							strNetworkType = "4G";
							netType = 2;
							break;
						default:
							// http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
							if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA")
									|| _strSubTypeName.equalsIgnoreCase("WCDMA")
									|| _strSubTypeName.equalsIgnoreCase("CDMA2000")){
								strNetworkType = "3G";
								netType = 1;
							}
							else {
								strNetworkType = _strSubTypeName;
							}
							break;
					}
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return netType;//strNetworkType;
	}

	/**
	 * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
	 * @param context
	 * @return true 表示开启
	 */
	public static boolean GPSHasOpen(final Context context) {
		LocationManager locationManager
				= (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		// 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
		boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		// 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
		boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		return gps || network;
	}

	/**
	 * 判断当前网络是否是wifi网络.
	 *
	 * @param context
	 *            the context
	 * @return boolean
	 */
	public static boolean isWifi(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}
}
