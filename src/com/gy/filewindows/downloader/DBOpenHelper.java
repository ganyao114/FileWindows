package com.gy.filewindows.downloader;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**********
 *  @author   YinJie
 *  <Type Create time>     2013��11��13��  ��    ����11:33:38
 *  TODO  >>  
 *   ���ݿ⸨�������࣬ʵ�ִ����͹������ݿ���汾�仯ʱʵ��ʵ�ֶԱ�Ĳ���
 * ****************************************************************************************
 */
public class DBOpenHelper extends SQLiteOpenHelper {

	private final static String DBNAME = "eric.db";//���ݿ�����
	private final static int  VERSION = 1;//���ݿ�汾��
	
	public DBOpenHelper(Context context) {
		super(context, DBNAME, null, VERSION);
	}
	
	/*****
	 * < Constructor >
	 * ֻ�������Ļ����Ĺ��췽������������ø���Ĺ��췽��
     *****************************************************************************************
	 */
	public DBOpenHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	/*****
	 * < Constructor >
     *****************************************************************************************
	 */
	public DBOpenHelper(Context context, String name, CursorFactory factory,
			int version, DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
     *****************************************************************************************
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		//��������ھʹ�����
		String sql = "CREATE TABLE IF NOT EXISTS "
								+ "downloadlog(id integer primary key autoincrement,downpath varchar(100),"
								+ "threadid integer,downlength integer);";
		db.execSQL(sql);//ִ�����
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 * �����ݿ�İ汾�ű仯ʱ��ص��˷���
     *****************************************************************************************
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//���ݿ�汾����������Ҫɾ��ԭ���ı���ʵ��ҵ����һ��Ҫ�������ݱ��ݵ�
		String sql = "DROP TABLE IF EXISTS downloadlog;";
		db.execSQL(sql);
		//����onCreate�������´�����Ҳ�ɸ����Լ����������д����µı�
		this.onCreate(db);
	}
}
