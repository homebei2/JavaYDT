package com.wing.ydt.handler;

import java.net.URI;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.wing.ydt.Application;
import com.wing.ydt.R;
import com.wing.ydt.view.EditActivity;
import com.wing.ydt.vo.ListItem;


public class UIFactory {
		public static UIFactory instance = new UIFactory();
		private Context context;
		private View s;
		private UIFactory() {
			 context = Application.getContext();
		}
		public View inflate(int id){
			return Application.inflater.inflate(id, null);
		}
		/**
		 * 该方法有缺陷，无法清除应用程序所有的Activity，只能finish当前Activity.
		 * @return
		 */
		public boolean quit(){
			android.os.Process.killProcess(android.os.Process.myPid());
			ActivityManager am = (ActivityManager)context.getSystemService  (Context.ACTIVITY_SERVICE);   
            am.restartPackage(context.getPackageName());
			return true;
		}
		public void startActivityForResult(Activity activity,ListItem item,Boolean isAdd){
			Intent intent = new Intent(context,EditActivity.class);
			Bundle bundle = new Bundle();
			bundle.putParcelable("item", item);
			bundle.putBoolean("isAdd", isAdd);
			intent.putExtras(bundle);
			activity.startActivityForResult(intent,isAdd?Application.ADD:Application.EDIT);
		}
		public void showMessageToastOnUIThread (Activity activity, final int stringId, final int length) {
			showMessageToastOnUIThread(activity, Application.getContext().getString(stringId), length);
		}
		
		private void showMessageToastOnUIThread (Activity activity, final String message, final int length) {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					 if (Application.UIThreadStillActive()) {
						 showMessageToast(message, length);
					 }	
				}
			});
		}
		public void showMessageToast(String message, int length) {
		    Toast messageToast = Toast.makeText(Application.getContext(), message, length);
		    int yOffset = (int) Application.getContext().getResources().getDimension(R.dimen.toast_y_offset);
		    messageToast.setGravity(Gravity.CENTER, 0, yOffset);
		    try {
				if (Application.UIThreadStillActive()) {
		    messageToast.show();
		}
			} catch (Exception e) {
				Log.e(UIFactory.class.getName(), "Showing a toast failed!", e);
			}
		}
		public void displayDeleteConfirmationDialog(Activity activity,String message,final Thread run) {
			Builder builder = new AlertDialog.Builder(activity)
				.setTitle(R.string.Delete)
		    	.setMessage(message)
		    	.setPositiveButton(R.string.Delete, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						run.start();
					}
		    	})
		    	.setNegativeButton(R.string.Cancel, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
		    	});
			
			createAndShowDialogOnUIThread(activity, builder);
		}
		public void createAndShowDialogOnUIThread(final Activity activity, final AlertDialog.Builder dialogBuilder) {
			activity.runOnUiThread(new Runnable() {
				public void run() {
					try {
						AlertDialog dialog = dialogBuilder.create();
						showDialogOnUIThread(activity, dialog);
					} catch (Exception e) {
						Log.e(UIFactory.class.getName(), "Building a dialog failed!", e);
					}
				}
	    	});
		}
		public void showDialogOnUIThread (final Activity activity, final Dialog dialog) {
			activity.runOnUiThread(new Runnable() {
				public void run() {
					try {
						if (Application.UIThreadStillActive()) {
							dialog.show();
						}
					} catch (Exception e) {
						Log.e(UIFactory.class.getName(), "Showing a dialog failed!", e);
					}
				}
	    	});
		}
		public void displayAboutDialog(final Activity activity) {
			View view = inflate(R.layout.about);
			TextView tv =(TextView) view.findViewById(R.id.About);
			tv.setText(String.format(activity.getString(R.string.app_about),Application.APPLICATION_VERSION));
			Builder builder = new AlertDialog.Builder(activity)
				.setTitle(R.string.About)	
				.setView(view)
		    	.setPositiveButton(R.string.AboutUser, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(isMarketInstalled())
							activity.startActivity(getMarketDetailIntent());
						else
							dialog.dismiss();
					}
		    	})
		    	.setNegativeButton(R.string.AboutOK, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
		    	});
			
			createAndShowDialogOnUIThread(activity, builder);
		}
	    public Intent getMarketDetailIntent() {
	    	return new Intent(Intent.ACTION_VIEW,Uri.parse("market://search?q=pub:yale"));
	    }
	    
	    
	    public boolean isMarketInstalled() {
	    	PackageManager pm = Application.getContext().getPackageManager();
	    	return !pm.queryIntentActivities(getMarketDetailIntent(), 0).isEmpty();
	    }
}
