package com.gy.filewindows.floatwindows;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

import com.gy.fileserver.DataBaseHelper;
import com.gy.filewindows.R;
import com.gy.filewindows.appwidget.Gridviewadapter;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class BigFloatService extends Service {
	LinearLayout mFloatLayout;
	WindowManager.LayoutParams wmParams;
	WindowManager mWindowManager;
	Button mFloatView;
	private GridView gridView;
	//private Button closebutton;
	private int x, y, statusBarHeight;
	private float mTouchStartX, mTouchStartY, mX, mY;
	private boolean initViewPlace = false;
	private ImageView floatwindowimg,closeimg;
	private OnTouchListener touchlistener;
	private String spinneritem[] = {"自选","文档","软件","视频","压缩包","照片","音乐"};
	private String spinnerdata[] = {"bookmarkdatabase","textdatabase","apkdatabase","videodatabase","compassdatabase","photodatabase","musicdatabase"};
	private Spinner spinner;
	private List<String> filelist;
	@Override
	public void onCreate() {
		// TODO Auto-generated method
		creatbigfloatview(x, y);
		super.onCreate();
	}

	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		x = intent.getIntExtra("x", 0);
		y = intent.getIntExtra("y", 0);

		super.onStart(intent, startId);
	}

	public BigFloatService() {

	}

	@SuppressLint({ "InflateParams", "RtlHardcoded" })
	@SuppressWarnings("static-access")
	private void creatbigfloatview(int x, int y) {
		wmParams = new WindowManager.LayoutParams();
		mWindowManager = (WindowManager) getApplication().getSystemService(
				getApplication().WINDOW_SERVICE);
		wmParams.type = LayoutParams.TYPE_PHONE;
		wmParams.format = PixelFormat.RGBA_8888;
		wmParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL;
		wmParams.gravity = Gravity.LEFT | Gravity.TOP;
		wmParams.x = x;
		wmParams.y = y;
		wmParams.width = 640;
		wmParams.height = 640;

		// 设置悬浮窗口长宽数据
		// wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		// wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

		LayoutInflater inflater = LayoutInflater.from(getApplication());
		mFloatLayout = (LinearLayout) inflater.inflate(
				R.layout.float_big_layout, null);
		filelist = DataBaseHelper.gettableitems("bookmarkdatabase", "filelist", BigFloatService.this);
		gridView = (GridView) mFloatLayout.findViewById(R.id.gridView1);
		gridView.setAdapter(new Gridviewadapter(BigFloatService.this, filelist));
		
		spinner = (Spinner)mFloatLayout.findViewById(R.id.widget_spinner);
		spinner.setAdapter(new MySpinnerAdapter(BigFloatService.this, spinneritem));
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				filelist = DataBaseHelper.gettableitems(spinnerdata[position], "filelist", BigFloatService.this);
				gridView.setAdapter(new Gridviewadapter(BigFloatService.this, filelist));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		
		
		//closebutton = (Button) mFloatLayout.findViewById(R.id.button1);
		floatwindowimg = (ImageView)mFloatLayout.findViewById(R.id.widget_icon1);
		closeimg = (ImageView)mFloatLayout.findViewById(R.id.widget_close);
		mWindowManager.addView(mFloatLayout, wmParams);
		mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		initViewPlace = false;
		touchlistener = new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (!initViewPlace) {
						initViewPlace = true;
						// 获取初始位置
						mTouchStartX = event.getRawX();
						mTouchStartY = event.getRawY();
						mX = event.getRawX();
						mY = event.getRawY();
					} else {
						// 根据上次手指离开的位置与此次点击的位置进行初始位置微调
						mTouchStartX += (event.getRawX() - mX);
						mTouchStartY += (event.getRawY() - mY);
					}

					break;
				case MotionEvent.ACTION_MOVE:
					// 获取相对屏幕的坐标，以屏幕左上角为原点
					mX = event.getRawX();
					mY = event.getRawY();
					wmParams.x = (int) (mX - mTouchStartX);
					wmParams.y = (int) (mY - mTouchStartY);
					mWindowManager.updateViewLayout(mFloatLayout, wmParams);
					break;

				case MotionEvent.ACTION_UP:
					break;
				}
				return false;
			}
		};
		closeimg.setOnTouchListener(touchlistener);
		floatwindowimg.setOnTouchListener(touchlistener);
		closeimg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent2 = new Intent(BigFloatService.this,
						FxService.class);
				startService(intent2);
				Intent intent = new Intent(BigFloatService.this,
						BigFloatService.class);
				stopService(intent);
			}
		});
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Log.e("gy", getPacageName());
				File file = new File(filelist.get(position));
				try {
					openFile(file);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(BigFloatService.this, "无法以当前程序打开文件",
							Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}

			}
		});
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mFloatLayout != null) {
			mWindowManager.removeView(mFloatLayout);
		}
	}

	private void openFile(File f) throws Exception {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(f), "*/*");
		intent.setPackage(getPacageName());
		startActivity(intent);
	}

	private String getPacageName() {
		String packageName = null;
		ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		/*
		 * @SuppressWarnings("deprecation") ComponentName cn =
		 * activityManager.getRunningTasks(1).get(0).topActivity; String
		 * packageName = cn.getPackageName();
		 */
		List<ActivityManager.RunningAppProcessInfo> appList = activityManager
				.getRunningAppProcesses();
		for (RunningAppProcessInfo running : appList) {
			if (running.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				packageName = running.processName;
				break;
			}

		}

		return packageName;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unused")
	private int getStatusBarHeight() {
		if (statusBarHeight == 0) {
			try {
				Class<?> c = Class.forName("com.android.internal.R$dimen");
				Object o = c.newInstance();
				Field field = c.getField("status_bar_height");
				int x = (Integer) field.get(o);
				statusBarHeight = getResources().getDimensionPixelSize(x);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return statusBarHeight;
	}
	
}
