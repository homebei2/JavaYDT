package com.wing.ydt.handler;
import java.io.InputStream;
import com.wing.ydt.R;
import com.wing.ydt.db.DBAdapter;
import com.wing.ydt.util.WebServiceCall;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;

public class HandlerFactory {
	private NormalHander handler;
	private InputStream in;
	private Resources res;
	public static HandlerFactory instance = new HandlerFactory();

	private HandlerFactory() {
		
	}

	public void initDB(Context context,SQLiteDatabase db) {
		res = context.getResources();	
		in = res.openRawResource(R.raw.category);
		handler = new CategoryHandler();
		WebServiceCall.invokeWithTimeout(in,handler, 300);
		DBAdapter.saveCategories(handler.getList(),db);
		
		handler = new MessageHandler();
		in = res.openRawResource(R.raw.message_java);
		WebServiceCall.invokeWithTimeout(in,handler, 300);
		DBAdapter.saveMessages(handler.getList(),db);
		
		handler = new MessageHandler();
		in = res.openRawResource(R.raw.message_interview);
		WebServiceCall.invokeWithTimeout(in,handler, 300);
		DBAdapter.saveMessages(handler.getList(),db);
		
		handler = new MessageHandler();
		in = res.openRawResource(R.raw.message_sql);
		WebServiceCall.invokeWithTimeout(in,handler, 300);
		DBAdapter.saveMessages(handler.getList(),db);
		
		handler = new MessageHandler();
		in = res.openRawResource(R.raw.message_pattern);
		WebServiceCall.invokeWithTimeout(in,handler, 300);
		DBAdapter.saveMessages(handler.getList(),db);
		
		handler = new MessageHandler();
		in = res.openRawResource(R.raw.message_other);
		WebServiceCall.invokeWithTimeout(in,handler, 300);
		DBAdapter.saveMessages(handler.getList(),db);
		
		
		handler = new ListHandler();
		in = res.openRawResource(R.raw.wmessagelist);
		WebServiceCall.invokeWithTimeout(in,handler, 300);
		DBAdapter.saveWMessageList(handler.getList(),db);
		

		handler = new MessageHandler();
		in = res.openRawResource(R.raw.wmessage_1);
		WebServiceCall.invokeWithTimeout(in,handler, 300);
		DBAdapter.saveWMessages(handler.getList(),db);
		
		handler = new MessageHandler();
		in = res.openRawResource(R.raw.wmessage_2);
		WebServiceCall.invokeWithTimeout(in,handler, 300);
		DBAdapter.saveWMessages(handler.getList(),db);
		
		handler = new MessageHandler();
		in = res.openRawResource(R.raw.wmessage_3);
		WebServiceCall.invokeWithTimeout(in,handler, 300);
		DBAdapter.saveWMessages(handler.getList(),db);
	}
}
