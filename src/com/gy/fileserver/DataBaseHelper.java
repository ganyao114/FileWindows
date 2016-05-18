package com.gy.fileserver;

import java.util.ArrayList;
import java.util.List;

import com.gy.filewindows.MainActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
	private static Context mContext;
	public DataBaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		
	}
	
	public static void addnewtable(String databasename, String tablename,
			List<String> items, Context context) {
		DataBaseHelper dHelper = new DataBaseHelper(context, databasename,
				null, 1);
		SQLiteDatabase db = dHelper.getWritableDatabase();
		db.execSQL("create table " + tablename
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT, path TEXT NOT NULL)");
		ContentValues contentValues = new ContentValues();
		for (int i = 1; i <= items.size(); i++) {
			contentValues.put("path", items.get(i - 1));
			db.insert(tablename, "_id", contentValues);
		}
		dHelper.close();
		db.close();
	}
	
	public static void deletetable(String databasename, String tablename,Context context) {
		
	}

	public static List<String> gettableitems(String databasename, String tablename,Context context) {

		List<String> items = new ArrayList<String>();
		DataBaseHelper dHelper = new DataBaseHelper(context, databasename,
				null, 1);
		SQLiteDatabase db = dHelper.getWritableDatabase();

		Cursor cursor = db.query(tablename, null, null, null, null, null, null);
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToFirst();
			cursor.move(i);
			items.add(cursor.getString(1));
		}
		dHelper.close();
		db.close();
		return items;
	}
	
	public static void refreshtableitems(String databasename,String tablename,List<String> items,Context context) {
		DataBaseHelper dHelper = new DataBaseHelper(context, databasename,
				null, 1);
		SQLiteDatabase db = dHelper.getWritableDatabase();
		db.execSQL("DELETE FROM "+tablename);
		db.execSQL("DELETE FROM sqlite_sequence");
		ContentValues contentValues = new ContentValues();
		for (int i = 1; i <= items.size(); i++) {
			contentValues.put("path", items.get(i - 1));
			db.insert(tablename, "_id", contentValues);
		}
		dHelper.close();
		db.close();
	}
	
	public static boolean istableempty(String databasename,String tablename,Context context) {
		DataBaseHelper dHelper = new DataBaseHelper(context, databasename,
				null, 1);
		SQLiteDatabase db = dHelper.getWritableDatabase();
		Cursor cursor = db.query(tablename, null, null, null, null, null, null);
		
		if (cursor.getCount() == 0) {
			dHelper.close();
			db.close();
			return true;
		}else {
			dHelper.close();
			db.close();
			return false;
		}
	}

	public static void initdatabase(List<String> databasenames,Context context) {
		for (String databasename : databasenames) {
			DataBaseHelper dHelper = new DataBaseHelper(context,
					databasename, null, 1);
			SQLiteDatabase db = dHelper.getWritableDatabase();
			db.execSQL("create table filelist(_id INTEGER PRIMARY KEY AUTOINCREMENT, path TEXT NOT NULL)");
			dHelper.close();
			db.close();
		}
	}
	
	public static boolean isfirstrun(Context context) {
		 SharedPreferences sharedPreferences = context.getSharedPreferences("share",Context.MODE_PRIVATE);  
		 boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);  
	     Editor editor = sharedPreferences.edit();
	     if (isFirstRun)  
	     {  
	         MainActivity.isfirstrun = true;
	         editor.putBoolean("isFirstRun", false);  
	         editor.commit();  
	     } else  
	     {  
	         MainActivity.isfirstrun = false;
	     }  
		 return MainActivity.isfirstrun;
	}
}
