package com.wing.ydt.view;
import java.util.ArrayList;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;

import com.wing.ydt.Application;
import com.wing.ydt.R;
import com.wing.ydt.db.DBAdapter;
import com.wing.ydt.view.adapter.CategoryListAdapter;
import com.wing.ydt.vo.Category;
import com.wing.ydt.vo.ListItem;
import com.wing.ydt.vo.WMessageList;

public class FavQueActivity extends BaseListActivity implements OnFocusChangeListener{
	private ArrayList<ListItem> listFaceModel,listUnFaceModel;
	private Button face,unface;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	protected int getLayoutID() {
		// TODO Auto-generated method stub
		return R.layout.favque_list_view;
	}
	protected void initView(Intent intent) {
		super.initView(intent);
		// TODO Auto-generated method stub
	    adapter = new CategoryListAdapter(this);
		setListAdapter(adapter);	
		
		
		face = (Button) findViewById(R.id.face);
		face.requestFocus();
		face.setOnFocusChangeListener(this);
		
		unface = (Button) findViewById(R.id.unface);
		unface.setOnFocusChangeListener(this);
	}
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		//每次Menu显示之前都会调用，多次
		menu.findItem(R.id.add).setEnabled(false);
		menu.findItem(R.id.add).setVisible(false);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	    DBAdapter db = DBAdapter.getInstance(this);
		
		if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {		      
			listUnFaceModel = db.QueryMessages(getIntent().getStringExtra(SearchManager.QUERY), 1);
			listFaceModel = db.QueryMessages(getIntent().getStringExtra(SearchManager.QUERY), 0);
			
			topBanner.setText(R.string.search_title);
		}else{
			listUnFaceModel = db.getFavorites(0, 1);
			listFaceModel = db.getFavorites(0, 0);
			topBanner.setText(db.QueryCategoryName(Application.FAVORITE));
		}
		if(face.hasFocus())
			adapter.setItems(listFaceModel);
		else if(unface.hasFocus())
			adapter.setItems(listUnFaceModel);
	}
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		this.setIntent(intent);
//		if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {	
//			DBAdapter db = DBAdapter.getInstance(this);
//			listUnFaceModel = db.QueryMessages(getIntent().getStringExtra(SearchManager.QUERY), 1);
//			listFaceModel = db.QueryMessages(getIntent().getStringExtra(SearchManager.QUERY), 0);
//		}
	}
	public void face(View view){
			 face.requestFocus();
			 adapter.setItems(listFaceModel);
			 this.getListView().invalidate(); 
	}
	public void unface(View view){
			 unface.requestFocus();
			 adapter.setItems(listUnFaceModel);
			 this.getListView().invalidate();
		 
	}
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		if(v ==face&&hasFocus){
			 face.requestFocus();
			 adapter.setItems(listFaceModel);
			 this.getListView().invalidate(); 
		}else if(v ==unface&&hasFocus){
			 unface.requestFocus();
			 adapter.setItems(listUnFaceModel);
			 this.getListView().invalidate();
		}
	}
}
