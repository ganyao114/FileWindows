package com.gy.fileserver;

import java.io.File;

import com.gy.filewindows.MainActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class Openfileshortcut extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		File file = new File(intent.getStringExtra("filepath"));
		if (file.isDirectory()) {
			opendir(file);
		} else {
			try {
				openFile(file);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Toast.makeText(Openfileshortcut.this, "´ò¿ªÊ§°Ü", Toast.LENGTH_SHORT)
						.show();
				e.printStackTrace();
			}
		}
		finish();
	}

	private void openFile(File f) throws Exception {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(f), getfiletype(f));
		startActivity(intent);
	}
	private void opendir(File f) {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setClass(this,MainActivity.class).addCategory(Intent.CATEGORY_LAUNCHER).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("path", f.getPath());
		startActivity(intent);
	}
	@SuppressLint("DefaultLocale")
	private String getfiletype(File file) {
		return GetFileProperty.getfileopenkind(file);
	}

}
