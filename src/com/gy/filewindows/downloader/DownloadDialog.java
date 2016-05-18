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
	private final static int Processing = 1;//正在下载标志
	private final static int Failure = -1;//下载失败标志、
	private MaterialEditText et_path;//路径输入文本框
	private ButtonFlat btn_start , btn_stop;//开始，暂停下载按钮
	private TextView tv_progress;//显示进度百分比
	private ProgressBar progressBar;//下载进度条，实时图形化显示进度
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
		materialDialog.setTitle("下载路径");
		materialDialog.setContentView(contentView);
		materialDialog.setPositiveButton("下载", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				materialDialog.dismiss();
				Toast.makeText(mContext, "非法路径!", Toast.LENGTH_LONG).show();
			}
		});
		materialDialog.setNegativeButton("取消", new OnClickListener() {
			
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
		list.add("文档");
		list.add("软件");
		list.add("视频");
		list.add("压缩包");
		list.add("照片");
		list.add("音乐");
		spinner.setAdapter(new MySpinnerAdapter(mContext, list));
	}
	
	private class MyHandler extends Handler{
		/* (non-Javadoc)
		 * @see android.os.Handler#handleMessage(android.os.Message)
	     ***************************************************************************************
		 *系统会自动回调方法，用于处理消息事件
		 *Message 一般会包含消息的标志和消息的内容以及消息的处理器Handler
		 */
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Processing:   //下载时
				//从消息中获取已经下载的数据长度
				 int size = msg.getData().getInt("size");
				 progressBar.setProgress(size);//设置进度条显示的进度
				 //计算已经下载的百分比，要使用浮点值计算
				 float num =(float)progressBar.getProgress()/(float)progressBar.getMax();
				 int result = (int) (num*100);//把获得的浮点数换成整数
				 tv_progress.setText(result+"%");//把下载的百分比显示在界面上
				 //Log.i(TAG, "下载中Processing,"+"size "+size+"KB,  result百分比是："+result+"%");
				 if (progressBar.getProgress() == progressBar.getMax()) {
					//当下载完成时
					 btn_start.setEnabled(true);
					 btn_stop.setEnabled(false);
					 Toast.makeText(mContext, "下载成功", Toast.LENGTH_SHORT).show();
				}
				break;
				
			case Failure :    //下载失败时
				Log.i(TAG, "收到Failure");
				 Toast.makeText(mContext, "下载失败", Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		}
	}
	
	private class Downloadask extends Thread{
		private String path;//下载路径
		private File saveDir; //下载的保存文件
		private FileDownloader loader; //文件下载器
		/******
		 * < Constructor > 初始化变量
		 * @param path 文件下载路径
		 * @param saveDir 文件保存路径
	     *****************************************************************************************
		 */
		public Downloadask(String path, File saveDir) {
			this.path = path;
			this.saveDir = saveDir;
		}

		/*****
		 *  < Method >  如果下载器存在，就退出下载
	     *****************************************************************************************
		 */
		public void exit(){
			if (loader != null) {
				loader.exit();//如果下载器存在，就退出下载
			}
		}
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
	     *****************************************************************************************
		 */
		@Override
		public void run() {
			loader = new FileDownloader(mContext, path, saveDir, 3);//初始化下载
			Log.i(TAG, "进度条最大值(文件大小)："+loader.getFileSize());
			progressBar.setMax(loader.getFileSize());//设置进度条最大值为文件的大小
			loader.downLoad(new DownloadProgressListener() {
				@Override
				public void onDownloadSize(int downloadSize) {
					Message msg = new Message();//用于向主线程发送下载进度的Messageduixia
					msg.what = Processing;//设置为1
					msg.getData().putInt("size", downloadSize);//把文件下载的长度放到Message对象
					mHandler.sendMessage(msg);//发送消息到消息队列
				}
			});
		}
	}
	
}
