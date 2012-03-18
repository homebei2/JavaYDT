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

import com.wing.ydt.Application;
import com.wing.ydt.R;
import com.wing.ydt.handler.HandlerFactory;
import com.wing.ydt.vo.Category;
import com.wing.ydt.vo.ListItem;
import com.wing.ydt.vo.Message;
import com.wing.ydt.vo.WMessageList;

public class DBAdapter {
	private static final String DATABASE_NAME = "wpp.db";
	private static final String DATABASE_PATH = "/data/data/com.wing.ydt/databases";
	private static final String Category_TABLE = "category";
	private static final String Message_TABLE = "message";
	private static final String WMessage_TABLE = "wmessage";
	private static int Version = 5;
	private static final String Category_CREATE = "create table "
			+ Category_TABLE
			+ "(category_type INTEGER PRIMARY KEY ASC,category_name TEXT,category_desc TEXT);";
	private static final String Message_CREATE = "create table  "
			+ Message_TABLE
			+ "(message_id INTEGER PRIMARY KEY ASC,messagelist_id INTEGER,category_type INTEGER NOT NULL,message_name TEXT,message_body TEXT,favorite INTEGER,message_answer text);";
	private static final String WMessage_CREATE = "create table  "
			+ WMessage_TABLE
			+ "(message_id INTEGER PRIMARY KEY ASC,messagelist_id INTEGER,category_type INTEGER NOT NULL,message_name TEXT,message_body TEXT,favorite INTEGER,message_answer text);";

	private static final String W_Message_LIST = "wmessagelist";
	private static final String W_Message_LIST_CREATE = "create table  "
			+ W_Message_LIST
			+ "(wmessagelist_id INTEGER PRIMARY KEY ASC,category_type INTEGER NOT NULL,wmessagelist_name TEXT,wmessagelist_desc TEXT);";

	private static DBAdapter dbAdapter;
	private SQLiteDatabase db;
	private DatabaseHelper dbHelper;
	private Context context;

	private DBAdapter(Context ctx) {
		if (context == null)
			context = ctx;
		if (dbHelper == null)
			dbHelper = new DatabaseHelper(context);
		if (db == null)
			db = dbHelper.getWritableDatabase();
		Log.i("DBAdapter", "initialize:");
	}

	public static DBAdapter getInstance(Context ctx) {
		if (dbAdapter == null) {
			synchronized (DBAdapter.class) {
				if (dbAdapter == null) {
					dbAdapter = new DBAdapter(ctx);
				}
			}
		}
		return dbAdapter;
	}

	public static DBAdapter getInstance() {
		if (dbAdapter == null) {
			synchronized (DBAdapter.class) {
				if (dbAdapter == null) {
					dbAdapter = new DBAdapter(Application.getContext());
				}
			}
		}
		return dbAdapter;
	}

