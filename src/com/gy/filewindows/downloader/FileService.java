package com.gy.filewindows.downloader;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**********
 *  @author   YinJie
 *  <Type Create time>     2013��11��13��  ��    ����11:38:57
 *  TODO  >>  
 *   ҵ��Bean,ʵ�ֶ����ݿ�Ĳ���
 *****************************************************************************************
 */
public class FileService {
	//private  final static String TAG = "FileService";
	private DBOpenHelper openHelper;  //�������ݿ������
	
	/***
	 * < Constructor >
     *****************************************************************************************
	 */
	public FileService(Context context) {
		//ʵ�������ݿ����ݿ������
		openHelper = new DBOpenHelper(context);
	}

	/***
	 *  < Method >  
	 *  ��ȡÿ���߳����س��ȵķ���
	 *  @param path �ļ����ص�·�������������ݿ��л�ȡ��·�����ļ�������Ϣ
	 *  @return �����߳�ID�ʹ��߳����صĳ��ȵļ���
     *****************************************************************************************
	 */
	public Map<Integer,Integer> getData(String path){
		//��ÿɶ����ݿ�����һ������¸ò������ڲ�ʵ����ʵ�ʷ��ص��ǿ�д�����ݿ���
		SQLiteDatabase db = openHelper.getReadableDatabase();
		//�������ص�·����ѯ�����߳����ص����ݣ����ص�cursor���ص�һ����¼
		Cursor cursor = db.rawQuery("select threadid,downlength from downloadlog where downpath=?", 
																		new String[]{path});
		//����һ����ϣ�����ڴ���ÿ���߳��Ѿ����ص��ļ�����
		Map<Integer, Integer> data = new HashMap<Integer, Integer>();
			//����cursor����
			while (cursor.moveToNext()) {
				//���߳�ID��(threadid)�͸��߳��Ѿ����صĳ���(downlength)��Ϊ��ֵ�Դ浽��ϣ��
				//map.put(cursor.getInt(0), cursor.getInt(1));
				data.put(cursor.getInt(cursor.getColumnIndex("threadid")), 
									cursor.getInt(cursor.getColumnIndex("downlength")));
			}
		cursor.close();//�ر�cursor�ͷ���Դ
		db.close();//�ر����ݿ�
		return data;//����ÿ���߳����صĳ��ȼ�
	}
	/***
	 *  < Method >  
	 *  ����ÿ���߳����ص��ļ��ĳ���
	 *  @param path �ļ�����·��
	 *  @param map �̵߳�ID�ʹ��߳����س��ȵļ���
     *****************************************************************************************
	 */
	public void save(String path , Map<Integer,Integer> map){
		//��ÿɶ����ݿ�����һ������¸ò������ڲ�ʵ����ʵ�ʷ��ص��ǿ�д�����ݿ���
				SQLiteDatabase db = openHelper.getReadableDatabase();
				db.beginTransaction();//��ʼ������Ϊ�˴�Ҫ�����������
				try {
					for (Map.Entry<Integer,Integer> entry : map.entrySet()) {
						//����For-Each��ʽ�������ݼ���
						//�����ض���·�����̼߳��������ص��ļ��ĳ���
						db.execSQL("insert into downloadlog(downpath,threadid,downlength) values (?,?,?);", 
												new Object[]{path,entry.getKey(),entry.getValue()});
						//��������ִ�еı�־Ϊ�ɹ�
					}
					db.setTransactionSuccessful();
				}finally{
					//�����ɱ��������Ļ����˲��ִ����Ǳض��ᱻִ�е�
					//����һ������������������˳ɹ���־���ύ���񣬷�������ع�
					db.endTransaction();
				}
				db.close();//�ر����ݿ��ͷ���Դ
	}
	
	/*****
	 *  < Method >  
	 *  ʵʱ����ÿ���߳��Ѿ����ص��ļ��ĳ���
	 *  @param path �ļ�����·��
	 *  @param threadId �̵߳�ID
	 *  @param position ���ص�λ��
     *****************************************************************************************
	 */
	public void update(String path , int threadId , int position){
		//��ÿɶ����ݿ�����һ������¸ò������ڲ�ʵ����ʵ�ʷ��ص��ǿ�д�����ݿ���
		SQLiteDatabase db = openHelper.getReadableDatabase();
		db.beginTransaction();
		try{
			//�����ض�������·�����ض��̣߳��Ѿ����ص��ļ�����
			db.execSQL("update downloadlog set downlength=? where downpath=? and threadid=?;",
									new Object[]{position, path, threadId});
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
		db.close();//�ر����ݿ��ͷ���Դ
	}
	
	/*****
	 *  < Method >  
	 *  ���ļ��������֮��ɾ����ص����ؼ�¼
	 *  @param path �ļ����ص�·��
     *****************************************************************************************
	 */
	public void delete(String path){
		//��ÿɶ����ݿ�����һ������¸ò������ڲ�ʵ����ʵ�ʷ��ص��ǿ�д�����ݿ���
		SQLiteDatabase db = openHelper.getReadableDatabase();
		//ɾ���ض�����·���������̼߳�¼
		db.execSQL("delete from downloadlog where downpath=?", new String[]{path});
		db.close();//�ر����ݿ��ͷ���Դ
	}
}
