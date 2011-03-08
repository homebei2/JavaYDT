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
import com.wing.ydt.view.component.CommonImageButton;
import com.wing.ydt.vo.Category;
import com.wing.ydt.vo.Key_Mess;
import com.wing.ydt.vo.Message;
import com.wing.ydt.vo.WMessage;
import com.wooboo.adlib_android.WoobooAdView;

public class WMessageActivity extends MenuActivity implements OnClickListener {
	private static final String TAG="com.wing.wp.view.WMessageActivity";
	private static WMessage message;
	private static ArrayList<WMessage> messList;
	private static int numNowMess=0;
	private static int numMaxMess=0;
	private ScrollView  nameLayout;
	private ScrollView  bodyLayout;
	private ScrollView selectLayout;
	private LinearLayout showLayout;
	private Button showButton;
	private Button prevButton;
	private Button nextButton;
	private TextView answer;
	private TextView select;
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
        
        
        selectLayout  = new ScrollView(this);
        selectLayout.setScrollContainer(true);   
        selectLayout.setFocusable(true); 
		bodyLayout  = new ScrollView(this);
		bodyLayout.setScrollContainer(true);   
		bodyLayout.setFocusable(true);   
		nameLayout  = new ScrollView(this);
		nameLayout.setScrollContainer(true);   
		nameLayout.setFocusable(true);
		
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
        
        
        nextButton = new Button(this);
        nextButton.setTextSize(13);
        nextButton.setText(R.string.next_message);
        nextButton.setOnClickListener(this);
        nextButton.setBackgroundResource(R.drawable.bg);
          
        select  = new TextView(this);
        select.setTextSize(16);
        
        
        answer = new TextView(this);
        answer.setTextSize(20);
        answer.setTextColor(Color.YELLOW);
        
        LayoutParams  showadparam =  new LayoutParams(screenWidth/3, (screenHeigh-80)/12);
        showLayout.addView(prevButton,showadparam);
        showLayout.addView(showButton,showadparam); 
        showLayout.addView(nextButton,showadparam);
        
		Bundle bundle = this.getIntent().getExtras();
		String from = bundle.getString("from");
		if(from.equals("ListWMessListActivity")){
			messList = bundle.getParcelableArrayList("WMessages");
			numNowMess =bundle.getInt("num");
			numMaxMess = messList.size();
		}
		if(messList.size()>0){
			message = messList.get(numNowMess);
			initView();
		}
		initView();
		this.setContentView(this.globalLayout);
	}
	private void initView() {
		globalLayout.removeView(nameLayout);
		globalLayout.removeView(selectLayout);
		globalLayout.removeView(ad);
		globalLayout.removeView(showLayout);
		globalLayout.removeView(bodyLayout);
		
		nameLayout.removeView(name);
		name.setText(message.getWmessage_name());		
        LayoutParams params = new LayoutParams(screenWidth, (screenHeigh-80)/10);
		nameLayout.addView(name, params);
		globalLayout.addView(nameLayout);
		
	
         
         globalLayout.addView(ad);
         
         if(numNowMess==0){
        	 prevButton.setTextColor(Color.GRAY);
         }else{
        	 prevButton.setTextColor(Color.WHITE);
         }              
         showButton.setTextColor(Color.WHITE);                   
         
         if(numNowMess+1>=numMaxMess){
        	nextButton.setTextColor(Color.GRAY);
		}else{
			nextButton.setTextColor(Color.WHITE);
		}  
        globalLayout.addView(this.showLayout);
         
        bodyLayout.removeView(answer);
        answer.setText(this.getResources().getString(R.string.answer)+message.getWmessage_answer());
        answer.setVisibility(View.INVISIBLE);
		LayoutParams param = new LayoutParams(screenWidth, (screenHeigh-80)*1/4);
		bodyLayout.addView(answer, param);
		globalLayout.addView(this.bodyLayout);
        
        selectLayout.removeView(select);
        select.setText(message.getWmessage_body());
		LayoutParams sparam = new LayoutParams(screenWidth, (screenHeigh-80)*2/4);
		selectLayout.addView(select, sparam);
		globalLayout.addView(this.selectLayout);
           
	}
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == this.showButton){
			if(answer.getVisibility()==View.VISIBLE){
				answer.setVisibility(View.INVISIBLE);
				showButton.setTextColor(Color.WHITE);
			}else if(answer.getVisibility()==View.INVISIBLE){
				answer.setVisibility(View.VISIBLE);
				showButton.setTextColor(Color.GRAY);
			}
		}else if(v == this.prevButton){
			if(numNowMess==0){
				Toast.makeText(this,R.string.firstM, Toast.LENGTH_SHORT).show();
			}else{
				message = messList.get(--numNowMess);
				initView();
			}
		}else if(v == this.nextButton){
			if(numNowMess+1>=numMaxMess){
				Toast.makeText(this,R.string.lastM, Toast.LENGTH_SHORT).show();
			}else{
			message = messList.get(++numNowMess);
			initView();
			}
		}
	}
	protected LinearLayout createSearchBar() {
		return new LinearLayout(this);
	}
	
}
