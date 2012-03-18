package com.wing.ydt.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.adview.AdViewInterface;
import com.adview.AdViewLayout;
import com.adview.AdViewTargeting;
import com.adview.AdViewTargeting.RunMode;
import com.adview.AdViewTargeting.UpdateMode;
import com.wing.ydt.R;
import com.wing.ydt.db.DBAdapter;
import com.wing.ydt.vo.Category;
import com.wing.ydt.vo.ListItem;
import com.wing.ydt.vo.Message;
import com.wing.ydt.vo.WMessageList;

public class EditActivity extends Activity implements AdViewInterface {
	private Message message;
	private Category category;
	private WMessageList wmlist;
	private ListItem item;
	private TextView topBanner;
	private EditText m_name, m_answer, m_body;
	private boolean isAdd = true;
	private int id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.edit_item);

		m_name = (EditText) findViewById(R.id.m_name);
		m_answer = (EditText) findViewById(R.id.m_answer);
		m_body = (EditText) findViewById(R.id.m_body);
		topBanner = (TextView) this.findViewById(R.id.title);
		topBanner.setFocusable(true);
		topBanner.setFocusableInTouchMode(true);
		topBanner.requestFocus();
		
		isAdd = this.getIntent().getExtras().getBoolean("isAdd");
		item = (ListItem) this.getIntent().getExtras().get("item");
		if (item instanceof Message) {
			message = (Message) item;
			m_name.setText(message.getMessage_name());
			m_body.setText(message.getMessage_body());
			m_answer.setText(message.getMessage_answer());

			DBAdapter db = DBAdapter.getInstance(this);
			if (message.getMessagelist_id() > 0) {
				m_answer.setVisibility(View.VISIBLE);
				topBanner.setText(db.getWMessageListName(message.getMessagelist_id()));
			} else{
				topBanner.setText(db.QueryCategoryName(message.getCategory_type()));
			}
		} else if (item instanceof Category) {
			category = (Category) this.getIntent().getExtras().get("item");
			m_name.setText(category.getCategory_name());
			m_body.setText(category.getCategory_desc());
			topBanner.setText(getString(R.string.app_name));
		} else {
			DBAdapter db = DBAdapter.getInstance(this);
			wmlist = (WMessageList) this.getIntent().getExtras().get("item");
			m_name.setText(wmlist.getWmessagelist_name());
			m_body.setText(wmlist.getWmessagelist_desc());
			topBanner.setText(db.QueryCategoryName(wmlist.getCategory_type()));
		}
		setResult(RESULT_CANCELED);

		
		  /*下面两行只用于测试,完成后一定要去掉,参考文挡说明*/
//        AdViewTargeting.setUpdateMode(UpdateMode.EVERYTIME);  //每次都从服务器取配置
//        AdViewTargeting.setRunMode(RunMode.TEST);        //保证所有选中的广告公司都为测试状态
        /*下面这句方便开发者进行发布渠道统计,详细调用可以参考java doc  */
        //AdViewTargeting.setChannel(Channel.GOOGLEMARKET);
        AdViewLayout adViewLayout = (AdViewLayout)findViewById(R.id.adview_ayout);
        adViewLayout.setAdViewInterface(this);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		// 第一次创建Menu会调用，一次
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.edit, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int id = item.getItemId();
		if (id == R.id.save) {
			save(topBanner);
			return true;
		} else if (id == R.id.cancel) {
			cancel(topBanner);
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		// 每次Menu显示之前都会调用，多次
		return super.onPrepareOptionsMenu(menu);
	}

	public void save(View view) {
		DBAdapter db = DBAdapter.getInstance(this);
		if (item instanceof Message) {
			message.setMessage_name(m_name.getText().toString());
			message.setMessage_body(m_body.getText().toString());
			if (message.getMessagelist_id() > 0) {
				message.setMessage_answer(m_answer.getText().toString());
				id = (int) db.saveWMessage(message, isAdd);
			} else {
				id = (int) db.saveMessage(message, isAdd);
			}
			if (id > 0)
				message.setMessage_id(id);
			item = message;
		} else if (item instanceof Category) {
			category.setCategory_name(m_name.getText().toString());
			category.setCategory_desc(m_body.getText().toString());
			id = (int) db.saveCategory(category, isAdd);
			if (id > 0)
				category.setCategory_type(id);
			item = category;
		} else {
			wmlist.setWmessagelist_name(m_name.getText().toString());
			wmlist.setWmessagelist_desc(m_body.getText().toString());
			id = (int) db.saveWMessageList(wmlist, isAdd);
			if (id > 0)
				wmlist.setWmessagelist_id(id);
			item = wmlist;
		}
		setResult(RESULT_OK, new Intent().putExtra("item", item));
		finish();
	}

	public void cancel(View view) {
		this.finish();
	}
	public void topBannerClick(View v) {
		if(v.getId()== R.id.back){
			this.finish();
		}else if(v.getId()== R.id.search){
			onSearchRequested();
		}
	}

	@Override
	public void onClickAd() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisplayAd() {
		// TODO Auto-generated method stub
		
	}
}
