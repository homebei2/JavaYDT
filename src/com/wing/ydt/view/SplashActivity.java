package com.wing.ydt.view;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;

import com.wing.ydt.R;
import com.wing.ydt.db.DBAdapter;

public class SplashActivity extends Activity implements AnimationListener{
	private final static String TAG = "com.wing.wp.db.SplashActivity";
	Animation an;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main);
		
	 an =AnimationUtils.loadAnimation(this, R.anim.fade);
		an.setAnimationListener(this);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		this.findViewById(R.id.main).startAnimation(an);
	}
	@Override
	public boolean onSearchRequested() {
		// TODO Auto-generated method stub
		return super.onSearchRequested();
	}
	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
		this.startActivity(new Intent(this,HomeActivity.class));
		this.finish();
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		
	}

}
