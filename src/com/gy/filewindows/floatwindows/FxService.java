package com.gy.filewindows.floatwindows;


import com.gy.filewindows.R;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class FxService extends Service {

	LinearLayout mFloatLayout;
	WindowManager.LayoutParams wmParams;

	WindowManager mWindowManager;

	ImageView mFloatView;

	// private static final String TAG = "FxService";

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// Log.i(TAG, "oncreat");
		createFloatView();
		// Toast.makeText(FxService.this, "create FxService",
		// Toast.LENGTH_LONG);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressLint({ "InflateParams", "RtlHardcoded" })
	@SuppressWarnings("static-access")
	private void createFloatView() {
		wmParams = new WindowManager.LayoutParams();
		// ��ȡWindowManagerImpl.CompatModeWrapper
		mWindowManager = (WindowManager) getApplication().getSystemService(
				getApplication().WINDOW_SERVICE);
		// ����window type
		wmParams.type = LayoutParams.TYPE_PHONE;
		// ����ͼƬ��ʽ��Ч��Ϊ����͸��
		wmParams.format = PixelFormat.RGBA_8888;
		// ���ø������ڲ��ɾ۽���ʵ�ֲ���������������������ɼ����ڵĲ�����
		wmParams.flags =
		// LayoutParams.FLAG_NOT_TOUCH_MODAL |
		LayoutParams.FLAG_NOT_FOCUSABLE
		// LayoutParams.FLAG_NOT_TOUCHABLE
		;

		// ������������ʾ��ͣ��λ��Ϊ����ö�
		wmParams.gravity = Gravity.LEFT | Gravity.TOP;

		// ����Ļ���Ͻ�Ϊԭ�㣬����x��y��ʼֵ
		wmParams.x = 0;
		wmParams.y = 0;

		/*
		 * // �����������ڳ������� wmParams.width = 200; wmParams.height = 80;
		 */

		// �����������ڳ�������
		wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

		LayoutInflater inflater = LayoutInflater.from(getApplication());
		// ��ȡ����������ͼ���ڲ���
		mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_layout,
				null);
		// ����mFloatLayout
		mWindowManager.addView(mFloatLayout, wmParams);

		// Log.i(TAG, "mFloatLayout-->left" + mFloatLayout.getLeft());
		// Log.i(TAG, "mFloatLayout-->right" + mFloatLayout.getRight());
		// Log.i(TAG, "mFloatLayout-->top" + mFloatLayout.getTop());
		// Log.i(TAG, "mFloatLayout-->bottom" + mFloatLayout.getBottom());

		// �������ڰ�ť
		mFloatView = (ImageView) mFloatLayout
				.findViewById(R.id.circleImageView1);

		mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		// Log.i(TAG, "Width/2--->" + mFloatView.getMeasuredWidth()/2);
		// Log.i(TAG, "Height/2--->" + mFloatView.getMeasuredHeight()/2);
		// ���ü����������ڵĴ����ƶ�
		mFloatView.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				// getRawX�Ǵ���λ���������Ļ�����꣬getX������ڰ�ť������
				wmParams.x = (int) event.getRawX()
						- mFloatView.getMeasuredWidth() / 2;
				// Log.i(TAG, "Width/2--->" + mFloatView.getMeasuredWidth()/2);
				// Log.i(TAG, "RawX" + event.getRawX());
				// Log.i(TAG, "X" + event.getX());
				// 25Ϊ״̬���ĸ߶�
				wmParams.y = (int) event.getRawY()
						- mFloatView.getMeasuredHeight() / 2 - 25;
				// Log.i(TAG, "Width/2--->" + mFloatView.getMeasuredHeight()/2);
				// Log.i(TAG, "RawY" + event.getRawY());
				// Log.i(TAG, "Y" + event.getY());
				// ˢ��
				mWindowManager.updateViewLayout(mFloatLayout, wmParams);
				return false;
			}
		});

		mFloatView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Toast.makeText(FxService.this, "onClick",
				// Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(FxService.this,
						BigFloatService.class);
				intent.putExtra("x", (int) mFloatView.getX());
				intent.putExtra("y", (int)  mFloatView.getY());
				startService(intent);
				Intent intent2 = new Intent(FxService.this, FxService.class);
				stopService(intent2);

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

}