package com.gy.filewindows.appwidget;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.gy.fileserver.DataBaseHelper;
import com.gy.fileserver.GetFileProperty;
import com.gy.filewindows.R;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.TextView;

@SuppressLint("NewApi")
public class ListViewService extends RemoteViewsService {

	@Override
	public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
		// TODO Auto-generated method stub
		List<String> filelist = new ArrayList<String>();
		int appWidgetId = intent.getIntExtra(
				AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
		return new ListRemoteViewsFactory(this, filelist, intent, appWidgetId);

	}

	private static class ListRemoteViewsFactory implements
			RemoteViewsService.RemoteViewsFactory {
		private List<String> mList;
		private Context mContext;
		private int appwidgetid;

		// ππ‘ÏListRemoteViewsFactory
		public ListRemoteViewsFactory(Context context, List<String> list,
				Intent intent, int appWidgetId2) {
			mList = list;
			mContext = context;
			appwidgetid = appWidgetId2;

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public RemoteViews getLoadingView() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public RemoteViews getViewAt(int position) {
			// TODO Auto-generated method stub

			RemoteViews rv = new RemoteViews(mContext.getPackageName(),
					R.layout.grid_item);
			File file = new File(mList.get(position));
			rv.setImageViewResource(R.id.coverimg,
					iconselect(getMIMEType(file)));
			rv.setTextViewText(R.id.name, file.getName());
			rv.setTextViewText(R.id.size, GetFileProperty.getFileSizeMb(file));
			Intent fillInIntent = new Intent();
			fillInIntent.putExtra(listwidgetprovider.COLLECTION_VIEW_EXTRA,
					position);
			fillInIntent.putExtra("filepath", mList.get(position));
			rv.setOnClickFillInIntent(R.id.itemLayout, fillInIntent);

			return rv;

		}

		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public void onCreate() {
			// TODO Auto-generated method stub
			mList = DataBaseHelper.gettableitems("appwidgetdatabase", "appwidget" + appwidgetid, mContext);
			for (int i = 0;i<mList.size();i++) {
				if (!isfilcunzai(mList.get(i))) {
					mList.remove(i);
				}
			}
		}
		 private boolean isfilcunzai(String path) {
				File file = new File(path);
		    	
		    	return file.exists();
		 }
		@Override
		public void onDataSetChanged() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onDestroy() {
			// TODO Auto-generated method stub
			mList.clear();
			mList = null;
		}

		private int iconselect(String type) {
			return GetFileProperty.getfileicon(type);
		}

		@SuppressLint("DefaultLocale")
		private String getMIMEType(File f) {
			return GetFileProperty.getfileopenkind(f);
		}
	}
   
	

}