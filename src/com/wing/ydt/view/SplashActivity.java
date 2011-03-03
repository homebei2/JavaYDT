package com.wing.ydt.view;

import java.util.ArrayList;

import android.R.color;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsoluteLayout;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.wing.ydt.R;
import com.wing.ydt.db.DBAdapter;
import com.wing.ydt.view.component.CommonImageButton;
import com.wing.ydt.view.component.ListCategoryButton;
import com.wing.ydt.vo.Category;
import com.wing.ydt.vo.Message;
import com.wing.ydt.vo.WMessageList;

public class SplashActivity extends MenuActivity implements OnClickListener {
	private final static String TAG = "com.wing.wp.db.SplashActivity";
	private ArrayList<Category> catList;
	private Category category;
	private LinearLayout listLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		this.setContentView(globalLayout);
	}

	private void initView() {
		// TODO Auto-generated method stub
		listLayout = new LinearLayout(this);
		listLayout.setOrientation(LinearLayout.VERTICAL);
		DBAdapter db = new DBAdapter(this);
		db.open();
		catList = db.QueryAllCategories();
		db.close();
		if (catList != null) {
			for (int i = 0; i < catList.size(); i++) {
				category = catList.get(i);
				ListCategoryButton button = new ListCategoryButton(this,
						category, i);
				button.setOnClickListener(this);
				listLayout.addView(button);
			}
			globalLayout.addView(listLayout);
		}
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		if (v instanceof ListCategoryButton) {
			ListCategoryButton catB = (ListCategoryButton) v;
			category = catB.getCategory();
			ArrayList<Message> messList;
			if (category.getCategory_type() == 5) {
				ArrayList<WMessageList> WmessList;
				DBAdapter db = new DBAdapter(this);
				db.open();
				WmessList = db.getWMessageList();
				db.close();
				if (WmessList != null && WmessList.size() > 0) {
					Intent intent = new Intent();
					intent
							.setClass(SplashActivity.this,
									ListWMessListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putParcelableArrayList("WMessageList", WmessList);
					intent.putExtras(bundle);
					startActivity(intent);
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
			} else {
				if (category.getCategory_type() == 7) {
					DBAdapter db = new DBAdapter(this);
					db.open();
					messList = db.getFavorites(1);
					db.close();
				} else {
					DBAdapter db = new DBAdapter(this);
					db.open();
					messList = db.getMessages(category.getCategory_type(), 1);
					db.close();
				}
				if (messList != null && messList.size() > 0) {
					Intent intent = new Intent();
					intent
							.setClass(SplashActivity.this,
									ListMessActivity.class);
					Bundle bundle = new Bundle();
					bundle.putParcelableArrayList("MessageList", messList);
					intent.putExtras(bundle);
					startActivity(intent);
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
