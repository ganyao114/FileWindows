package com.gy.filewindows.appwidget;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.gc.materialdesign.views.ButtonRectangle;
import com.gy.fileserver.DataBaseHelper;
import com.gy.filewindows.R;
import com.gy.widget.listview.Filelistadapter;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.Toast;

public class AppWidgetConfigruation extends ListActivity {
	private ButtonRectangle text, app, rar, video, photo, music, determin;
	private int appwidgetid;
	public static final String COLLECTION_VIEW_ACTION = "com.gy.filewindows.appwidget.COLLECTION_VIEW_ACTION";
	private List<String> item, paths;
	private List<String> filelist = new ArrayList<String>();
	private String rootpath = "/";
	private GridView gridView;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_widget_configruation);
		gridView = (GridView) findViewById(R.id.tmpfilegridView);
		text = (ButtonRectangle) findViewById(R.id.bttext);
		app = (ButtonRectangle) findViewById(R.id.btapp);
		rar = (ButtonRectangle) findViewById(R.id.btrar);
		video = (ButtonRectangle) findViewById(R.id.btvideo);
		photo = (ButtonRectangle) findViewById(R.id.btphoto);
		music = (ButtonRectangle) findViewById(R.id.btmusic);
		determin = (ButtonRectangle) findViewById(R.id.confirm);
		getdir(rootpath);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			appwidgetid = bundle.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
		}
		Intent intent2 = new Intent(getApplicationContext(),
				ListViewService.class);
		intent2.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appwidgetid);
		intent2.setData(Uri.parse(intent2.toUri(Intent.URI_INTENT_SCHEME)));
		Intent gridIntent = new Intent();
		gridIntent.setAction(COLLECTION_VIEW_ACTION);
		gridIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appwidgetid);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
				appwidgetid, gridIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
		RemoteViews remoteViews = new RemoteViews(this.getPackageName(),
				R.layout.appwidgetlayout);
		remoteViews.setRemoteAdapter(R.id.gridview, intent2);
		remoteViews.setPendingIntentTemplate(R.id.gridview, pendingIntent);
		appWidgetManager.updateAppWidget(appwidgetid, remoteViews);
		determin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				DataBaseHelper.addnewtable("appwidgetdatabase", "appwidget"+appwidgetid, filelist, getApplicationContext());
				Intent resultIntent = new Intent();
				resultIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
						appwidgetid);
				setResult(RESULT_OK, resultIntent);
				finish();
			}
		});
		text.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				click("textdatabase");
			}
		});
		app.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				click("apkdatabase");
			}
		});
		rar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				click("compassdatabase");
			}
		});
		video.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				click("videodatabase");
			}
		});
		photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				click("photodatabase");
			}
		});
		music.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				click("musicdatabase");
			}
		});
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				filelist.remove(position);
				gridView.setAdapter(new Gridviewadapter(
						AppWidgetConfigruation.this, filelist));
			}
		});
	}
	
	public void click(String databasename) {
		DataBaseHelper.addnewtable("appwidgetdatabase", "appwidget"+appwidgetid, DataBaseHelper.gettableitems(databasename, "filelist",getApplicationContext()), getApplicationContext());
		Intent resultIntent = new Intent();
		resultIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appwidgetid);
		setResult(RESULT_OK, resultIntent);
		finish();
	}

	private void getdir(String filepath) {
		item = new ArrayList<String>();
		paths = new ArrayList<String>();
		File file = new File(filepath);
		File[] files = file.listFiles();
		if (!filepath.equals(rootpath)) {
			item.add("back");
			paths.add(file.getParent());
		}
		for (int i = 0; i < files.length; i++) {
			File tempfile = files[i];
			item.add(tempfile.getName());
			paths.add(tempfile.getPath());
		}
		setListAdapter(new Filelistadapter(AppWidgetConfigruation.this, item,
				paths));
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		File templefile = new File(paths.get(position));
		if (templefile.isDirectory()) {
			try {
				getdir(paths.get(position));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				getdir(rootpath);
				Toast.makeText(getApplicationContext(), "未获得权限无法查看",
						Toast.LENGTH_LONG).show();
			}
		} else {
			filelist.add(templefile.getPath());
			gridView.setAdapter(new Gridviewadapter(
					AppWidgetConfigruation.this, filelist));
		}
	}
}
