package com.wing.ydt.view;

import java.util.ArrayList;

import android.R.color;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ScrollView;

import com.wing.ydt.db.DBAdapter;
import com.wing.ydt.view.component.ListMessageButton;
import com.wing.ydt.view.component.ListWMessageListButton;
import com.wing.ydt.vo.Message;
import com.wing.ydt.vo.WMessage;
import com.wing.ydt.vo.WMessageList;

public class ListWMessListActivity extends MenuActivity implements OnClickListener {
	private final static String TAG = "com.wing.wp.db.ListWMessListActivity";
	private ArrayList<WMessageList> catList;
	private WMessageList message;
	private LinearLayout listLayout;
	private ScrollView bodyLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		initView();
		this.setContentView(globalLayout);
	}

	private void initView() {
		// TODO Auto-generated method stub
		Bundle bundle = this.getIntent().getExtras();
		catList = bundle.getParcelableArrayList("WMessageList");
		listLayout = new LinearLayout(this);
		listLayout.setOrientation(LinearLayout.VERTICAL);
		
		bodyLayout = new ScrollView(this);
		bodyLayout.setScrollContainer(true);   
		bodyLayout.setFocusable(true);  
		if (catList == null) {

		} else {
			Log.i(TAG, "WMessageListSize:" + catList.size());
			for (int i = 0; i < catList.size(); i++) {
				message = catList.get(i);
				ListWMessageListButton button = new ListWMessageListButton(this, message,
						i);
				button.setOnClickListener(this);
				listLayout.addView(button);
			}
			bodyLayout.addView(listLayout);
			globalLayout.addView(bodyLayout);
		}
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		if (v instanceof ListWMessageListButton) {
			ListWMessageListButton bu = (ListWMessageListButton) v;
			WMessageList mess = bu.getMessage();
			ArrayList<WMessage> WmessList;
			DBAdapter db = new DBAdapter(this);
			db.open();
			WmessList = db.getWMessages(mess.getWmessagelist_id());
			db.close();
			Log.i(TAG, "getWMessages:"+WmessList.size());
			Intent intent = new Intent();
			intent.setClass(ListWMessListActivity.this, WMessageActivity.class);
			Bundle bundle = new Bundle();
			bundle.putParcelableArrayList("WMessages", WmessList);
			bundle.putInt("num", 0);
			bundle.putString("from", "ListWMessListActivity");
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}
}
