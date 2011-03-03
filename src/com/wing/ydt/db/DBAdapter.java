package com.wing.ydt.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.wing.ydt.R;
import com.wing.ydt.vo.Category;
import com.wing.ydt.vo.Key_Mess;
import com.wing.ydt.vo.Keyword;
import com.wing.ydt.vo.Message;
import com.wing.ydt.vo.WMessage;
import com.wing.ydt.vo.WMessageList;

public class DBAdapter {

	private static final String DATABASE_NAME_OLD = "wp.db";
	private static final String DATABASE_NAME = "wpp.db";
	private static final String DATABASE_PATH ="/data/data/com.wing.ydt/databases";
	private static final String Category_TABLE = "category";
	private static final String FAVORITE_TABLE="favorite";
	private static final String Message_TABLE="message";
	private static final String Key_Mess_TABLE="key_mess";
	private static int Version = 2;
	private static final String Category_CREATE = "create table "+Category_TABLE+"(category_type INTEGER PRIMARY KEY,category_name TEXT,category_desc TEXT);";
	private static final String FAVORITE_CREATE = "create table  "+FAVORITE_TABLE+"(message_id INTEGER PRIMARY KEY,category_type INTEGER NOT NULL,message_name TEXT,message_body TEXT,message_desc text);";
	private static final String Message_CREATE = "create table  "+Message_TABLE+"(message_id INTEGER PRIMARY KEY,category_type INTEGER NOT NULL,message_name TEXT,message_body TEXT,message_desc text);";
	private static final String Key_Mess_CREATE = "create table "+Key_Mess_TABLE+"(message_id INTEGER NOT NULL,keyword_type INTEGER,keyword_name TEXT ,key_mess_desc TEXT);";
	
	private static final String W_Message_TABLE="wmessage";
	private static final String W_Message_LIST="wmessagelist";
	private static final String W_Message_CREATE = "create table  "+W_Message_TABLE+"(wmessage_id INTEGER PRIMARY KEY,wmessagelist_id INTEGER NOT NULL,wmessage_name TEXT,wmessage_body TEXT,wmessage_answer text);";
	private static final String W_Message_LIST_CREATE = "create table  "+W_Message_LIST+"(wmessagelist_id INTEGER PRIMARY KEY,category_type INTEGER NOT NULL,wmessagelist_name TEXT);";
	
	private SQLiteDatabase db;
	private DatabaseHelper dbHelper;
	private Context context;

	public DBAdapter(Context ctx) {
		this.context = ctx;
		dbHelper = new DatabaseHelper(context);
	}

