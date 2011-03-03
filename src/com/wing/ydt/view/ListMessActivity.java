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
import com.wing.ydt.vo.Message;

public class ListMessActivity extends MenuActivity implements OnClickListener {
	private final static String TAG = "com.wing.wp.db.ListMessActivity";
	private ArrayList<Message> catList;
	private Message message;
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
		catList = bundle.getParcelableArrayList("MessageList");
		listLayout = new LinearLayout(this);
		listLayout.setOrientation(LinearLayout.VERTICAL);
		
		bodyLayout = new ScrollView(this);
		bodyLayout.setScrollContainer(true);   
		bodyLayout.setFocusable(true);  
		if (catList == null) {

		} else {
			Log.i(TAG, "MessageSize:" + catList.size());
			for (int i = 0; i < catList.size(); i++) {
				message = catList.get(i);
				ListMessageButton button = new ListMessageButton(this, message,
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
		if (v instanceof ListMessageButton) {
			ListMessageButton bu = (ListMessageButton) v;
			Message mess = bu.getMessage();
			int num = 0;
			for (int i = 0; i < catList.size(); i++) {
				if (mess.getMessage_id() == catList.get(i).getMessage_id()) {
					num = i;
					i = catList.size();
				}
			}
			Intent intent = new Intent();
			intent.setClass(ListMessActivity.this, MessageActivity.class);
			Bundle bundle = new Bundle();
			bundle.putParcelableArrayList("MessageList", catList);
			bundle.putInt("num", num);
			bundle.putString("from", "ListMessActivity");
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}
}
