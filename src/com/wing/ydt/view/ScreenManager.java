package com.wing.ydt.view;

import java.util.Stack;
import android.app.Activity;
import android.util.Log;

public class ScreenManager {
	private static Stack<Activity> activityStack;
	private static ScreenManager instance;
	private final static String TAG="com.yahoomusic.view.ScreenManager";
	private  ScreenManager(){
	}
	public static ScreenManager getScreenManager(){
		if(instance==null){
			instance=new ScreenManager();
		}
		return instance;
	}
	public void popActivity(){
		Log.i(TAG, "popActivity:"+activityStack.size());
		if(!activityStack.isEmpty()){ // add by yale for NoSuchElementException in com.yahoomusic 
			Activity activity=activityStack.lastElement();
			if(activity!=null){
				activity.finish();
				activityStack.remove(activity);
				activity=null;
			}
		}
		
	}
	public void popActivity(Activity activity){
		if(activity!=null){
			activity.finish();
			activityStack.remove(activity);
			activity=null;
		}
	}
	public Activity currentActivity(){
		Activity activity=null;
		if(activityStack !=null && activityStack.size()>0){
			activity=activityStack.lastElement();
		}
		return activity;
	}
	public void pushActivity(Activity activity){
		if(activityStack==null){
			activityStack=new Stack<Activity>();
		}
		activityStack.add(activity);
	}
	
	public void popAllActivityExceptOne(Class cls){
		while(true){
			Activity activity=currentActivity();
			if(activity==null){
				break;
			}
			if(activity.getClass().equals(cls) ){
				break;
			}
			popActivity(activity);
		}
	}
	
	
	public void popAllActivity(){
		Log.i(TAG, "popAllActivity:"+activityStack.size());
		if(activityStack!=null && activityStack.size()>0){
			while(true){
				Activity activity=currentActivity();
				if(activity==null){
					break;
				}
				popActivity(activity);
			}
		}		
	}
	public int getAcitivityNumber(){
		if(activityStack==null){
			return 0;
		}else{
			return activityStack.size();
		}		
		
	}
}
