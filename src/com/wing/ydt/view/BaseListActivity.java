package com.wing.ydt.view;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.wing.ydt.Application;
import com.wing.ydt.R;
import com.wing.ydt.db.DBAdapter;
import com.wing.ydt.handler.UIFactory;
import com.wing.ydt.view.adapter.BaseListAdapter;
import com.wing.ydt.vo.Category;
import com.wing.ydt.vo.ListItem;
import com.wing.ydt.vo.Message;
import com.wing.ydt.vo.WMessageList;

public class BaseListActivity extends ListActivity implements OnClickListener{
	protected TextView topBanner;
	protected BaseListAdapter adapter;
	protected ListItem item;
	private ListItem contextItem;
	protected int type;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(getLayoutID());
		initView(this.getIntent());
		topBanner = (TextView) this.findViewById(R.id.title);
		
		 if(getIntent().getExtras()!=null)
		    	item = this.getIntent().getExtras().getParcelable("item");
		 
			quitReceiver = new QuitIntentReceiver();
			IntentFilter filter = new IntentFilter(QUIT);
			registerReceiver(quitReceiver, filter);
	}

	protected int getLayoutID() {
		// TODO Auto-generated method stub
		return R.layout.list_view;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		//第一次创建Menu会调用，一次
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.listview, menu);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		//每次Menu显示之前都会调用，多次
		return super.onPrepareOptionsMenu(menu);
	}
	@Override
	public boolean onContextItemSelected(MenuItem it) {
		// TODO Auto-generated method stub
		int menuItemId = it.getItemId();
		switch (menuItemId) {
			case R.id.edit:
				UIFactory.instance.startActivityForResult(this, contextItem, false);
				return true;
			case R.id.delete:
				if(type==Application.CATEGORY&&((Category)contextItem).getCategory_type()<=7){
					UIFactory.instance.showMessageToastOnUIThread(this, R.string.Cannot_Delete, Toast.LENGTH_SHORT);
				}else{
					String message = null;
					if(type==Application.MESSAGE||type==Application.WMESSAGE){
						message = ((Message) contextItem).getMessage_name();			
					}else if(type==Application.CATEGORY){
						message =((Category)contextItem).getCategory_name();
					}else if(type==Application.WMESSAGELIST){
						message =((WMessageList)contextItem).getWmessagelist_name();
					}else{
						if(contextItem instanceof Message)
							message = ((Message) contextItem).getMessage_name();
					}
					UIFactory.instance.displayDeleteConfirmationDialog(this,
							String.format(this.getString(R.string.Delete_Message), message),
							new Thread(){
							public void run() {
								handler.sendEmptyMessage(0);
								DBAdapter.getInstance().delListItem(contextItem);
						}						
					});
				}
				return true;
		}
		return super.onContextItemSelected(it);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		if(v.getTag() instanceof ListItem){
			contextItem = (ListItem) v.getTag();
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.context, menu);
		}
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	protected void initView(Intent intent){
//		getListView().setOnCreateContextMenuListener(this); 如此注册获取不到具体哪个View产生的ContextItem
//		getListView().setOnItemClickListener(this); 如果在Item中注册OnCreateContextMenuListener,则getListView().setOnItemClickListener(this);此方法将不可用，获取不到Click事件了，改用在item中注册OnClickListener事件。
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode==Application.EDIT&&resultCode==RESULT_OK){
			contextItem = data.getParcelableExtra("item");
			((BaseListAdapter) this.getListAdapter()).changeItem(contextItem);
		}else if(requestCode==Application.ADD&&resultCode==RESULT_OK){
			contextItem = data.getParcelableExtra("item");
			((BaseListAdapter) this.getListAdapter()).add(contextItem);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	public void topBannerClick(View v) {
		if(v.getId()== R.id.back){
			this.finish();
		}else if(v.getId()== R.id.search){
			this.onSearchRequested();
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		ListItem item  = (ListItem) v.getTag();
		Intent intent;
		Bundle bundle = new Bundle();
		if(item instanceof WMessageList){
			intent=new Intent(Application.getContext(),HomeActivity.class);
		}else if(item instanceof Category){
			if(((Category)item).getCategory_type()==7)
				intent=new Intent(Application.getContext(),FavQueActivity.class);
			else
				intent=new Intent(Application.getContext(),HomeActivity.class);
		}else{
			intent=new Intent(Application.getContext(),MessageActivity.class);
			bundle.putParcelableArrayList("itemList", (ArrayList<? extends Parcelable>)adapter.getItems());
		}
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		bundle.putParcelable("item",item);
		intent.putExtras(bundle);
		Application.getContext().startActivity(intent);
	}
	Handler handler = new Handler(){

		@Override
		public void handleMessage(android.os.Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			adapter.removeItem(contextItem);
		}		
	};
	private Handler quithandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			BaseListActivity.this.finish();
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
	private final static String QUIT = "com.wing.app.quit";
	private QuitIntentReceiver quitReceiver;
	
}
