package com.gy.filewindows.setting;

import com.gc.materialdesign.views.CheckBox;
import com.gc.materialdesign.views.CheckBox.OnCheckListener;
import com.gy.filewindows.R;
import com.gy.filewindows.floatwindows.FxService;
import com.gy.widget.meteriadialog.MaterialDialog;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class Setting {
	private MaterialDialog mDialog;
	private CheckBox ck;
	public Setting(final Context context) {
		View view = View.inflate(context,R.layout.layout_setting,null);
		ck = (CheckBox)view.findViewById(R.id.ckbox1);
		mDialog = new MaterialDialog(context);
		mDialog.setTitle("����");
		mDialog.setContentView(view);
		mDialog.setPositiveButton("ȷ��", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDialog.dismiss();
			}
		});
		mDialog.setNegativeButton("ȡ��", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDialog.dismiss();
			}
		});
		mDialog.show();
		ck.setOncheckListener(new OnCheckListener() {
			
			@Override
			public void onCheck(boolean check) {
				// TODO Auto-generated method stub
				if (check) {
					Intent intent = new Intent(context,
							FxService.class);
					context.startService(intent);
				} else {
					Intent intent = new Intent(context,
							FxService.class);
					context.stopService(intent);
				}
			}
		});
	}
	
}
