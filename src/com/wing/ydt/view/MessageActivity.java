package com.wing.ydt.view;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.guohead.sdk.GuoheAdLayout;
import com.wing.ydt.R;
import com.wing.ydt.db.DBAdapter;
import com.wing.ydt.vo.Category;
import com.wing.ydt.vo.Key_Mess;
import com.wing.ydt.vo.Message;
import com.wooboo.adlib_android.WoobooAdView;

public class MessageActivity extends MenuActivity implements OnClickListener {
	private static final String TAG="com.wing.wp.view.MessageActivity";
	private static Category category;
	private static Message message;
	private static ArrayList<Message> messList;
	private static ArrayList<Message> messTempList;
	private static ArrayList<Key_Mess> keyList;
	private static int numNowMess;
	private static int numMaxMess;
	private ScrollView  nameLayout;
	private ScrollView  bodyLayout;
	private LinearLayout keyLayout;
	private LinearLayout showLayout;
	private boolean isAddToFav=false;
	private Button showButton;
	private Button favoButton;
	private boolean isFavo=false;
	private Button prevButton;
	private Button nextButton;
	private TextView body;
	private TextView name ;
	private LinearLayout ad;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		ad = new LinearLayout(this);
        LayoutParams adparam =  new LayoutParams(screenWidth, (screenHeigh-80)/10);
        //(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT ); 
        ad.setLayoutParams(adparam); 
        
        RelativeLayout.LayoutParams GuoheAdLayoutParams = new RelativeLayout.LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        GuoheAdLayout adLayout = new GuoheAdLayout(this);
        ad.addView(adLayout, GuoheAdLayoutParams);
        ad.invalidate();
        		 
		bodyLayout  = new ScrollView(this);
		bodyLayout.setScrollContainer(true);   
		bodyLayout.setFocusable(true);   
		nameLayout  = new ScrollView(this);
		nameLayout.setScrollContainer(true);   
		nameLayout.setFocusable(true);
		
		keyLayout =  new LinearLayout(this);
        showLayout  = new LinearLayout(this);     
		
		name = new TextView(this);
		name.setTextSize(16);
		
        prevButton = new Button(this);
        prevButton.setBackgroundResource(R.drawable.bg);
        prevButton.setTextSize(13);
        prevButton.setOnClickListener(this);
        prevButton.setText(R.string.prev_message);
        
        showButton = new Button(this);
        showButton.setOnClickListener(this);
        showButton.setBackgroundResource(R.drawable.bg);
        showButton.setTextSize(13);
        showButton.setText(R.string.show_message);
        
        favoButton = new Button(this);
        favoButton.setTextSize(13);
        favoButton.setText(R.string.favo_message);
        favoButton.setOnClickListener(this);
        favoButton.setBackgroundResource(R.drawable.bg); 
        
        nextButton = new Button(this);
        nextButton.setTextSize(13);
        nextButton.setText(R.string.next_message);
        nextButton.setOnClickListener(this);
        nextButton.setBackgroundResource(R.drawable.bg);
              
        body = new TextView(this);
 	 	body.setTextSize(16);
        
        LayoutParams  showadparam =  new LayoutParams(screenWidth/4, (screenHeigh-80)/12);
        showLayout.addView(prevButton,showadparam);
        showLayout.addView(showButton,showadparam); 
        showLayout.addView(favoButton,showadparam);
        showLayout.addView(nextButton,showadparam);
 		            
