package com.wing.ydt.view;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.wing.ydt.Application;
import com.wing.ydt.R;
import com.wing.ydt.db.DBAdapter;
import com.wing.ydt.handler.UIFactory;
import com.wing.ydt.view.adapter.CategoryListAdapter;
import com.wing.ydt.vo.Category;
import com.wing.ydt.vo.ListItem;
import com.wing.ydt.vo.Message;
import com.wing.ydt.vo.WMessageList;

public class HomeActivity extends BaseListActivity{
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	protected void initView(Intent intent) {
		super.initView(intent);
		// TODO Auto-generated method stub
	    adapter = new CategoryListAdapter(this);
		setListAdapter(adapter);
		
		
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem it) {
		// TODO Auto-generated method stub
		int id = it.getItemId();
		ListItem tempItem;
		if(id==R.id.add){
			if(type==Application.MESSAGE){
				Message e = new Message();
				e.setCategory_type(((Category) item).getCategory_type());
				tempItem = e;
			}else if(type==Application.WMESSAGE){
				Message e = new Message();
				e.setCategory_type(5);
				e.setMessagelist_id(((WMessageList) item).getWmessagelist_id());			
				tempItem = e;
			}else if(type==Application.CATEGORY){
				Category ca = new Category();
				tempItem = ca;
			}else{
				WMessageList wm = new WMessageList();
				wm.setCategory_type(5);
				tempItem = wm;
			}
			UIFactory.instance.startActivityForResult(this, tempItem, true);
			return true;
		}else
				return super.onOptionsItemSelected(it);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	    DBAdapter db = DBAdapter.getInstance(this);
	    if(item==null){
	    	adapter.setItems(db.QueryCategories());
	    	type = Application.CATEGORY;
	    }else if(item instanceof Category){
			 adapter.setItems(db.getMessages(((Category) item).getCategory_type(), 0));
			 if(((Category) item).getCategory_type()==5)
				 type = Application.WMESSAGELIST;
			 else
				 type = Application.MESSAGE;
			 
			 topBanner.setText(((Category) item).getCategory_name());
		}else if(item instanceof WMessageList){
		  	type = Application.WMESSAGE;
			adapter.setItems(db.getMessages(((WMessageList) item).getCategory_type(),((WMessageList) item).getWmessagelist_id(), 0));
			
			topBanner.setText(((WMessageList) item).getWmessagelist_name());
		}
	}
}