	private class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, Version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// create table
			Log.i("onCreateTable", ":");
			if (Application.IS_DEBUG) {
				db.execSQL(Category_CREATE);
				db.execSQL(Message_CREATE);
				db.execSQL(W_Message_LIST_CREATE);
				db.execSQL(WMessage_CREATE);
				HandlerFactory.instance.initDB(Application.getContext(), db);
			} else {
				copyDB();
			}

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// reLoad raw resources
			// HandlerFactory.instance.initDB(Application.getContext());
//			copyDB();
		}

	}

	public static void copyDB() {
		try {
			// 获得dictionary.db文件的绝对路径
			String databaseFilename = DATABASE_PATH + "/" + DATABASE_NAME;
			File dir = new File(DATABASE_PATH);
			// 如果/sdcard/dictionary目录中存在，创建这个目录
			if (!dir.exists())
				dir.mkdir();
			// 如果在/sdcard/dictionary目录中不存在
			// dictionary.db文件，则从res\raw目录中复制这个文件到
			// SD卡的目录（/sdcard/dictionary）
			for (File file : dir.listFiles())
				file.delete();
			Log.i(databaseFilename, "copy db");
			// 获得封装dictionary.db文件的InputStream对象
			InputStream is = Application.getContext().getResources().openRawResource(R.raw.wpp);
			FileOutputStream fos = new FileOutputStream(databaseFilename);
			byte[] buffer = new byte[8192];
			int count = 0;
			// 开始复制db文件
			while ((count = is.read(buffer)) > 0) {
				fos.write(buffer, 0, count);
			}
			fos.close();
			is.close();
		} catch (Exception e) {
			Log.e("CopyDB", "Error:" + e.getMessage());
		}
	}

	public void close() {
		try {
			if (db.isOpen())
				db.close();

		} catch (Exception e) {
		}
		try {
			dbHelper.close();
		} catch (Exception e) {
		}

	}
	public void saveFavorite(Message category) {
		// TODO Auto-generated method stub
		// Log.i("saveFavorite",":"+category.getMessage_id());
		String table = category.getCategory_type() == 5 ? WMessage_TABLE
				: Message_TABLE;
		ContentValues cv = new ContentValues();
		cv.put("favorite", category.isFavorite() ? 1 : 0);
		String[] args = new String[] { category.getMessage_id() + "" };
		db.update(table, cv, " message_id=?", args);
	}

	public ArrayList<ListItem> getFavorites(int num, int flag) {
		String table = flag == 0 ? Message_TABLE : WMessage_TABLE;
		String sql = "select * from " + table + " where favorite=1";
		// Log.i("getFavorites",":"+sql+":"+db);
		ArrayList<ListItem> allMessages = new ArrayList<ListItem>();
		Message mes;
		Cursor cur = db.rawQuery(sql, null);
		if (cur == null)
			return allMessages;
		cur.moveToFirst();
		while (!cur.isAfterLast()) {
			mes = new Message();
			mes.setCategory_type(cur
					.getInt(cur.getColumnIndex("category_type")));
			mes.setMessage_id(cur.getInt(cur.getColumnIndex("message_id")));
			mes.setMessage_name(cur.getString(cur
					.getColumnIndex("message_name")));
			mes.setMessage_body(cur.getString(cur
					.getColumnIndex("message_body")));
			mes.setMessage_answer(cur.getString(cur
					.getColumnIndex("message_answer")));
			mes.setMessagelist_id(cur.getInt(cur
					.getColumnIndex("messagelist_id")));
			mes.setFavorite(1);
			allMessages.add(mes);
			cur.moveToNext();
		}
		cur.close();
		// db.close();
		return allMessages;
	}

	public static void saveMessages(ArrayList<Object> allMessages,
			SQLiteDatabase db) {
		// TODO Auto-generated method stub
		ContentValues cv;
		Message category;
		for (int i = 0; i < allMessages.size(); i++) {
			cv = new ContentValues();
			category = (Message) allMessages.get(i);
			cv.put("message_name", category.getMessage_name());
			cv.put("message_body", category.getMessage_body());
			cv.put("message_answer", category.getMessage_answer());
			cv.put("messagelist_id", category.getMessagelist_id());
			cv.put("category_type", category.getCategory_type());
			cv.put("favorite", category.isFavorite() ? 1 : 0);
			db.insert(Message_TABLE, null, cv);
		}
	}

	public long saveMessage(Message category, Boolean isAdd) {
		ContentValues cv = new ContentValues();
		cv.put("message_name", category.getMessage_name());
		cv.put("message_body", category.getMessage_body());
		cv.put("message_answer", category.getMessage_answer());
		cv.put("favorite", category.isFavorite() ? 1 : 0);
		if (isAdd) {
			cv.put("messagelist_id", category.getMessagelist_id());
			cv.put("category_type", category.getCategory_type());
			return db.insert(Message_TABLE, null, cv);
		} else {
			String[] args = new String[] { category.getMessage_id() + "" };
			db.update(Message_TABLE, cv, " message_id=?", args);
			return 0;
		}

	}

	public static void saveCategories(ArrayList<Object> allCategories,
			SQLiteDatabase db) {
		// TODO Auto-generated method stub
		ContentValues cv;
		Category category;
		for (int i = 0; i < allCategories.size(); i++) {
			cv = new ContentValues();
			category = (Category) allCategories.get(i);
			cv.put("category_type", category.getCategory_type());
			cv.put("category_name", category.getCategory_name());
			cv.put("category_desc", category.getCategory_desc());
			db.insert(Category_TABLE, null, cv);
		}
	}

	public long saveCategory(Category category, Boolean isAdd) {
		ContentValues cv = new ContentValues();
		cv.put("category_name", category.getCategory_name());
		cv.put("category_desc", category.getCategory_desc());
		if (isAdd) {
			return db.insert(Category_TABLE, null, cv);
		} else {
			String[] args = new String[] { category.getCategory_type() + "" };
			db.update(Category_TABLE, cv, " category_type=?", args);
			return 0;
		}
	}

	public ArrayList<ListItem> QueryCategories() {
		String sql = "select * from category order by category_type asc";
		ArrayList<ListItem> allCategories = new ArrayList<ListItem>();
		Category cat;
		Cursor cur = db.rawQuery(sql, null);
		if (cur == null)
			return null;
		cur.moveToFirst();
		while (!cur.isAfterLast()) {
			cat = new Category();
			cat.setCategory_type(cur
					.getInt(cur.getColumnIndex("category_type")));
			cat.setCategory_name(cur.getString(cur
					.getColumnIndex("category_name")));
			cat.setCategory_desc(cur.getString(cur
					.getColumnIndex("category_desc")));
			allCategories.add(cat);
			cur.moveToNext();
		}
		cur.close();
		return allCategories;
	}

	public String QueryCategoryName(int type) {
		// TODO Auto-generated method stub
		String sql = "select category_name from category where category_type ="
				+ type;
		Cursor cur = db.rawQuery(sql, null);
		String name;
		if (cur == null)
			return "";
		cur.moveToFirst();
		name = cur.getString(cur.getColumnIndex("category_name"));
		cur.close();
		return name;
	}

	public ArrayList<ListItem> QueryMessages(String search, int flag) {
		// TODO Auto-generated method stub
		String table = flag == 0 ? Message_TABLE : WMessage_TABLE;
		ArrayList<ListItem> allMessages = new ArrayList<ListItem>();
		Message mes;
		// 从message_name、message_body中搜索
		String sql = "select * from  " + table
				+ "  where (message_name like '%" + search
				+ "%' or (message_name not like '%" + search
				+ "%' and message_body like '%" + search + "%')) ";
		Cursor cur = db.rawQuery(sql, null);
		if (cur == null)
			return allMessages;
		cur.moveToFirst();
		Log.i("QueryMessages", ":" + sql + ":" + cur.getCount());
		while (!cur.isAfterLast()) {
			mes = new Message();
			mes.setCategory_type(cur
					.getInt(cur.getColumnIndex("category_type")));
			mes.setMessage_id(cur.getInt(cur.getColumnIndex("message_id")));
			mes.setMessage_name(cur.getString(cur
					.getColumnIndex("message_name")));
			mes.setMessage_body(cur.getString(cur
					.getColumnIndex("message_body")));
			mes.setMessage_answer(cur.getString(cur
					.getColumnIndex("message_answer")));
			mes.setMessagelist_id(cur.getInt(cur
					.getColumnIndex("messagelist_id")));
			mes.setFavorite(cur.getInt(cur.getColumnIndex("favorite")));
			allMessages.add(mes);
			cur.moveToNext();
		}
		cur.close();
		// db.close();
		return allMessages;
	}

	public ArrayList<ListItem> getMessages(int type, int num) {
		return getMessages(type, 9999, num);
	}

	public ArrayList<ListItem> getMessages(int type, int list_id, int num) {
		// TODO Auto-generated method stub
		// String sql =
		// "select * from category where category_type="+type+" limit "+(num-1)*50+",50 order by message_id asc";
		if (type == 5 && list_id == 9999) {
			return getWMessageList();
		} else if (type == 7) {
			return getFavorites(num, 0);
		} else {
			String sql = null;
			if (list_id == 9999) {
				sql = "select * from message where category_type=" + type;
			} else {
				sql = "select * from wmessage where messagelist_id=" + list_id;
			}
			Log.i("getMessages", ":" + sql + ":" + db);
			ArrayList<ListItem> allMessages = new ArrayList<ListItem>();
			Message mes;
			Cursor cur = db.rawQuery(sql, null);
			if (cur == null)
				return allMessages;
			cur.moveToFirst();
			while (!cur.isAfterLast()) {
				mes = new Message();
				mes.setCategory_type(cur.getInt(cur
						.getColumnIndex("category_type")));
				mes.setMessage_id(cur.getInt(cur.getColumnIndex("message_id")));
				mes.setMessage_name(cur.getString(cur
						.getColumnIndex("message_name")));
				mes.setMessage_body(cur.getString(cur
						.getColumnIndex("message_body")));
				mes.setMessage_answer(cur.getString(cur
						.getColumnIndex("message_answer")));
				mes.setMessagelist_id(cur.getInt(cur
						.getColumnIndex("messagelist_id")));
				mes.setFavorite(cur.getInt(cur.getColumnIndex("favorite")));
				allMessages.add(mes);
				cur.moveToNext();
			}
			cur.close();
			// db.close();
			return allMessages;
		}
	}

	public void clearTables() {
		// TODO Auto-generated method stub
		db.execSQL("delete from  " + Category_TABLE);
		db.execSQL("delete from  " + Message_TABLE);
		db.execSQL("delete from  " + W_Message_LIST);
		db.execSQL("delete from  " + WMessage_TABLE);
	}

	public static void saveWMessages(ArrayList<Object> allMessages,
			SQLiteDatabase db) {
		// TODO Auto-generated method stub
		ContentValues cv;
		Message category;
		for (int i = 0; i < allMessages.size(); i++) {
			cv = new ContentValues();
			category = (Message) allMessages.get(i);
			cv.put("message_name", category.getMessage_name());
			cv.put("message_body", category.getMessage_body());
			cv.put("message_answer", category.getMessage_answer());
			cv.put("messagelist_id", category.getMessagelist_id());
			cv.put("category_type", category.getCategory_type());
			cv.put("favorite", category.isFavorite() ? 1 : 0);
			db.insert(WMessage_TABLE, null, cv);
		}
	}

	public long saveWMessage(Message category, Boolean isAdd) {
		ContentValues cv = new ContentValues();
		cv.put("message_name", category.getMessage_name());
		cv.put("message_body", category.getMessage_body());
		cv.put("message_answer", category.getMessage_answer());
		cv.put("favorite", category.isFavorite() ? 1 : 0);
		if (isAdd) {
			cv.put("messagelist_id", category.getMessagelist_id());
			cv.put("category_type", category.getCategory_type());
			return db.insert(WMessage_TABLE, null, cv);
		} else {
			String[] args = new String[] { category.getMessage_id() + "" };
			db.update(WMessage_TABLE, cv, " message_id=?", args);
			return 0;
		}
	}

	public static void saveWMessageList(ArrayList<Object> allMessages,
			SQLiteDatabase db) {
		// TODO Auto-generated method stub
		ContentValues cv;
		WMessageList category;
		for (int i = 0; i < allMessages.size(); i++) {
			cv = new ContentValues();
			category = (WMessageList) allMessages.get(i);
			cv.put("wmessagelist_name", category.getWmessagelist_name());
			cv.put("wmessagelist_desc", category.getWmessagelist_desc());
			cv.put("category_type", category.getCategory_type());
			db.insert(W_Message_LIST, null, cv);
		}
	}

	public long saveWMessageList(WMessageList category, Boolean isAdd) {
		ContentValues cv = new ContentValues();
		cv.put("wmessagelist_name", category.getWmessagelist_name());
		cv.put("wmessagelist_desc", category.getWmessagelist_desc());
		if (isAdd) {
			cv.put("category_type", category.getCategory_type());
			return db.insert(W_Message_LIST, null, cv);
		} else {
			String[] args = new String[] { category.getWmessagelist_id() + "" };
			db.update(W_Message_LIST, cv, " wmessagelist_id=?", args);
			return 0;
		}
	}

	public ArrayList<ListItem> getWMessageList() {
		// TODO Auto-generated method stub
		String sql = "select * from wmessagelist where category_type=" + 5;
		Log.i("getWMessageList", ":" + sql + ":" + db);
		ArrayList<ListItem> allMessages = new ArrayList<ListItem>();
		WMessageList mes;
		Cursor cur = db.rawQuery(sql, null);
		if (cur == null)
			return allMessages;
		cur.moveToFirst();
		while (!cur.isAfterLast()) {
			mes = new WMessageList();
			mes.setCategory_type(cur
					.getInt(cur.getColumnIndex("category_type")));
			mes.setWmessagelist_id(cur.getInt(cur
					.getColumnIndex("wmessagelist_id")));
			mes.setWmessagelist_name(cur.getString(cur
					.getColumnIndex("wmessagelist_name")));
			mes.setWmessagelist_desc(cur.getString(cur
					.getColumnIndex("wmessagelist_desc")));
			allMessages.add(mes);
			cur.moveToNext();
		}
		cur.close();
		// db.close();
		return allMessages;
	}

	public String getWMessageListName(int id) {
		// TODO Auto-generated method stub
		String sql = "select * from wmessagelist where wmessagelist_id=" + id;
		Log.i("getWMessageListName", ":" + sql + ":" + db);
		Cursor cur = db.rawQuery(sql, null);
		if (cur == null)
			return "";
		cur.moveToFirst();
		String name = cur.getString(cur.getColumnIndex("wmessagelist_name"));
		cur.close();
		// db.close();
		return name;
	}

	public int delListItem(ListItem item) {
		if (item == null) {
			return 0;
		} else if (item instanceof Category) {
			String[] args = new String[] { ((Category) item).getCategory_type()
					+ "" };
			return db.delete(Category_TABLE, " category_type=?", args);
		} else if (item instanceof WMessageList) {
			String[] args = new String[] { ((WMessageList) item)
					.getWmessagelist_id()
					+ "" };
			return db.delete(W_Message_LIST, " wmessagelist_id=?", args);
		} else if (item instanceof Message) {
			String[] args = new String[] { ((Message) item).getMessage_id()
					+ "" };
			if (((Message) item).getMessagelist_id() > 0) {
				return db.delete(WMessage_TABLE, " message_id=?", args);
			} else {
				return db.delete(Message_TABLE, " message_id=?", args);
			}
		} else {
			return 0;
		}
	}
}
