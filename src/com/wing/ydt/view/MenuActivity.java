package com.wing.ydt.view;

import java.util.ArrayList;

import android.R.color;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AbsoluteLayout;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.wing.ydt.R;
import com.wing.ydt.db.DBAdapter;
import com.wing.ydt.view.component.CommonImageButton;
import com.wing.ydt.vo.Message;

public class MenuActivity extends Activity implements OnClickListener {
	// top bar button
	protected CommonImageButton backBtn;
	protected CommonImageButton searchBtn;
	protected boolean isDisplaySearch=false;
	protected LinearLayout  globalLayout;
	protected LinearLayout topLayout;
	protected LinearLayout searchLayout;
	protected CommonImageButton goBtn=null;
	protected EditText searchEditText;
	private LinearLayout lLayout;
	protected static int screenHeigh;
	protected static int screenWidth;
	private static final String TAG="com.wing.wp.view.MenuActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		Display display = getWindowManager().getDefaultDisplay(); 
		screenHeigh = display.getHeight();
		//Log.i(TAG, "screenHeigh:"+screenHeigh);
		screenWidth = display.getWidth();
		
		globalLayout = new LinearLayout(this);
		globalLayout.setOrientation(LinearLayout.VERTICAL);
		globalLayout.setBackgroundResource(R.drawable.bg);
		
		topLayout=createTopBar();
		globalLayout.addView(topLayout);
		
		lLayout = createSearchBar();
		globalLayout.addView(lLayout);
		Log.i(TAG, "Push Activity from statck£º"+this.toString());
		ScreenManager.getScreenManager().pushActivity(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.FLAG_KEEP_TOUCH_MODE) {
			Log.i(TAG, "Remove Activity from statck£º"+this.toString());
			ScreenManager.getScreenManager().popActivity();
		}
		return super.onKeyDown(keyCode, event);
		
	}

	protected LinearLayout createTopBar() {
		LinearLayout topLayout  = new LinearLayout(this);
		
		CommonImageButton  titleBtn =  new CommonImageButton(this, R.drawable.top,
				R.drawable.top);
		LayoutParams titleParams =  new LayoutParams(screenWidth, 30);
		topLayout.addView(titleBtn, titleParams);
		return topLayout;
	}
	protected LinearLayout createSearchBar() {
		LinearLayout lLayout = new LinearLayout(this);
		goBtn = new CommonImageButton(this, R.drawable.home_go,
				R.drawable.home_go_over);
		goBtn.setOnClickListener(this);
		searchEditText = new EditText(this);
		LayoutParams editTextParams = new LayoutParams(270, 50);
		LayoutParams goBtnParams = new LayoutParams(50, 50);

		lLayout = new LinearLayout(this);
		lLayout.setOrientation(LinearLayout.HORIZONTAL);
		lLayout.addView(searchEditText, editTextParams);
		lLayout.addView(goBtn, goBtnParams);
		return lLayout;
	}
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		 super.onMenuItemSelected(featureId, item);
		 Intent intent  = new Intent();
		 switch (item.getItemId()) {
			case 0:
				ScreenManager.getScreenManager().popAllActivityExceptOne(
						SplashActivity.class);
				break;
			case 1:
				new AlertDialog.Builder(this)
					.setTitle(R.string.thanks)
					.setMessage(R.string.app_about).setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								/* User clicked OK so do some stuff */
							}
						}).create().show();
				break;
			case 2:
				ScreenManager.getScreenManager().popAllActivity();
				android.os.Process.killProcess(android.os.Process.myPid());
				break;
		 }
		 
		 return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		 super.onCreateOptionsMenu(menu);
		 menu.add(0, 0, 0, R.string.Home);
		 menu.add(0, 1, 1, R.string.About);
		 menu.add(0, 2, 2, R.string.Quit);
		 return true;
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == goBtn) {
			if (searchEditText.getText().toString().trim().length() <= 0) {
				new AlertDialog.Builder(this).setMessage(
						R.string.not_null).setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								/* User clicked OK so do some stuff */
							}
						}).create().show();
			} else {
				DBAdapter db = new DBAdapter(this);
				db.open();
				ArrayList<Message> messTempList = db
						.QueryMessages(searchEditText.getText().toString()
								.trim());
				db.close();
				if (messTempList != null && messTempList.size() > 0) {
					Log.i(TAG, ":" + messTempList.size());
					Intent intent = new Intent();
					intent.setClass(this,
									ListMessActivity.class);
					Bundle bundle = new Bundle();
					bundle.putParcelableArrayList("MessageList", messTempList);
					intent.putExtras(bundle);
					this.startActivity(intent);
				} else {
					new AlertDialog.Builder(this).setMessage(
							R.string.no_message).setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									/* User clicked OK so do some stuff */
								}
							}).create().show();
				}
			}
		}
	}

}
