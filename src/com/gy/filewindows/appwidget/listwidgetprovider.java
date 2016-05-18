package com.gy.filewindows.appwidget;

import java.io.File;

import com.gy.fileserver.DataBaseHelper;
import com.gy.fileserver.GetFileProperty;
import com.gy.filewindows.R;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

@SuppressLint("NewApi")
public class listwidgetprovider extends AppWidgetProvider {
	public static final String BT_REFRESH_ACTION = "com.gy.filewindows.appwidget.BT_REFRESH_ACTION";
	public static final String COLLECTION_VIEW_ACTION = "com.gy.filewindows.appwidget.COLLECTION_VIEW_ACTION";
	public static final String COLLECTION_VIEW_EXTRA = "com.gy.filewindows.appwidget.COLLECTION_VIEW_EXTRA";
	@SuppressLint("NewApi")
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub

		for (int appWidgetId : appWidgetIds) {

			RemoteViews rv = new RemoteViews(context.getPackageName(),
					R.layout.appwidgetlayout);
			//rv.setTextViewText(R.id.widget_label, "文件视窗");
			Intent serviceIntent = new Intent(context, ListViewService.class);
			serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
					appWidgetId);
			serviceIntent.setData(Uri.parse(serviceIntent
					.toUri(Intent.URI_INTENT_SCHEME)));
			rv.setRemoteAdapter(R.id.gridview, serviceIntent);
			Intent gridIntent = new Intent();
			gridIntent.setAction(COLLECTION_VIEW_ACTION);
			gridIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
					appWidgetId);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
					appWidgetId, gridIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			rv.setPendingIntentTemplate(R.id.gridview, pendingIntent);
			appWidgetManager.updateAppWidget(appWidgetId, rv);
		}
		super.onUpdate(context, appWidgetManager, appWidgetIds);

	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		// AppWidgetManager
		// appWidgetManager=AppWidgetManager.getInstance(context);

		DataBaseHelper dHelper = new DataBaseHelper(context,
				"appwidgetdatabase", null, 1);
		SQLiteDatabase db = dHelper.getWritableDatabase();
		db.execSQL("DROP TABLE appwidget" + appWidgetIds[0]);
		db.close();
		dHelper.close();
		super.onDeleted(context, appWidgetIds); 
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		// AppWidgetManager appWidgetManager =
		// AppWidgetManager.getInstance(context);

		if (action.equals(COLLECTION_VIEW_ACTION)) {

			File file = new File(intent.getStringExtra("filepath"));
			try {
				openFile(file, context, getfiletype(file));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Toast.makeText(context, "打开失败", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		}
		super.onReceive(context, intent);
	}

	private void openFile(File f, Context context, String filetype)
			throws Exception {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(f), filetype);
		context.startActivity(intent);
	}

	@SuppressLint("DefaultLocale")
	private String getfiletype(File file) {
		return GetFileProperty.getfileopenkind(file);
	}

}