	private class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, Version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// create table
			Log.i("onCreateTable",":");
			db.execSQL(Category_CREATE);
			db.execSQL(FAVORITE_CREATE);
			db.execSQL(Message_CREATE);
			db.execSQL(Key_Mess_CREATE);
			db.execSQL(W_Message_LIST_CREATE);
			db.execSQL(W_Message_CREATE);	
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("drop table if exists "+Category_TABLE);	
			db.execSQL("drop table if exists "+FAVORITE_TABLE);	
			db.execSQL("drop table if exists "+Message_TABLE);	
			db.execSQL("drop table if exists "+Key_Mess_TABLE);	
			db.execSQL("drop table if exists "+W_Message_TABLE);	
			db.execSQL("drop table if exists "+W_Message_LIST);	
			onCreate(db);
		}

	}

	public DBAdapter open() {
		//db = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
		try
		{
			// 获得dictionary.db文件的绝对路径
			String databaseFilename = DATABASE_PATH + "/" + DATABASE_NAME;
			File dir = new File(DATABASE_PATH);
			// 如果/sdcard/dictionary目录中存在，创建这个目录
			if (!dir.exists())
				dir.mkdir();
			// 如果在/sdcard/dictionary目录中不存在
			// dictionary.db文件，则从res\raw目录中复制这个文件到
			// SD卡的目录（/sdcard/dictionary）
			File file = new File( DATABASE_PATH + "/" + DATABASE_NAME_OLD);
			if(file.exists()){
				Log.i(databaseFilename,"delete db");
				file.delete();
			}
			if (!(new File(databaseFilename)).exists())
			{	
				Log.i(databaseFilename,"copy db");
				// 获得封装dictionary.db文件的InputStream对象
				InputStream is = context.getResources().openRawResource(
						R.raw.wpp);
				FileOutputStream fos = new FileOutputStream(databaseFilename);
				byte[] buffer = new byte[8192];
				int count = 0;
				// 开始复制dictionary.db文件
				while ((count = is.read(buffer)) > 0)
				{
					fos.write(buffer, 0, count);
				}

				fos.close();
				is.close();
			}
			// 打开/sdcard/dictionary目录中的dictionary.db文件
				db = SQLiteDatabase.openOrCreateDatabase(
					databaseFilename, null);
			return this;
		}
		catch (Exception e)
		{
		}
		return null;
	}

	public void close() {
		try {
			dbHelper.close();
		} catch (Exception e) {

		}

	}

	public void saveCategories(ArrayList<Category> allCategories) {
		// TODO Auto-generated method stub
		ContentValues cv;
		Category category;
		for(int i=0;i<allCategories.size();i++){
			cv = new ContentValues();
			category = allCategories.get(i);
			cv.put("category_type", category.getCategory_type());
			cv.put("category_name", category.getCategory_name());
			cv.put("category_desc", category.getCategory_desc());
			db.insert(Category_TABLE, null, cv);
		}
		db.close();
	}
	public boolean isExistFavorite(int messageId) {
		// TODO Auto-generated method stub
		String sql = "select count(*) num from favorite where message_id ="+messageId;
		//Log.i("isExistFavorite",":"+sql+":"+db);
		boolean isExist=false;
		Cursor cur = db.rawQuery(sql, null);
		if (cur != null && cur.moveToFirst()&& cur.getInt(cur.getColumnIndex("num"))>0) {
			isExist = true;
		}else{
			isExist = false;
		}
		cur.close();
		db.close();
		return isExist;
	}
	public void deleteFavorite(int message_id) {
		//Log.i("deleteFavorite",":"+message_id);
		String sql = "delete from favorite where message_id="+message_id;
		db.execSQL(sql);
		db.close();
	}
	public void saveFavorite(Message category) {
		// TODO Auto-generated method stub
			//Log.i("saveFavorite",":"+category.getMessage_id());
			ContentValues cv;
			cv = new ContentValues();
			cv.put("message_id", category.getMessage_id());
			cv.put("message_name", category.getMessage_name());
			cv.put("message_body", category.getMessage_body());
			cv.put("message_desc", category.getMessage_desc());
			cv.put("category_type", 6);
			db.insert(FAVORITE_TABLE, null, cv);
			db.close();
	}

	public ArrayList<Message> getFavorites(int i) {
		String sql = "select * from favorite ";
		//Log.i("getFavorites",":"+sql+":"+db);
		ArrayList<Message> allMessages = new ArrayList<Message>();
		Message mes;
		Cursor cur = db.rawQuery(sql, null);
		if(cur==null) return allMessages;
		cur.moveToFirst();
		while(!cur.isAfterLast()){
			mes = new Message();
			mes.setCategory_type(cur.getInt(cur.getColumnIndex("category_type")));
			mes.setMessage_id(cur.getInt(cur.getColumnIndex("message_id")));
			mes.setMessage_name(cur.getString(cur.getColumnIndex("message_name")));
			mes.setMessage_body(cur.getString(cur.getColumnIndex("message_body")));
			mes.setMessage_desc(cur.getString(cur.getColumnIndex("message_desc")));
			allMessages.add(mes);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return allMessages;
	}
	
	public void saveMessages(ArrayList<Message> allMessages) {
		// TODO Auto-generated method stub
		ContentValues cv;
		Message category;
		for(int i=0;i<allMessages.size();i++){
			cv = new ContentValues();
			category = allMessages.get(i);
			cv.put("message_id", category.getMessage_id());
			cv.put("message_name", category.getMessage_name());
			cv.put("message_body", category.getMessage_body());
			cv.put("message_desc", category.getMessage_desc());
			cv.put("category_type", category.getCategory_type());
			db.insert(Message_TABLE, null, cv);
		}
		db.close();
	}

	public int getMaxMessageId() {
		// TODO Auto-generated method stub
		int i=0;
		Cursor cur =db.rawQuery("select max(message_id) mess from "+Message_TABLE,null);
		if (cur != null && cur.moveToFirst()) {
			i= cur.getInt(cur.getColumnIndex("mess"));
		}
		cur.close();
		db.close();
		return i;
	}

	public void saveKey_Messes(ArrayList<Key_Mess> allKeyMess) {
		// TODO Auto-generated method stub
		ContentValues cv;
		Key_Mess category;
		for(int i=0;i<allKeyMess.size();i++){
			cv = new ContentValues();
			category = allKeyMess.get(i);
			cv.put("message_id", category.getMessage_id());
			cv.put("key_mess_desc", category.getKey_mess_desc());
			cv.put("keyword_name", category.getKeyword_name());
			cv.put("keyword_type", category.getKeyword_type());
			db.insert(Key_Mess_TABLE, null, cv);
		}
		db.close();
	}

	public ArrayList<Category> QueryAllCategories() {
		// TODO Auto-generated method stub
		String sql = "select * from category order by category_type asc";
		ArrayList<Category> allCategories = new ArrayList<Category>();
		Category cat;
		Cursor cur = db.rawQuery(sql, null);
		if(cur==null) return null;
		cur.moveToFirst();
		while(!cur.isAfterLast()){
			cat = new Category();
			cat.setCategory_type(cur.getInt(cur.getColumnIndex("category_type")));
			cat.setCategory_name(cur.getString(cur.getColumnIndex("category_name")));
			cat.setCategory_desc(cur.getString(cur.getColumnIndex("category_desc")));
			allCategories.add(cat);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return allCategories;
	}
	public ArrayList<Message> QueryMessages(String search) {
		// TODO Auto-generated method stub
		//从关键字中搜索，按照message_id排序
		String sql = "select * from message where message_id  in " +
				" (select distinct(message_id) from key_mess where keyword_name  = '"+search+"') order by message_id asc";
		ArrayList<Message> allMessages = new ArrayList<Message>();
		Message mes;
		Cursor cur = db.rawQuery(sql, null);
		if(cur==null) return null;
		cur.moveToFirst();
		
		Log.i("QueryMessages",":"+sql+":"+cur.getCount());
		while(!cur.isAfterLast()){
			mes = new Message();
			mes.setCategory_type(cur.getInt(cur.getColumnIndex("category_type")));
			mes.setMessage_id(cur.getInt(cur.getColumnIndex("message_id")));
			mes.setMessage_name(cur.getString(cur.getColumnIndex("message_name")));
			mes.setMessage_body(cur.getString(cur.getColumnIndex("message_body")));
			mes.setMessage_desc(cur.getString(cur.getColumnIndex("message_desc")));
			allMessages.add(mes);
			cur.moveToNext();
		}
		cur.close();
		cur=null;
		
		//从message_name、message_body中搜索
		sql = "select * from message where (message_name like '%"+search+ "%' or (message_name not like '%"+search+"%' and message_body like '%"+search
		+"%')) and message_id not in (select distinct(message_id) from key_mess where keyword_name = '"+search+"')";
		cur = db.rawQuery(sql, null);
		if(cur==null) return allMessages;
		cur.moveToFirst();
		Log.i("QueryMessages",":"+sql+":"+cur.getCount());
		while(!cur.isAfterLast()){
			mes = new Message();
			mes.setCategory_type(cur.getInt(cur.getColumnIndex("category_type")));
			mes.setMessage_id(cur.getInt(cur.getColumnIndex("message_id")));
			mes.setMessage_name(cur.getString(cur.getColumnIndex("message_name")));
			mes.setMessage_body(cur.getString(cur.getColumnIndex("message_body")));
			mes.setMessage_desc(cur.getString(cur.getColumnIndex("message_desc")));
			allMessages.add(mes);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return allMessages;
	}
	public ArrayList<Message>  getMessages(int type,int num) {
		// TODO Auto-generated method stub
		//String sql = "select * from category where category_type="+type+" limit "+(num-1)*50+",50 order by message_id asc";
		String sql = "select * from message where category_type="+type;
		Log.i("getMessages",":"+sql+":"+db);
		ArrayList<Message> allMessages = new ArrayList<Message>();
		Message mes;
		Cursor cur = db.rawQuery(sql, null);
		if(cur==null) return allMessages;
		cur.moveToFirst();
		while(!cur.isAfterLast()){
			mes = new Message();
			mes.setCategory_type(cur.getInt(cur.getColumnIndex("category_type")));
			mes.setMessage_id(cur.getInt(cur.getColumnIndex("message_id")));
			mes.setMessage_name(cur.getString(cur.getColumnIndex("message_name")));
			mes.setMessage_body(cur.getString(cur.getColumnIndex("message_body")));
			mes.setMessage_desc(cur.getString(cur.getColumnIndex("message_desc")));
			allMessages.add(mes);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return allMessages;
	}

	public ArrayList<Key_Mess> getKey_Messes(int messageId) {
		// TODO Auto-generated method stub
		String sql = "select * from key_mess where message_id="+messageId+" order by message_id asc";
		Log.i("getKey_Messes",":"+sql+":"+db);
		ArrayList<Key_Mess> allKey_Messes = new ArrayList<Key_Mess>();
		Key_Mess mes;
		Cursor cur = db.rawQuery(sql, null);
		if(cur==null) return null;
		cur.moveToFirst();
		while(!cur.isAfterLast()){
			mes = new Key_Mess();
			mes.setMessage_id(cur.getInt(cur.getColumnIndex("message_id")));
			mes.setKeyword_name((cur.getString(cur.getColumnIndex("keyword_name"))));
			mes.setKey_mess_desc((cur.getString(cur.getColumnIndex("key_mess_desc"))));
			allKey_Messes.add(mes);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return allKey_Messes;
	}
	public void clearTables() {
		// TODO Auto-generated method stub
		db.execSQL("delete from  "+Category_TABLE);	
		db.execSQL("delete from  "+FAVORITE_TABLE);	
		db.execSQL("delete from  "+Message_TABLE);	
		db.execSQL("delete from  "+Key_Mess_TABLE);	
		db.execSQL("delete from  "+W_Message_LIST);	
		db.execSQL("delete from  "+W_Message_TABLE);	
	}
	public ArrayList<WMessageList> getWMessageList() {
		// TODO Auto-generated method stub
		String sql = "select * from wmessagelist where category_type="+5;
		Log.i("getWMessageList",":"+sql+":"+db);
		ArrayList<WMessageList> allMessages = new ArrayList<WMessageList>();
		WMessageList mes;
		Cursor cur = db.rawQuery(sql, null);
		if(cur==null) return allMessages;
		cur.moveToFirst();
		while(!cur.isAfterLast()){
			mes = new WMessageList();
			mes.setCategory_type(cur.getInt(cur.getColumnIndex("category_type")));
			mes.setWmessagelist_id(cur.getInt(cur.getColumnIndex("wmessagelist_id")));
			mes.setWmessagelist_name(cur.getString(cur.getColumnIndex("wmessagelist_name")));
			allMessages.add(mes);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return allMessages;
	}
	public ArrayList<WMessage> getWMessages( int id) {
		// TODO Auto-generated method stub
		String sql = "select * from wmessage where wmessagelist_id="+id+" order by wmessage_id asc ";
		Log.i("getWMessages",":"+sql+":"+db);
		ArrayList<WMessage> allMessages = new ArrayList<WMessage>();
		WMessage mes;
		Cursor cur = db.rawQuery(sql, null);
		if(cur==null) return allMessages;
		cur.moveToFirst();
		while(!cur.isAfterLast()){
			mes = new WMessage();
			mes.setWmessage_answer(cur.getString(cur.getColumnIndex("wmessage_answer")));
			mes.setWmessagelist_id(cur.getInt(cur.getColumnIndex("wmessagelist_id")));
			mes.setWmessage_body(cur.getString(cur.getColumnIndex("wmessage_body")));
			mes.setWmessage_name(cur.getString(cur.getColumnIndex("wmessage_name")));
			mes.setWmessage_id(cur.getInt(cur.getColumnIndex("wmessage_id")));
			allMessages.add(mes);
			cur.moveToNext();
		}
		cur.close();
		db.close();
		return allMessages;
	}
}
