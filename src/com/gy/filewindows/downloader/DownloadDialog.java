package com.gy.filewindows.downloader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.gc.materialdesign.views.ButtonFlat;
import com.gy.filewindows.R;
import com.gy.widget.meteriadialog.MaterialDialog;
import com.rengwuxian.materialedittext.MaterialEditText;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class DownloadDialog {
	private Spinner spinner;
	private LayoutInflater mInflater;
	private Context mContext;
	private View contentView;
	private MaterialDialog materialDialog;
	
	private final static String TAG = "MainActivity";
	private final static int Processing = 1;//�������ر�־
	private final static int Failure = -1;//����ʧ�ܱ�־��
	private MaterialEditText et_path;//·�������ı���
	private ButtonFlat btn_start , btn_stop;//��ʼ����ͣ���ذ�ť
	private TextView tv_progress;//��ʾ���Ȱٷֱ�
	private ProgressBar progressBar;//���ؽ�������ʵʱͼ�λ���ʾ����
	private MyHandler mHandler = new MyHandler();
	private Downloadask task ;
	public DownloadDialog(Context context)
	{	
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		contentView = mInflater.inflate(R.layout.downloaddialog_layout, null);
		spinner = (Spinner) contentView.findViewById(R.id.pathspinner);
		materialDialog = new MaterialDialog(context);
		initView();
		setDialog();
	}
	
	private void setDialog()
	{
		materialDialog.setTitle("����·��");
		materialDialog.setContentView(contentView);
		materialDialog.setPositiveButton("����", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				materialDialog.dismiss();
				Toast.makeText(mContext, "�Ƿ�·��!", Toast.LENGTH_LONG).show();
			}
		});
		materialDialog.setNegativeButton("ȡ��", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				materialDialog.dismiss();
			}
		});
		materialDialog.show();
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		et_path = (MaterialEditText) contentView.findViewById(R.id.pathedt);
		List<String> list = new ArrayList<String>();
		list.add("�ĵ�");
		list.add("���");
		list.add("��Ƶ");
		list.add("ѹ����");
		list.add("��Ƭ");
		list.add("����");
		spinner.setAdapter(new MySpinnerAdapter(mContext, list));
	}
	
	private class MyHandler extends Handler{
		/* (non-Javadoc)
		 * @see android.os.Handler#handleMessage(android.os.Message)
	     ***************************************************************************************
		 *ϵͳ���Զ��ص����������ڴ�����Ϣ�¼�
		 *Message һ��������Ϣ�ı�־����Ϣ�������Լ���Ϣ�Ĵ�����Handler
		 */
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Processing:   //����ʱ
				//����Ϣ�л�ȡ�Ѿ����ص����ݳ���
				 int size = msg.getData().getInt("size");
				 progressBar.setProgress(size);//���ý�������ʾ�Ľ���
				 //�����Ѿ����صİٷֱȣ�Ҫʹ�ø���ֵ����
				 float num =(float)progressBar.getProgress()/(float)progressBar.getMax();
				 int result = (int) (num*100);//�ѻ�õĸ�������������
				 tv_progress.setText(result+"%");//�����صİٷֱ���ʾ�ڽ�����
				 //Log.i(TAG, "������Processing,"+"size "+size+"KB,  result�ٷֱ��ǣ�"+result+"%");
				 if (progressBar.getProgress() == progressBar.getMax()) {
					//���������ʱ
					 btn_start.setEnabled(true);
					 btn_stop.setEnabled(false);
					 Toast.makeText(mContext, "���سɹ�", Toast.LENGTH_SHORT).show();
				}
				break;
				
			case Failure :    //����ʧ��ʱ
				Log.i(TAG, "�յ�Failure");
				 Toast.makeText(mContext, "����ʧ��", Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		}
	}
	
	private class Downloadask extends Thread{
		private String path;//����·��
		private File saveDir; //���صı����ļ�
		private FileDownloader loader; //�ļ�������
		/******
		 * < Constructor > ��ʼ������
		 * @param path �ļ�����·��
		 * @param saveDir �ļ�����·��
	     *****************************************************************************************
		 */
		public Downloadask(String path, File saveDir) {
			this.path = path;
			this.saveDir = saveDir;
		}

		/*****
		 *  < Method >  ������������ڣ����˳�����
	     *****************************************************************************************
		 */
		public void exit(){
			if (loader != null) {
				loader.exit();//������������ڣ����˳�����
			}
		}
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
	     *****************************************************************************************
		 */
		@Override
		public void run() {
			loader = new FileDownloader(mContext, path, saveDir, 3);//��ʼ������
			Log.i(TAG, "���������ֵ(�ļ���С)��"+loader.getFileSize());
			progressBar.setMax(loader.getFileSize());//���ý��������ֵΪ�ļ��Ĵ�С
			loader.downLoad(new DownloadProgressListener() {
				@Override
				public void onDownloadSize(int downloadSize) {
					Message msg = new Message();//���������̷߳������ؽ��ȵ�Messageduixia
					msg.what = Processing;//����Ϊ1
					msg.getData().putInt("size", downloadSize);//���ļ����صĳ��ȷŵ�Message����
					mHandler.sendMessage(msg);//������Ϣ����Ϣ����
				}
			});
		}
	}
	
}