		Bundle bundle = this.getIntent().getExtras();
		String from = bundle.getString("from");
		if(from.equals("ListMessActivity")){
			messList = bundle.getParcelableArrayList("MessageList");
			numNowMess =bundle.getInt("num");
		}
		if(messList.size()>0){
			message = messList.get(numNowMess);
			numMaxMess = messList.size();
			initKey_Mess();
			initView();
		}
		this.setContentView(this.globalLayout);
	}
	private void initView() {
		globalLayout.removeView(nameLayout);
		globalLayout.removeView(keyLayout);
		globalLayout.removeView(ad);
		globalLayout.removeView(showLayout);
		globalLayout.removeView(bodyLayout);
		
		nameLayout.removeView(name);
		name.setText(message.getMessage_name());		
        LayoutParams params = new LayoutParams(screenWidth, (screenHeigh-80)/10);
		nameLayout.addView(name, params);
		globalLayout.addView(nameLayout);
		
		keyLayout.removeAllViews();
         Button button;
         for(int i=0;i<keyList.size();i++){	 
        	 button = new Button(this);
        	 button.setTextSize(13);
        	 button.setText(keyList.get(i).getKeyword_name());
        	 button.setOnClickListener(this);
        	 button.setBackgroundResource(R.drawable.bg);
        	 button.setTextColor(Color.WHITE);
        	 keyLayout.addView(button,new LayoutParams(screenWidth/keyList.size(), (screenHeigh-80)/12));
         }
         globalLayout.addView(this.keyLayout);  
         
         globalLayout.addView(ad);
         
         if(numNowMess==0){
        	 prevButton.setTextColor(Color.GRAY);
         }else{
        	 prevButton.setTextColor(Color.WHITE);
         }              
         showButton.setTextColor(Color.WHITE);                   
         DBAdapter db = new DBAdapter(this);
         db.open();
 		 isAddToFav = db.isExistFavorite(message.getMessage_id());
 		 db.close();
 		if (isAddToFav) {
 			favoButton.setTextColor(Color.RED);
 		} else {
 			favoButton.setTextColor(Color.WHITE);
 		}                
         if(numNowMess+1>=numMaxMess){
        	nextButton.setTextColor(Color.GRAY);
		}else{
			nextButton.setTextColor(Color.WHITE);
		}  
        globalLayout.addView(this.showLayout);
         
        bodyLayout.removeView(body);
 		body.setText(message.getMessage_body());
 		body.setVisibility(View.INVISIBLE);
		LayoutParams param = new LayoutParams(screenWidth, (screenHeigh-80)*3/4);
		bodyLayout.addView(body, param);
		globalLayout.addView(this.bodyLayout);
	}

	private void initKey_Mess() {
		// TODO Auto-generated method stub
		DBAdapter db = new DBAdapter(this);
		db.open();
		keyList = db.getKey_Messes(message.getMessage_id());
		db.close();
	}
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == this.showButton){
			if(body.getVisibility()==View.VISIBLE){
				body.setVisibility(View.INVISIBLE);
				showButton.setTextColor(Color.WHITE);
			}else if(body.getVisibility()==View.INVISIBLE){
				body.setVisibility(View.VISIBLE);
				showButton.setTextColor(Color.GRAY);
			}
		}else if(v == this.prevButton){
			if(numNowMess==0){
				Toast.makeText(this,R.string.firstM, Toast.LENGTH_SHORT).show();
			}else{
				message = messList.get(--numNowMess);
				initKey_Mess();
				initView();
			}
		}else if(v == this.nextButton){
			if(numNowMess+1>=numMaxMess){
				Toast.makeText(this,R.string.lastM, Toast.LENGTH_SHORT).show();
			}else{
			message = messList.get(++numNowMess);
			initKey_Mess();
			initView();
			}
		}else if(v == this.favoButton){
			if(isAddToFav){
				DBAdapter db = new DBAdapter(this);
				db.open();
				db.deleteFavorite(message.getMessage_id());
				db.close();
				isAddToFav =false;
				favoButton.setTextColor(Color.WHITE);
			}else{
				DBAdapter db = new DBAdapter(this);
				db.open();
				db.saveFavorite(message);
				db.close();
				isAddToFav =true;
				favoButton.setTextColor(Color.RED);
			}
		}else if(v instanceof Button){
			Button button = (Button)v;			
			DBAdapter db = new DBAdapter(this);
			db.open();
			messTempList = db.QueryMessages(button.getText().toString().trim());
			db.close();
			Log.i(TAG, ":"+messTempList.size());
			if(messTempList==null){
				new AlertDialog.Builder(this).setMessage(R.string.no_message)
				.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int whichButton) {
								/* User clicked OK so do some stuff */
							}
					}).create().show();
			}else{
				Intent intent = new Intent();
				intent.setClass(MessageActivity.this,ListMessActivity.class);
				Bundle bundle = new Bundle();
				bundle.putParcelableArrayList("MessageList",messTempList);
				intent.putExtras(bundle);
				this.startActivity(intent);
			}	
		}
	}
	protected LinearLayout createSearchBar() {
		return new LinearLayout(this);
	}
	
}
