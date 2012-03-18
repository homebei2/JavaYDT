package com.wing.ydt.view;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.guohead.sdk.GuoheAdLayout;
import com.wing.ydt.Application;
import com.wing.ydt.R;
import com.wing.ydt.db.DBAdapter;
import com.wing.ydt.handler.UIFactory;
import com.wing.ydt.util.ColourUtil;
import com.wing.ydt.vo.ListItem;
import com.wing.ydt.vo.Message;

public class MessageActivity extends BaseActivity {
	private ArrayList<ListItem> listModel;
	private Message item;
	private int total, num;
	private TextView message,topBanner;
	private DBAdapter db;
	private ImageButton favorite;
	private GestureDetector gestureDetector;
	private String[] highlight= new String[2];
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.item);
		item = (Message) this.getIntent().getExtras().get("item");
		listModel = this.getIntent().getExtras().getParcelableArrayList(
				"itemList");
		
		total = listModel.size();
		for (int i = 0; i < total; i++) {
			Message temp = (Message) listModel.get(i);
			if (temp.getMessage_id() == item.getMessage_id()) {
				num = i;
				break;
			}
		}
		
		message = (TextView) this.findViewById(R.id.message);
		message.setOnCreateContextMenuListener(this);
		
		favorite =(ImageButton) this.findViewById(R.id.favorite);
		favorite.setVisibility(View.VISIBLE);
		
		topBanner = (TextView) this.findViewById(R.id.title);
		db = DBAdapter.getInstance(this);
//		num_total = (TextView) this.findViewById(R.id.num_total);
//		num_total.setVisibility(View.VISIBLE);
		
		
		highlight[0]=this.getString(R.string.select);
		highlight[1]=this.getString(R.string.answer);
		
		initView(true);
		
		RelativeLayout.LayoutParams GuoheAdLayoutParams = new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		GuoheAdLayout adLayout = new GuoheAdLayout(this);
		((LinearLayout) this.findViewById(R.id.adLayout)).addView(adLayout,
				GuoheAdLayoutParams);

		gestureDetector = new GestureDetector(new GestureDetectorSimple());
	}

	private void initView(boolean bool) {
		// TODO Auto-generated method stub
		if (bool){
			if (item.getMessagelist_id() != 0) {
				message.setText(item.getMessage_name()+this.getString(R.string.select)
						+ item.getMessage_body());
				ColourUtil.INSTANCE.highlightText(message, highlight, R.color.answer_color);
				
				topBanner.setText(db.getWMessageListName(item.getMessagelist_id()));
			} else {
				message.setText(item.getMessage_name());
				topBanner.setText(db.QueryCategoryName(item.getCategory_type()));
			}
//			num_total.setText(num+"/"+total);
			UIFactory.instance.showMessageToast(String.format(getString(R.string.num_total),num+1,total),Toast.LENGTH_SHORT);
			
		}else {
			if (item.getMessagelist_id() != 0) {
				message.setText(item.getMessage_name()+this.getString(R.string.answer)
						+ item.getMessage_answer()+this.getString(R.string.select)
						+ item.getMessage_body());
				ColourUtil.INSTANCE.highlightText(message, highlight, R.color.answer_color);
			} else {
				message.setText(item.getMessage_name()+this.getString(R.string.answer)
						+ item.getMessage_body());
				ColourUtil.INSTANCE.highlightText(message, highlight, R.color.answer_color);
			}
		}
		if(item.isFavorite())
			favorite.setBackgroundResource(R.drawable.fav_button_selector);
		else
			favorite.setBackgroundResource(R.drawable.unfav_button_selector);
		favorite.invalidate();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private class GestureDetectorSimple extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			if (Math.abs(e2.getY() - e1.getY()) > Application.SWIPE_MIN_DISTANCE) {
				initView(false);
			}			
			if (e2.getX() - e1.getX() > Application.SWIPE_MIN_DISTANCE) {
					   backClick();
			} else if (e1.getX() - e2.getX() >  Application.SWIPE_MIN_DISTANCE) {
					  goClick();
			}
			return false;
		}	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (gestureDetector.onTouchEvent(event))
			return true;
		else
			return super.onTouchEvent(event);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		gestureDetector.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onContextItemSelected(MenuItem it) {
		// TODO Auto-generated method stub
		int menuItemId = it.getItemId();
		switch (menuItemId) {
			case R.id.edit:
				UIFactory.instance.startActivityForResult(this, item, false);
				return true;
			case R.id.delete:
				UIFactory.instance.displayDeleteConfirmationDialog(this,
						String.format(this.getString(R.string.Delete_Message), item.getMessage_name()),
						new Thread(){
						public void run() {
							handler.sendEmptyMessage(0);
							DBAdapter.getInstance().delListItem(item);
					}						
				});
				return true;
		}
		return super.onContextItemSelected(it);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		if(v!=null&&v== message){
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.context, menu);
		}
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		//第一次创建Menu会调用，一次
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.message, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem it) {
		// TODO Auto-generated method stub
		int id = it.getItemId();
		if(id==R.id.about){
			UIFactory.instance.displayAboutDialog(this);
			return true;
		}else if(id==R.id.back){
			backClick();
			return true;
		}else if(id==R.id.go){
			goClick();
			return true;
		}else if(id==R.id.answer){
			initView(false);
			return true;
		}else if(id==R.id.favo){
			this.startActivity(new Intent(Application.getContext(),FavQueActivity.class));
			return true;
		}else{
			return super.onOptionsItemSelected(it);
		}
	}
	private void goClick(){
		 if((num+1)>=total){
			   UIFactory.instance.showMessageToastOnUIThread(MessageActivity.this, R.string.Mlast, Toast.LENGTH_SHORT);
		  }else{
			  ++num;
				 item = (Message) listModel.get(num);
				 initView(true);
		  } 	 
	}
	private void backClick(){
		if(num==0){
			   UIFactory.instance.showMessageToastOnUIThread(MessageActivity.this, R.string.Mfirst, Toast.LENGTH_SHORT);
		   }else{
			   --num;
			   item = (Message) listModel.get(num);
			   initView(true);
		   }
	}
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		//每次Menu显示之前都会调用，多次
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode==Application.EDIT&&resultCode==RESULT_OK){
			item = data.getParcelableExtra("item");
			initView(true);
			listModel.set(num, item);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	Handler handler = new Handler(){

		@Override
		public void handleMessage(android.os.Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			listModel.remove(num);
			item = (Message) listModel.get(num);
			initView(true);
		}		
	};
	public void topBannerClick(View v) {
		if(v.getId()== R.id.back){
			this.finish();
		}else if(v.getId()== R.id.search){
			this.onSearchRequested();
		}else{
			DBAdapter db = DBAdapter.getInstance();
			if(item.isFavorite()){
				favorite.setBackgroundResource(R.drawable.unfav_button_selector);
				item.setFavorite(0);
				listModel.set(num, item);
				db.saveFavorite(item);
			}else{
				favorite.setBackgroundResource(R.drawable.fav_button_selector);
				item.setFavorite(1);
				listModel.set(num, item);
				db.saveFavorite(item);
			}	
			topBanner.setFocusable(true);
			topBanner.setFocusableInTouchMode(true);
			topBanner.requestFocus();

		}
	}
}
