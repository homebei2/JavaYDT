package com.wing.ydt;

import com.wing.ydt.db.DBAdapter;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
public class Application extends android.app.Application {
	private static final String ERROR_CANT_LOAD_PACKAGE_INFO = "Can't load package info";
	private static final String DEFAULT_APPLICATION_VERSION = "1.0";
	private static final int DEFAULT_APPLICATION_VERSION_CODE = 1;
	public static final int SWIPE_MIN_DISTANCE=120;
	public static final int SWIPE_THRESHOLD_VELOCITY=200;
	public static String APPLICATION_VERSION = DEFAULT_APPLICATION_VERSION;
	public static int APPLICATION_VERSION_CODE = DEFAULT_APPLICATION_VERSION_CODE;
	public static final int MESSAGE=1,WMESSAGE=4;
	public static final int CATEGORY=2, WMESSAGELIST=3; 
	public static final int ADD = 1;
	public static final int EDIT = 2;
	public static final int FAVORITE = 7;
	public static LayoutInflater inflater;
	
	private static final String DEBUG_METADATA_KEY = "debug";
	public static boolean IS_DEBUG = false;
	
	public static final int QUERYFace = 11,QUERYUnFace=12,FAVFace=21,FAVUnFace=22;
	//TODO: do not expose this, make util class to allow for getting resources.
	private static Context applicationContext;
	public Application() {
		super();
	}
	
	@Override
	public void onCreate(){
		super.onCreate();
		applicationContext = getApplicationContext();
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		try {
			DBAdapter.copyDB();
			PackageInfo packageInfo =
				applicationContext.getPackageManager().getPackageInfo(applicationContext.getPackageName(), 0);
			APPLICATION_VERSION = packageInfo.versionName;
			APPLICATION_VERSION_CODE = packageInfo.versionCode;
			
			ApplicationInfo applicationInfo = 
				getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
			Bundle applicationMetaData = applicationInfo.metaData;			
			IS_DEBUG = applicationMetaData.getBoolean(DEBUG_METADATA_KEY);
		} catch (NameNotFoundException e) {
			Log.e(getClass().getName(), ERROR_CANT_LOAD_PACKAGE_INFO, e);
			throw new WingException(ERROR_CANT_LOAD_PACKAGE_INFO, e);
		}	
	}
	@Override
	public void onTerminate() {
		//		
		DBAdapter.getInstance().close();
	}
	
	@Override
	public void onLowMemory() {
		// TODO Shao. Check if we need to clear the other caches

	}
	
	public static Context getContext() {
		return applicationContext;
	}
	public static boolean UIThreadStillActive() {
		return applicationContext != null && applicationContext.getApplicationContext() != null; 
}
}
