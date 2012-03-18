package com.wing.ydt.view;
import com.wing.ydt.R;
import com.wing.ydt.handler.UIFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class BaseActivity extends Activity {
	private final static String QUIT = "com.wing.app.quit";
	private QuitIntentReceiver quitReceiver;
	public static int density;
	protected int num;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		quitReceiver = new QuitIntentReceiver();
		IntentFilter filter = new IntentFilter(QUIT);
		registerReceiver(quitReceiver, filter);
	}

	private Handler quithandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			BaseActivity.this.finish();
		}
	};

	private class QuitIntentReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			quithandler.sendEmptyMessage(0);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(quitReceiver);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem it) {
		// TODO Auto-generated method stub
		int id = it.getItemId();
		switch(id){
		case R.id.about:
			UIFactory.instance.displayAboutDialog(this);
			return true;
		case R.id.quit:
			this.sendBroadcast(new Intent().setAction(QUIT));
			return true;
		default:
			return super.onOptionsItemSelected(it);
		}
	}
}
